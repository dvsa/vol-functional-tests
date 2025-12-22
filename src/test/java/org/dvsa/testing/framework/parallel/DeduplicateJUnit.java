package org.dvsa.testing.framework.parallel;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DeduplicateJUnit {
    public static void main(String[] args) throws Exception {
        String inputDir = "target/surefire-reports";
        String outputFile = "target/results-summary/summary.xml";

        // Map to hold the latest testcase for each (classname, name)
        Map<String, Element> deduplicatedTestcases = new LinkedHashMap<>();

        // Get all XML files sorted by last modified time
        List<Path> xmlFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputDir), "*.xml")) {
            for (Path entry : stream) {
                xmlFiles.add(entry);
            }
        }
        xmlFiles.sort(Comparator.comparingLong(f -> f.toFile().lastModified()));

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        for (Path xmlFile : xmlFiles) {
            Document doc = dBuilder.parse(xmlFile.toFile());
            NodeList testcaseNodes = doc.getElementsByTagName("testcase");
            for (int i = 0; i < testcaseNodes.getLength(); i++) {
                Element testcase = (Element) testcaseNodes.item(i);
                String key = testcase.getAttribute("classname") + "#" + testcase.getAttribute("name");
                // Import node to a new document later
                deduplicatedTestcases.put(key, testcase);
            }
        }

        // Create new summary document
        Document summaryDoc = dBuilder.newDocument();
        Element testsuite = summaryDoc.createElement("testsuite");
        summaryDoc.appendChild(testsuite);

        int tests = 0, failures = 0, errors = 0, skipped = 0;
        for (Element testcase : deduplicatedTestcases.values()) {
            Node imported = summaryDoc.importNode(testcase, true);
            testsuite.appendChild(imported);
            tests++;
            if (testcase.getElementsByTagName("failure").getLength() > 0) failures++;
            if (testcase.getElementsByTagName("error").getLength() > 0) errors++;
            if (testcase.getElementsByTagName("skipped").getLength() > 0) skipped++;
        }
        testsuite.setAttribute("tests", String.valueOf(tests));
        testsuite.setAttribute("failures", String.valueOf(failures));
        testsuite.setAttribute("errors", String.valueOf(errors));
        testsuite.setAttribute("skipped", String.valueOf(skipped));

        // Write to output file
        Files.createDirectories(Paths.get(outputFile).getParent());
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(summaryDoc), new StreamResult(new File(outputFile)));

        System.out.println("Deduplicated summary.xml created at: " + outputFile);
    }
}