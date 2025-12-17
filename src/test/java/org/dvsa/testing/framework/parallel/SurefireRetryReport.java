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

public class SurefireRetryReport {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: java SurefireRetryReport <surefire-xml> <rerun.txt> <rerun-cucumber>");
            System.exit(1);
        }
        String surefireXmlPath = args[0];
        String rerunTxtPath = args[1];
        String rerunCucumberPath = args[2];

        Set<String> failedTestNames = getFailedTestNames(rerunTxtPath, rerunCucumberPath);
        if (failedTestNames.isEmpty()) {
            System.out.println("No rerun failures detected, no update needed.");
            return;
        }
        updateSurefireXml(surefireXmlPath, failedTestNames);
    }

    private static Set<String> getFailedTestNames(String rerunTxtPath, String rerunCucumberPath) throws IOException {
        Set<String> failed = new HashSet<>();
        Path rerunTxt = Paths.get(rerunTxtPath);
        if (Files.exists(rerunTxt) && Files.size(rerunTxt) > 0) {
            List<String> lines = Files.readAllLines(rerunTxt);
            for (String line : lines) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) failed.add(trimmed);
            }
        } else {
            Path rerunCucumber = Paths.get(rerunCucumberPath);
            if (Files.exists(rerunCucumber)) {
                String content = new String(Files.readAllBytes(rerunCucumber));
                Pattern p = Pattern.compile("^([^\n\r]+?) -- Time elapsed:", Pattern.MULTILINE);
                Matcher m = p.matcher(content);
                while (m.find()) {
                    failed.add(m.group(1).trim());
                }
            }
        }
        return failed;
    }

    private static void updateSurefireXml(String surefireXmlPath, Set<String> failedTestNames) throws Exception {
        File xmlFile = new File(surefireXmlPath);
        if (!xmlFile.exists()) {
            System.err.println("Surefire XML not found: " + surefireXmlPath);
            System.exit(1);
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile);

        NodeList testcases = doc.getElementsByTagName("testcase");
        int errors = 0;
        for (int i = 0; i < testcases.getLength(); i++) {
            Element tc = (Element) testcases.item(i);
            String name = tc.getAttribute("name");
            NodeList children = tc.getChildNodes();
            for (int j = children.getLength() - 1; j >= 0; j--) {
                Node n = children.item(j);
                if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals("error")) {
                    tc.removeChild(n);
                }
            }
            if (failedTestNames.contains(name)) {
                Element errorNode = doc.createElement("error");
                errorNode.setAttribute("type", "Error");
                errorNode.setTextContent("Failed after all reruns.");
                tc.appendChild(errorNode);
                errors++;
            }
        }
        Element suite = doc.getDocumentElement();
        suite.setAttribute("errors", Integer.toString(errors));

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(xmlFile));
        System.out.println("Surefire report updated with rerun failures.");
    }
}