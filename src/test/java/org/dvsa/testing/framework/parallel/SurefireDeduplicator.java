package org.dvsa.testing.framework.parallel;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class SurefireDeduplicator {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: SurefireDeduplicator <inputDir> <outputFile>");
            System.exit(1);
        }

        String inputDir = args[0];
        String outputFile = args[1];
        boolean excludeSkipped = args.length > 2 && args[2].equalsIgnoreCase("--exclude-skipped");

        Map<String, TestCaseInfo> testCaseMap = new LinkedHashMap<>();
        Map<String, List<String>> classnameIndex = new HashMap<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        List<Path> xmlFiles = Files.list(Paths.get(inputDir))
                .filter(p -> p.toString().endsWith(".xml"))
                .filter(p -> !p.getFileName().toString().equals("summary.xml"))
                .sorted((a, b) -> {
                    String fileA = a.getFileName().toString();
                    String fileB = b.getFileName().toString();
                    boolean aIsRerun = fileA.toLowerCase().contains("rerun");
                    boolean bIsRerun = fileB.toLowerCase().contains("rerun");

                    if (aIsRerun && !bIsRerun) return 1;
                    if (!aIsRerun && bIsRerun) return -1;
                    return fileA.compareTo(fileB);
                })
                .toList();

        System.out.println("Processing " + xmlFiles.size() + " XML files...");
        System.out.println("==============================================\n");

        for (Path xmlPath : xmlFiles) {
            boolean isRerunFile = xmlPath.getFileName().toString().toLowerCase().contains("rerun");
            System.out.println("Processing: " + xmlPath.getFileName() + (isRerunFile ? " [RERUN]" : " [INITIAL]"));

            Document doc = dBuilder.parse(xmlPath.toFile());
            NodeList testcases = doc.getElementsByTagName("testcase");

            for (int i = 0; i < testcases.getLength(); i++) {
                Element tc = (Element) testcases.item(i);
                String name = tc.getAttribute("name");
                String classname = tc.getAttribute("classname");
                String key = classname + "::" + name;

                String currentStatus;
                if (tc.getElementsByTagName("error").getLength() > 0) {
                    currentStatus = "error";
                } else if (tc.getElementsByTagName("failure").getLength() > 0) {
                    currentStatus = "failure";
                } else if (tc.getElementsByTagName("skipped").getLength() > 0) {
                    currentStatus = "skipped";
                } else {
                    currentStatus = "passed";
                }

                if (isRerunFile) {
                    String matchKey = findMatchingTest(classname, name, classnameIndex);

                    if (matchKey != null) {
                        TestCaseInfo existing = testCaseMap.get(matchKey);
                        System.out.println("  *** MATCHED RERUN ***");
                        System.out.println("      Initial: " + matchKey);
                        System.out.println("      Rerun: " + key);
                        System.out.println("      Status: " + existing.status + " -> " + currentStatus);
                        testCaseMap.put(matchKey, new TestCaseInfo((Element) tc.cloneNode(true), currentStatus, true));
                    } else {
                        System.out.println("  Adding new rerun test: " + name + " [" + currentStatus + "]");
                        testCaseMap.put(key, new TestCaseInfo((Element) tc.cloneNode(true), currentStatus, true));
                        classnameIndex.computeIfAbsent(normalizeClassname(classname), k -> new ArrayList<>()).add(key);
                    }
                } else {
                    testCaseMap.put(key, new TestCaseInfo((Element) tc.cloneNode(true), currentStatus, false));
                    classnameIndex.computeIfAbsent(normalizeClassname(classname), k -> new ArrayList<>()).add(key);
                }
            }
            System.out.println();
        }

        System.out.println("==============================================");
        System.out.println("Final Test Case Breakdown:");
        System.out.println("==============================================");

        int failures = 0, errors = 0, skipped = 0, passed = 0;
        Map<String, List<String>> statusGroups = new HashMap<>();
        statusGroups.put("error", new ArrayList<>());
        statusGroups.put("failure", new ArrayList<>());
        statusGroups.put("skipped", new ArrayList<>());
        statusGroups.put("passed", new ArrayList<>());

        for (Map.Entry<String, TestCaseInfo> entry : testCaseMap.entrySet()) {
            TestCaseInfo info = entry.getValue();
            String testName = entry.getKey();

            switch (info.status) {
                case "error":
                    errors++;
                    statusGroups.get("error").add(testName + (info.fromRerun ? " [UPDATED BY RERUN]" : ""));
                    break;
                case "failure":
                    failures++;
                    statusGroups.get("failure").add(testName + (info.fromRerun ? " [UPDATED BY RERUN]" : ""));
                    break;
                case "skipped":
                    skipped++;
                    break;
                case "passed":
                    passed++;
                    if (info.fromRerun) {
                        statusGroups.get("passed").add(testName + " [PASSED ON RERUN]");
                    }
                    break;
            }
        }

        if (!statusGroups.get("error").isEmpty()) {
            System.out.println("\nERRORS (" + errors + "):");
            for (String test : statusGroups.get("error")) {
                System.out.println("  - " + test);
            }
        }

        if (!statusGroups.get("failure").isEmpty()) {
            System.out.println("\nFAILURES (" + failures + "):");
            for (String test : statusGroups.get("failure")) {
                System.out.println("  - " + test);
            }
        }

        if (!statusGroups.get("passed").isEmpty()) {
            System.out.println("\nPASSED ON RERUN (" + statusGroups.get("passed").size() + "):");
            for (String test : statusGroups.get("passed")) {
                System.out.println("  - " + test);
            }
        }

        System.out.println("\n==============================================");
        System.out.println("=== FINAL Test Summary ===");
        System.out.println("Total tests: " + testCaseMap.size());
        System.out.println("Passed: " + passed);
        System.out.println("Failures: " + failures);
        System.out.println("Errors: " + errors);
        System.out.println("Skipped: " + skipped);
        System.out.println("==============================================\n");

        Document summaryDoc = dBuilder.newDocument();
        Element testsuite = summaryDoc.createElement("testsuite");
        summaryDoc.appendChild(testsuite);

        int finalTests = 0;
        int finalFailures = 0;
        int finalErrors = 0;
        int finalSkipped = 0;

        for (TestCaseInfo info : testCaseMap.values()) {
            if (excludeSkipped && info.status.equals("skipped")) {
                continue;
            }

            Node imported = summaryDoc.importNode(info.element, true);
            testsuite.appendChild(imported);
            finalTests++;

            switch (info.status) {
                case "error": finalErrors++; break;
                case "failure": finalFailures++; break;
                case "skipped": finalSkipped++; break;
            }
        }

        testsuite.setAttribute("tests", String.valueOf(finalTests));
        testsuite.setAttribute("failures", String.valueOf(finalFailures));
        testsuite.setAttribute("errors", String.valueOf(finalErrors));
        testsuite.setAttribute("skipped", String.valueOf(finalSkipped));
        testsuite.setAttribute("name", "Deduplicated Test Results");
        testsuite.setAttribute("time", "0");

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        transformer.transform(new DOMSource(summaryDoc), new StreamResult(new File(outputFile)));

        System.out.println("Deduplicated Surefire summary written to: " + outputFile);
        if (excludeSkipped) {
            System.out.println("(Skipped tests excluded from summary)");
        }
    }

    private static String normalizeClassname(String classname) {
        String normalized = classname.replaceAll(" - Examples.*$", "").trim();
        String[] parts = normalized.split(" - ");
        return parts[0].trim();
    }

    private static String findMatchingTest(String rerunClassname, String rerunName, Map<String, List<String>> classnameIndex) {
        String normalizedRerunClass = normalizeClassname(rerunClassname);

        List<String> candidates = classnameIndex.get(normalizedRerunClass);
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        Pattern pattern = Pattern.compile("^(.+?)\\s*#(\\d+)$");
        Matcher matcher = pattern.matcher(rerunName);

        String baseRerunName = rerunName;
        String exampleNumber = null;

        if (matcher.matches()) {
            baseRerunName = matcher.group(1).trim();
            exampleNumber = matcher.group(2);
        }

        for (String candidateKey : candidates) {
            String candidateClass = candidateKey.substring(0, candidateKey.indexOf("::"));
            String candidateName = candidateKey.substring(candidateKey.indexOf("::") + 2);

            if (normalizeClassname(candidateClass).equals(normalizedRerunClass)) {
                if (exampleNumber != null) {
                    Pattern examplePattern = Pattern.compile("Example #1\\." + exampleNumber + "$");
                    if (examplePattern.matcher(candidateName).find() && candidateName.contains(baseRerunName)) {
                        return candidateKey;
                    }
                }

                if (candidateName.contains(rerunName) ||
                        (candidateName.endsWith(rerunName) ||
                                candidateName.contains(" - " + rerunName))) {
                    return candidateKey;
                }
            }
        }

        return null;
    }

    private static class TestCaseInfo {
        final Element element;
        final String status;
        final boolean fromRerun;

        TestCaseInfo(Element element, String status, boolean fromRerun) {
            this.element = element;
            this.status = status;
            this.fromRerun = fromRerun;
        }
    }
}