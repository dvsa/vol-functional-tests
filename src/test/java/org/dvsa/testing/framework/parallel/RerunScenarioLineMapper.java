package org.dvsa.testing.framework.parallel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RerunScenarioLineMapper {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java RerunScenarioLineMapper <input_rerun.txt> <output_rerun.txt>");
            System.exit(1);
        }
        List<String> rerunEntries = Files.readAllLines(Paths.get(args[0]));
        Set<String> scenarioRefs = new HashSet<>();

        for (String entry : rerunEntries) {
            if (!entry.contains(":")) continue;
            String[] parts = entry.split(":(?=[0-9]+$)");
            String featurePath = parts[0];
            int failedLine = Integer.parseInt(parts[1]);
            List<String> lines = Files.readAllLines(Paths.get(featurePath));
            int scenarioLine = findScenarioLine(lines, failedLine);
            if (scenarioLine > 0) {
                scenarioRefs.add(featurePath + ":" + scenarioLine);
            }
        }
        Files.write(Paths.get(args[1]), scenarioRefs);
    }

    private static int findScenarioLine(List<String> lines, int failedLine) {
        for (int i = failedLine - 1; i >= 0; i--) {
            String line = lines.get(i).trim();
            if (line.startsWith("Scenario:") || line.startsWith("Scenario Outline:")) {
                return i + 1; // lines are 1-based
            }
        }
        return -1; // not found
    }
}