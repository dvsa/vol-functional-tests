package org.dvsa.testing.framework.document;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class XMLParser {

    public Set<String> browser;

    public XMLParser() {
        browser = new HashSet<>();
    }

    public void addBrowser(String retrievedBrowser) {
        browser.add(retrievedBrowser);
    }

    public void getElementsByTagName() {
        try {
            File inputFile = new File("src/test/resources/browserStack/testNG.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("parameter");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    addBrowser(eElement.getAttribute("value"));
                    addBrowser(eElement.getAttribute("version"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
