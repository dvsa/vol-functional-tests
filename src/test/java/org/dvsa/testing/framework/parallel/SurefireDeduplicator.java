package org.dvsa.testing.framework.parallel;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SurefireDeduplicator {
    
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: SurefireDeduplicator <inputDir> <outputFile>");
            System.exit(1);
        }
        
        String inputDir = args[0];
        String outputFile = args[1];
        
        Map<String, Element> testCaseMap = new LinkedHashMap<>();
        Map<String, String> testCaseStatus = new HashMap<>();
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        List<Path> xmlFiles = Files.list(Paths.get(inputDir))
                .filter(p -> p.toString().endsWith(".xml"))
                .sorted((a, b) -> {
                    String fileA = a.getFileName().toString();
                    String fileB = b.getFileName().toString();
                    boolean aIsRerun = fileA.contains("rerun");
                    boolean bIsRerun = fileB.contains("rerun");
                    
                    if (aIsRerun && !bIsRerun) return 1;
                    if (!aIsRerun && bIsRerun) return -1;
                    return fileA.compareTo(fileB);
                })
                .toList();
        
        System.out.println("Processing " + xmlFiles.size() + " XML files...");
        
        for (Path xmlPath : xmlFiles) {
            boolean isRerunFile = xmlPath.getFileName().toString().contains("rerun");
            System.out.println("Processing: " + xmlPath.getFileName() + (isRerunFile ? " [RERUN]" : ""));
            
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
                
                boolean shouldUpdate = !testCaseMap.containsKey(key) || isRerunFile;
                
                if (shouldUpdate) {
                    testCaseMap.put(key, (Element) tc.cloneNode(true));
                    testCaseStatus.put(key, currentStatus);
                    
                    if (isRerunFile) {
                        System.out.println("  Updated: " + name + " -> " + currentStatus);
                    }
                }
            }
        }
        
        int failures = 0, errors = 0, skipped = 0, passed = 0;
        for (String status : testCaseStatus.values()) {
            switch (status) {
                case "error": errors++; break;
                case "failure": failures++; break;
                case "skipped": skipped++; break;
                case "passed": passed++; break;
            }
        }
        
        System.out.println("\n=== Test Summary ===");
        System.out.println("Total tests: " + testCaseMap.size());
        System.out.println("Passed: " + passed);
        System.out.println("Failures: " + failures);
        System.out.println("Errors: " + errors);
        System.out.println("Skipped: " + skipped);
        
        Document summaryDoc = dBuilder.newDocument();
        Element testsuite = summaryDoc.createElement("testsuite");
        summaryDoc.appendChild(testsuite);
        
        for (Element tc : testCaseMap.values()) {
            Node imported = summaryDoc.importNode(tc, true);
            testsuite.appendChild(imported);
        }
        
        testsuite.setAttribute("tests", String.valueOf(testCaseMap.size()));
        testsuite.setAttribute("failures", String.valueOf(failures));
        testsuite.setAttribute("errors", String.valueOf(errors));
        testsuite.setAttribute("skipped", String.valueOf(skipped));
        testsuite.setAttribute("name", "Deduplicated Test Results");
        testsuite.setAttribute("time", "0");
        
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        transformer.transform(new DOMSource(summaryDoc), new StreamResult(new File(outputFile)));
        
        System.out.println("\nDeduplicated Surefire summary written to: " + outputFile);
    }
}
