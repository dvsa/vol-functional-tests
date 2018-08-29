package org.dvsa.testing.framework.Utils.Generic;

import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import activesupport.MissingRequiredArgument;
import activesupport.http.RestUtils;
import activesupport.jenkins.Jenkins;
import activesupport.jenkins.JenkinsParameterKey;
import activesupport.system.Properties;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.dvsa.testing.framework.Utils.API_Builders.GenericBuilder;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.CreateLicenceAPI;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.GrantLicenceAPI;
import org.dvsa.testing.framework.Utils.API_Headers.Headers;
import org.dvsa.testing.framework.stepdefs.World;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.dvsa.testing.framework.Utils.API_Headers.Headers.getHeaders;

public class GenericUtils extends BasePage {

    private static EnvironmentType env;

    static {
        try {
            env = EnvironmentType.getEnum(Properties.get("env", false));
        } catch (MissingRequiredArgument missingRequiredArgument) {
            missingRequiredArgument.printStackTrace();
        }
    }

    private World world;
    private ValidatableResponse apiResponse;
    private String registrationNumber;
    private static final String zipFilePath = "/src/test/resources/ESBR.zip";
    private String trafficAreaName;
    private static String variationApplicationNumber;
    private static String operatorType = System.getProperty("operatorType");

    public static int tmCount;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getTrafficAreaName() {
        return trafficAreaName;
    }

    public void setTrafficAreaName(String trafficAreaName) {
        this.trafficAreaName = trafficAreaName;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public GenericUtils(World world) throws MissingRequiredArgument {
        this.world = world;
        world.createLicence = new CreateLicenceAPI();
        world.grantLicence = new GrantLicenceAPI(world);
    }

    public void payFeesAndGrantLicence() throws MalformedURLException {
        if (variationApplicationNumber != null) {
            world.grantLicence.createOverview(variationApplicationNumber);
            world.grantLicence.variationGrant(variationApplicationNumber);
        } else {
            world.grantLicence.createOverview(world.createLicence.getApplicationNumber());
            world.grantLicence.getOutstandingFees(world.createLicence.getApplicationNumber());
            world.grantLicence.payOutstandingFees(world.createLicence.getOrganisationId(), world.createLicence.getApplicationNumber());
            world.grantLicence.grant(world.createLicence.getApplicationNumber());
        }
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
                        switch (getTrafficAreaName()) {
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
                System.out.println(state + ": does not exist, needs to either be 'current', 'past' or 'future'");
        }
        return myDate;
    }

    public static void zipFolder() {
        /*
        / Uses Open source util zt-zip https://github.com/zeroturnaround/zt-zip
         */
        ZipUtil.pack(new File("./src/test/resources/ESBR"), new File("./src/test/resources/ESBR.zip"));
    }

    public void createAdminUser() throws MissingRequiredArgument {
        world.updateLicence.createInternalAdminUser();
    }

    public void nIAddressBuilder() {
        world.createLicence.setEnforcementArea("EA-N");
        world.createLicence.setTrafficArea("N");
        world.createLicence.setTown("Belfast");
        world.createLicence.setPostcode("BT28HQ");
        world.createLicence.setCountryCode("NI");
        world.createLicence.setNiFlag("Y");
    }

    public void getLicenceTrafficArea() {
        Headers.getHeaders().put("x-pid", CreateLicenceAPI.getAdminUserHeader());
        String getApplicationResource = org.dvsa.testing.lib.url.api.URL.build(env, String.format("licence/%s", world.createLicence.getLicenceId())).toString();

        apiResponse = RestUtils.get(getApplicationResource, getHeaders());
        setTrafficAreaName(apiResponse.extract().jsonPath().getString("trafficArea.name"));
    }

    public void generateAndGrantPsvApplicationPerTrafficArea(String trafficArea, String enforcementArea) throws Exception {
        world.createLicence.setTrafficArea(trafficArea);
        world.createLicence.setEnforcementArea(enforcementArea);
        world.createLicence.setOperatorType("public");
        world.createLicence.createAndSubmitApp();
        payFeesAndGrantLicence();
        world.grantLicence.payGrantFees();
        getLicenceTrafficArea();
        System.out.println("--Licence-Number: " + world.createLicence.getLicenceNumber() + "--");
    }

    public void updateLicenceStatus(String licenceId, String status) throws MalformedURLException {
        String typeOfLicenceResource = org.dvsa.testing.lib.url.api.URL.build(env, String.format("licence/%s/decisions/%s", licenceId, status)).toString();

        GenericBuilder genericBuilder = new GenericBuilder().withId(licenceId);
        apiResponse = RestUtils.post(genericBuilder, typeOfLicenceResource, getHeaders());
        assertThat(apiResponse.statusCode(HttpStatus.SC_CREATED));
    }

    public CreateLicenceAPI createApp() throws MissingRequiredArgument {
        CreateLicenceAPI api = new CreateLicenceAPI();
        return api;
    }

    public GrantLicenceAPI grantLicence() throws MissingRequiredArgument {
        GrantLicenceAPI grantLicenceAPI = new GrantLicenceAPI(world);
        return grantLicenceAPI;
    }

    public void createApplication() throws Exception {
        if (world.createLicence.getApplicationNumber() == null) {
            world.createLicence.createAndSubmitApp();
        }
    }

    public void executeJenkinsBatchJob(String command) throws Exception {
        HashMap<String, String> jenkinsParams = new HashMap<>();
        jenkinsParams.put(JenkinsParameterKey.NODE.toString(), String.format("api&&%s&&olcs", Properties.get("env", true)));
        jenkinsParams.put(JenkinsParameterKey.JOB.toString(), command);

        Jenkins.trigger(Jenkins.Job.BATCH_PROCESS_QUEQUE, jenkinsParams);
    }

    public boolean retryingFindClick(By by) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 10) {
            try {
                Browser.navigate().findElement(by).click();
                result = true;
                break;
            } catch (Exception e) {
            }
            attempts++;
        }
        return result;
    }

    public String stripNonAlphanumericCharacters(String value) {
        return value.replaceAll("[^A-Za-z0-9]", "");

    }

    public String stripAlphaCharacters(String value) {
        return value.replaceAll("[^0-9]", "");
    }

    public void selectAllExternalRadioButtons(String radioButtonValue) throws IllegalBrowserException {
        List<WebElement> radioButtons = Browser.navigate().findElements(By.xpath("//label[@class='form-control form-control--radio form-control--inline']"));
        radioButtons.stream().filter(s -> s.getText().equals(radioButtonValue)).forEach(x -> x.click());
    }
    public void findAllRadioButtons(String value) throws IllegalBrowserException {
        List<WebElement> radioButtons = Browser.navigate().findElements(By.xpath("//*[@type='radio']"));
        radioButtons.stream().filter(x -> x.getAttribute("value").equals(value)).forEach(x -> x.click());
    }
    public void selectFirstValueInList(String selector) throws IllegalBrowserException {
        Browser.navigate().findElements(By.xpath(selector)).stream().findFirst().get().click();
    }

    public boolean checkForValuesInTable(String searchTerm) throws IllegalBrowserException {
        return Browser.navigate().findElements(By.xpath("//table/tbody/tr[*]")).stream().allMatch(w -> w.getText().contains(searchTerm));
    }
}