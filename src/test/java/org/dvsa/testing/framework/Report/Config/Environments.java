package org.dvsa.testing.framework.Report.Config;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Environments {

    public HashMap<String, String> envVariables = new HashMap<>();{
        envVariables.put("Browser",System.getProperty("browser"));
        envVariables.put("Browser.Version", System.getProperty("browserVersion"));
        envVariables.put("Platform",System.getProperty("platform"));
        envVariables.put("Environment",System.getProperty("env"));
    }

    public void createResultsFolder() throws IOException {
        File DirectoryPath = new File(System.getProperty("user.dir") + "/target/allure-results");
        if (!DirectoryPath.exists()){
                FileUtils.forceMkdir(DirectoryPath);
            }
    }

    public void generateXML(){
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("environment");
            doc.appendChild(rootElement);

            Iterator it = envVariables.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                // parameter element
                Element supercar = doc.createElement("parameter");
                rootElement.appendChild(supercar);

                Element key = doc.createElement("key");
                key.appendChild(doc.createTextNode(String.valueOf(pair.getKey())));
                supercar.appendChild(key);

                Element value = doc.createElement("value");
                value.appendChild(doc.createTextNode(String.valueOf(pair.getValue())));
                supercar.appendChild(value);

                it.remove(); // avoids a ConcurrentModificationException
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(System.getProperty("user.dir") + "/target/allure-results/environment.xml"));
            transformer.transform(source, result);

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
