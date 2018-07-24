package org.dvsa.testing.framework.Utils.Generic;

import activesupport.IllegalBrowserException;
import activesupport.MissingDriverException;
import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.S3;
import activesupport.driver.Browser;
import activesupport.http.RestUtils;
import activesupport.jenkins.Jenkins;
import activesupport.jenkins.JenkinsParameterKey;
import activesupport.string.Str;
import activesupport.system.Properties;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.dvsa.testing.framework.Utils.API_Builders.GenericBuilder;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.CreateLicenceAPI;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.GrantLicenceAPI;
import org.dvsa.testing.framework.Utils.API_Headers.Headers;
import org.dvsa.testing.framework.stepdefs.World;
import org.dvsa.testing.lib.Login;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.internal.SearchNavBar;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
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

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.dvsa.testing.framework.Utils.API_Headers.Headers.getHeaders;

public class GenericUtils extends BasePage {
   private static Browser browser = new Browser();
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
    private static final String DA_USER = "usr271";
    private static final String DA_PASSWORD = "password";
    private static final String USER = "usr336";
    private static final String USER_PASSWORD = "Password1";
    private static final String zipFilePath = "/src/test/resources/ESBR.zip";
    private String trafficAreaName;
    private static String variationApplicationNumber;
    private static String operatorType = System.getProperty("operatorType");

    public static int tmCount;

    private String getRegistrationNumber() {
        return registrationNumber;
    }

    private String getTrafficAreaName() {
        return trafficAreaName;
    }

    private void setTrafficAreaName(String trafficAreaName) {
        this.trafficAreaName = trafficAreaName;
    }

    private void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public GenericUtils(World world) throws MissingRequiredArgument {
        this.world = world;
        world.createLicence = new CreateLicenceAPI();
        world.grantLicence = new GrantLicenceAPI(world);
    }

    public static void generateLetter() {
        clickByLinkText("Docs & attachments");
        isTextPresent("1 Docs & attachments", 60);
        clickByName("New letter");
        findElement("//*[@id='modal-title']", SelectorType.XPATH, 600);


        waitAndSelectByIndex("Generate letter", "//*[@id='category']", SelectorType.XPATH, 1);
        waitAndSelectByIndex("Generate letter", "//*[@id='documentSubCategory']", SelectorType.XPATH, 1);
        waitAndSelectByIndex("Generate letter", "//*[@id='documentTemplate']", SelectorType.XPATH, 5);
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
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

    public static void internalUserLogin() throws MissingRequiredArgument, MalformedURLException, MissingDriverException, IllegalBrowserException {
        String url = URL.build(ApplicationType.INTERNAL, env).toString();

        if (Browser.isInitialised()) {
            //Quit Browser and open a new window
            browser.quit();
        }
        browser.navigate(url);

        if (url.contains("da")) {
            Login.signIn(DA_USER, DA_PASSWORD);
        } else {
            Login.signIn(USER, USER_PASSWORD);
        }
    }

    public void externalUserLogin() throws MalformedURLException, MissingRequiredArgument {
        String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();

        if (Browser.isInitialised()) {
            //Quit Browser and open a new window
            Browser.quit();
        }
        Browser.go(myURL);
        String password = S3.getTempPassword(world.createLicence.getEmailAddress());

        if (isTextPresent("Username", 60))
            Login.signIn(world.createLicence.getLoginId(), password);
        if (isTextPresent("Current password", 60)) {
            enterField(nameAttribute("input", "oldPassword"), password);
            enterField(nameAttribute("input", "newPassword"), "Password1");
            enterField(nameAttribute("input", "confirmPassword"), "Password1");
            click(nameAttribute("input", "submit"));
        }

    }

    public void nIAddressBuilder() {
        world.createLicence.setEnforcementArea("EA-N");
        world.createLicence.setTrafficArea("N");
        world.createLicence.setTown("Belfast");
        world.createLicence.setPostcode("BT28HQ");
        world.createLicence.setCountryCode("NI");
        world.createLicence.setNiFlag("Y");
    }

    public void getLicenceTrafficArea() throws MalformedURLException {
        Headers.headers.put("x-pid", world.createLicence.getAdminUserHeader());
        String getApplicationResource = org.dvsa.testing.lib.url.api.URL.build(env, String.format("licence/%s", world.createLicence.getLicenceId())).toString();

        apiResponse = RestUtils.get(getApplicationResource, getHeaders());
        setTrafficAreaName(apiResponse.extract().jsonPath().getString("trafficArea.name"));
    }

    public static void enterDate(int day, int month, int year) {
        enterText("receivedDate_day", String.valueOf(day), SelectorType.ID);
        enterText("receivedDate_month", String.valueOf(month), SelectorType.ID);
        enterText("receivedDate_year", String.valueOf(year), SelectorType.ID);
    }

    public static void internalSiteAddBusNewReg(int day, int month, int year) {
        waitForTextToBePresent("Service details");
        assertTrue(isTextPresent("Service No. & type", 5));
        enterText("serviceNo", "123", SelectorType.ID);
        enterText("startPoint", Str.randomWord(9), SelectorType.ID);
        enterText("finishPoint", Str.randomWord(11), SelectorType.ID);
        enterText("via", Str.randomWord(5), SelectorType.ID);
        selectServiceType("//ul[@class='chosen-choices']", "//*[@id=\"busServiceTypes_chosen\"]/div/ul/li[1]", SelectorType.XPATH);
        enterDate(getCurrentDayOfMonth(), getCurrentMonth(), getCurrentYear());
        enterText("effectiveDate_day", String.valueOf(day), SelectorType.ID);
        enterText("effectiveDate_month", String.valueOf(month), SelectorType.ID);
        enterText("effectiveDate_year", String.valueOf(year), SelectorType.ID);
        click(nameAttribute("button", "form-actions[submit]"));
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

    public void uploadAndSubmitESBR(String state, int interval) throws MissingRequiredArgument, MalformedURLException {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        modifyXML(state, interval);
        zipFolder();
        externalUserLogin();
        clickByLinkText("Bus");
        waitAndClick("//*[@id='main']/div[2]/ul/li[2]/a", SelectorType.XPATH);
        click(nameAttribute("button", "action"));
        String workingDir = System.getProperty("user.dir");
        uploadFile("//*[@id='fields[files][file]']", workingDir + zipFilePath, "document.getElementById('fields[files][file]').style.left = 0", SelectorType.XPATH);
        waitAndClick("//*[@name='form-actions[submit]']", SelectorType.XPATH);
    }

    public void removeInternalTransportManager() {
        assertTrue(isTextPresent("Overview", 60));
        if (!isLinkPresent("Transport", 60) && isTextPresent("Granted", 60)) {
            clickByLinkText(world.createLicence.getLicenceNumber());
            tmCount = returnTableRows("//*[@id='lva-transport-managers']/fieldset/div/div[2]/table/tbody/tr", SelectorType.XPATH);
        }
        clickByLinkText("Transport");
        isTextPresent("TransPort Managers", 60);
        click("//*[@value='Remove']", SelectorType.XPATH);
    }

    public void updateLicenceStatus(String licenceId, String status) throws MalformedURLException {
        String typeOfLicenceResource = org.dvsa.testing.lib.url.api.URL.build(env, String.format("licence/%s/decisions/%s", licenceId, status)).toString();

        GenericBuilder genericBuilder = new GenericBuilder().withId(licenceId);
        apiResponse = RestUtils.post(genericBuilder, typeOfLicenceResource, getHeaders());
        assertThat(apiResponse.statusCode(HttpStatus.SC_CREATED));
    }

    public void searchAndViewApplication() {
        selectValueFromDropDown("//select[@id='search-select']", SelectorType.XPATH, "Applications");
        if (variationApplicationNumber != null) {
            do {
                SearchNavBar.search(variationApplicationNumber);
            } while (!isLinkPresent(variationApplicationNumber, 60));
            clickByLinkText(variationApplicationNumber);
            assertTrue(Boolean.parseBoolean(String.valueOf(Browser.getURL().contains("variation"))));
        } else {
            do {
                SearchNavBar.search(String.valueOf(world.createLicence.getApplicationNumber()));
            } while (!isLinkPresent(world.createLicence.getApplicationNumber(), 200));
            clickByLinkText(world.createLicence.getApplicationNumber());
            if (isLinkPresent("Interim", 60))
                clickByLinkText("Interim ");
        }
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

    public void createAdminFee(String amount, String feeType) {
        waitAndClick("//button[@id='new']", SelectorType.XPATH);
        waitForTextToBePresent("Create new fee");
        selectValueFromDropDown("fee-details[feeType]", SelectorType.NAME, feeType);
        waitAndEnterText("amount", SelectorType.ID, amount);
        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void payFee(String amount, @NotNull String paymentMethod, String bankCardNumber, String cardExpiryMonth, String cardExpiryYear) {

        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH));
        waitAndClick("//tbody/tr[2]/td[7]", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Pay fee");

        if (paymentMethod.toLowerCase().trim().equals("cash") || paymentMethod.toLowerCase().trim().equals("cheque") || paymentMethod.toLowerCase().trim().equals("postal")) {
            enterText("details[received]", amount, SelectorType.NAME);
            enterText("details[payer]", "Automation payer", SelectorType.NAME);
            enterText("details[slipNo]", "1234567", SelectorType.NAME);
            enterText("details[customerName]", "Jane Doe", SelectorType.NAME);
        }
        if (paymentMethod.toLowerCase().trim().equals("card") && (isTextPresent("Pay fee", 10))) {
            selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Card Payment");
            enterText("details[customerName]", "Veena Skish", SelectorType.NAME);
            enterText("details[customerReference]", "AutomationCardCustomerRef", SelectorType.NAME);
            findAddress();
        }
        switch (paymentMethod.toLowerCase().trim()) {
            case "cash":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cash");
                enterText("details[customerReference]", "AutomationCashCustomerRef", SelectorType.NAME);
                findAddress();
                break;
            case "cheque":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cheque");
                enterText("details[chequeNo]", "12345", SelectorType.NAME);
                enterText("details[customerReference]", "AutomationChequeCustomerRef", SelectorType.NAME);
                enterText("details[chequeDate][day]", String.valueOf(getCurrentDayOfMonth()), SelectorType.NAME);
                enterText("details[chequeDate][month]", String.valueOf(getCurrentMonth()), SelectorType.NAME);
                enterText("details[chequeDate][year]", String.valueOf(getCurrentYear()), SelectorType.NAME);
                findAddress();
                break;
            case "postal":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Postal Order");
                enterText("details[customerReference]", "AutomationPostalOrderCustomerRef", SelectorType.NAME);
                enterText("details[poNo]", "123456", SelectorType.NAME);
                findAddress();
                break;
            case "card":
                customerPaymentModule(bankCardNumber, cardExpiryMonth, cardExpiryYear);
                break;
        }
    }

    public void customerPaymentModule(String bankCardNumber, String cardExpiryMonth, String cardExpiryYear) {
        waitForTextToBePresent("Card Number*");
        enterText("//*[@id='scp_cardPage_cardNumber_input']", bankCardNumber, SelectorType.XPATH);
        enterText("//*[@id='scp_cardPage_expiryDate_input']", cardExpiryMonth, SelectorType.XPATH);
        enterText("//*[@id='scp_cardPage_expiryDate_input2']", cardExpiryYear, SelectorType.XPATH);
        enterText("//*[@id='scp_cardPage_csc_input']", "123", SelectorType.XPATH);
        click("//*[@id='scp_cardPage_buttonsNoBack_continue_button']", SelectorType.XPATH);
        enterText("//*[@id='scp_additionalInformationPage_cardholderName_input']", "Mr Regression Test", SelectorType.XPATH);
        click("//*[@id='scp_additionalInformationPage_buttons_continue_button']", SelectorType.XPATH);
        waitForTextToBePresent("Online Payments");
        click("//*[@id='scp_confirmationPage_buttons_payment_button']", SelectorType.XPATH);
        if (isElementPresent("//*[@id='scp_storeCardConfirmationPage_buttons_back_button']", SelectorType.XPATH)){
            waitForTextToBePresent("Online Payments");
            click("//*[@id='scp_storeCardConfirmationPage_buttons_back_button']", SelectorType.XPATH);
            waitForTextToBePresent("Payment successful");
            clickByLinkText("Back");
            waitForTextToBePresent("There are currently no outstanding fees to pay");
        }
    }

    public void findAddress() {
        enterText("address[searchPostcode][postcode]", "NG1 5FW", SelectorType.NAME);
        waitAndClick("address[searchPostcode][search]", SelectorType.NAME);
        waitAndSelectByIndex("", "//*[@id='fee_payment']/fieldset[2]/fieldset/div[3]/select[@name='address[searchPostcode][addresses]']", SelectorType.XPATH, 1);
        do {
            retryingFindClick(By.xpath("//*[@id='form-actions[pay]']"));
        } while (getAttribute("//*[@name='address[addressLine1]']", SelectorType.XPATH, "value").isEmpty());
    }

    public boolean retryingFindClick(By by) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 10) {
            try {
                Browser.getDriver().findElement(by).click();
                result = true;
                break;
            } catch (Exception e) {
            }
            attempts++;
        }
        return result;
    }

    public String stripNonAlphanumericCharacters(String value){
        return value.replaceAll("[^A-Za-z0-9]", "");

    }
    public String stripAlphaCharacters(String value){
        return value.replaceAll("[^0-9]", "");
    }
}