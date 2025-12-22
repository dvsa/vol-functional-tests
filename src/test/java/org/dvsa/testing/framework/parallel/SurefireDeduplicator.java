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
                .sorted((a, b) -> a.getFileName().toString().contains("rerun") ? 1 : -1)
                .toList();

        for (Path xmlPath : xmlFiles) {
            Document doc = dBuilder.parse(xmlPath.toFile());
            NodeList testcases = doc.getElementsByTagName("testcase");
            for (int i = 0; i < testcases.getLength(); i++) {
                Element tc = (Element) testcases.item(i);
                String name = tc.getAttribute("name");
                String classname = tc.getAttribute("classname");
                String key = classname + "::" + name;
                testCaseMap.put(key, (Element) tc.cloneNode(true));
                // Determine status: error > failure > skipped > passed
                if (tc.getElementsByTagName("error").getLength() > 0) testCaseStatus.put(key, "error");
                else if (tc.getElementsByTagName("failure").getLength() > 0) testCaseStatus.put(key, "failure");
                else if (tc.getElementsByTagName("skipped").getLength() > 0) testCaseStatus.put(key, "skipped");
                else testCaseStatus.put(key, "passed");
            }
        }

        int failures = 0, errors = 0, skipped = 0;
        for (String status : testCaseStatus.values()) {
            switch (status) {
                case "error": errors++; break;
                case "failure": failures++; break;
                case "skipped": skipped++; break;
            }
        }

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

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(summaryDoc), new StreamResult(new File(outputFile)));

        System.out.println("Deduplicated Surefire summary written to " + outputFile);
    }
}