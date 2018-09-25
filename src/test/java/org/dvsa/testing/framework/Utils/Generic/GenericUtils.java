package org.dvsa.testing.framework.Utils.Generic;

import activesupport.MissingRequiredArgument;
import activesupport.driver.Browser;
import activesupport.jenkins.Jenkins;
import activesupport.jenkins.JenkinsParameterKey;
import activesupport.system.Properties;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.CreateLicenceAPI;
import org.dvsa.testing.lib.pages.BasePage;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zeroturnaround.zip.ZipUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


public class GenericUtils extends BasePage {

    private World world;
    private String registrationNumber;
    private static final String zipFilePath = "/src/test/resources/ESBR.zip";
    private String trafficAreaName;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public GenericUtils(World world) throws MissingRequiredArgument {
        this.world = world;
        world.createLicence = new CreateLicenceAPI();
    }

    public void modifyXML(String dateState, int months) {
        try {
            String xmlFile = "./src/test/resources/ESBR/ESBR.xml";
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDoc = xmlBuilder.parse(xmlFile);
            //update licence number
            NodeList nodeList = xmlDoc.getElementsByTagName("*");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    // do something with the current element

                    if ("StartDate".equals(node.getNodeName())) {
                        node.setTextContent(getDates(dateState, months));
                    }
                    if ("LicenceNumber".equals(node.getNodeName())) {
                        node.setTextContent(world.createLicence.getLicenceNumber());
                    }
                    if ("RegistrationNumber".equals(node.getNodeName())) {
                        String getContent = node.getTextContent();
                        int newRegNumber = Integer.parseInt(getContent);
                        setRegistrationNumber(String.valueOf(newRegNumber + 1));
                        node.setTextContent(getRegistrationNumber());
                    }
                    if ("TrafficAreaName".equals(node.getNodeName())) {
                        switch (world.APIJourneySteps.getTrafficAreaName()) {
                            case "Wales":
                                node.setTextContent("Welsh");
                                break;
                            case "Scotland":
                                node.setTextContent("Scottish");
                                break;
                            default:
                                node.setTextContent("WestMidlands");
                                break;
                        }

                    }
                }
            }
            // write the content on console
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDoc);
            System.out.println("-----------Modified File-----------");

            StreamResult result = new StreamResult(new File(xmlFile));
            transformer.transform(source, result);
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDates(String state, int months) {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String myDate = null;

        switch (state) {
            case "futureMonth":
                myDate = date.format(now.plusMonths(months));
                break;
            case "futureDay":
                myDate = date.format(now.plusDays(months));
                break;
            case "past":
                myDate = date.format(now.minusMonths(months));
                break;
            case "current":
                myDate = date.format(now);
                break;
            default:
                System.out.println(state + ": does not exist, needs to either be 'current', or 'past' or 'futureDay' or 'futureMonth'");
        }
        return myDate;
    }

    public static void zipFolder() {
        /*
        / Uses Open source util zt-zip https://github.com/zeroturnaround/zt-zip
         */
        ZipUtil.pack(new File("./src/test/resources/ESBR"), new File("./src/test/resources/ESBR.zip"));
    }

    public void executeJenkinsBatchJob(String command) throws Exception {
        HashMap<String, String> jenkinsParams = new HashMap<>();
        jenkinsParams.put(JenkinsParameterKey.NODE.toString(), String.format("api&&%s&&olcs", Properties.get("env", true)));
        jenkinsParams.put(JenkinsParameterKey.JOB.toString(), command);

        Jenkins.trigger(Jenkins.Job.BATCH_PROCESS_QUEQUE, jenkinsParams);
    }

    public String stripNonAlphanumericCharacters(String value) {
        return value.replaceAll("[^A-Za-z0-9]", "");
    }

    public String stripAlphaCharacters(String value) {
        return value.replaceAll("[^0-9]", "");
    }

    public static java.time.LocalDate getFutureDate(@NotNull int month) {
        java.time.LocalDate date = java.time.LocalDate.now().plusMonths(month);
        return date;
    }

    public static java.time.LocalDate getPastDate(@NotNull int years) {
        java.time.LocalDate date = java.time.LocalDate.now().minusYears(years);
        return date;
    }

    public String confirmationPanel(String locator, String cssValue) throws IllegalBrowserException {
        return Browser.navigate().findElement(By.xpath(locator)).getCssValue(cssValue);
    }

    public void switchTab(int tab) throws IllegalBrowserException {
        ArrayList<String> tabs = new ArrayList<>(Browser.navigate().getWindowHandles());
        Browser.navigate().switchTo().window(tabs.get(tab));
    }
}