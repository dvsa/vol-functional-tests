package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.MissingDriverException;
import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.S3;
import activesupport.driver.Browser;
import activesupport.string.Str;
import activesupport.system.Properties;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.stepdefs.PaymentProcessing;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.exception.ElementDidNotAppearWithinSpecifiedTimeException;
import org.dvsa.testing.lib.pages.internal.SearchNavBar;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getFutureDate;


public class UIJourneySteps extends BasePage {

    private World world;
    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    private String localUser = Properties.get("localUser", false);
    private String localDefaultPassword = Properties.get("localDefaultPassword", false);
    static int tmCount;
    private static final String zipFilePath = "/src/test/resources/ESBR.zip";
    private String verifyUsername;
    private String operatorUser;
    private String operatorUserEmail;
    private String operatorForeName;
    private String operatorFamilyName;
    private String externalTMUser;
    private String externalTMEmail;
    private String password;
    private String licenceNumber;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyUsername() {
        return verifyUsername;
    }

    private void setVerifyUsername(String verifyUsername) {
        this.verifyUsername = verifyUsername;
    }

    public UIJourneySteps(World world) {
        this.world = world;
    }

    public String getOperatorUser() {
        return operatorUser;
    }

    public void setOperatorUser(String operatorUser) {
        this.operatorUser = operatorUser;
    }

    public String getOperatorUserEmail() {
        return operatorUserEmail;
    }

    public void setOperatorUserEmail(String operatorUserEmail) {
        this.operatorUserEmail = operatorUserEmail;
    }

    public String getOperatorForeName() {
        return operatorForeName;
    }

    public void setOperatorForeName(String operatorForeName) {
        this.operatorForeName = operatorForeName;
    }

    public String getOperatorFamilyName() {
        return operatorFamilyName;
    }

    public void setOperatorFamilyName(String operatorFamilyName) {
        this.operatorFamilyName = operatorFamilyName;
    }


    public String getExternalTMUser() {
        return externalTMUser;
    }

    public void setExternalTMUser(String externalTMUser) {
        this.externalTMUser = externalTMUser;
    }

    public String getExternalTMEmail() {
        return externalTMEmail;
    }

    public void setExternalTMEmail(String externalTMEmail) {
        this.externalTMEmail = externalTMEmail;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public void internalSearchForBusReg() throws IllegalBrowserException {
        selectValueFromDropDown("//*[@id='search-select']", SelectorType.XPATH, "Bus registrations");
        do {
            SearchNavBar.search(world.createLicence.getLicenceNumber());
        } while (!isLinkPresent(world.createLicence.getLicenceNumber(), 60));
        clickByLinkText(world.createLicence.getLicenceNumber());
    }

    public void internalSiteAddBusNewReg(int month) throws IllegalBrowserException {
        waitForTextToBePresent("Overview");
        clickByLinkText("Bus registrations");
        click(nameAttribute("button", "action"));
        waitForTextToBePresent("Service details");
        assertTrue(isTextPresent("Service No. & type", 5));
        enterText("serviceNo", "123", SelectorType.ID);
        enterText("startPoint", Str.randomWord(9), SelectorType.ID);
        enterText("finishPoint", Str.randomWord(11), SelectorType.ID);
        enterText("via", Str.randomWord(5), SelectorType.ID);
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        clickFirstElementFound("//*[@class=\"active-result\"]", SelectorType.XPATH);
        enterDate(getCurrentDayOfMonth(), getCurrentMonth(), getCurrentYear());
        getFutureDate(month);
        String[] date = getFutureDate(5).toString().split("-");
        enterText("effectiveDate_day", date[2], SelectorType.ID);
        enterText("effectiveDate_month", date[1], SelectorType.ID);
        enterText("effectiveDate_year", date[0], SelectorType.ID);
        click(nameAttribute("button", "form-actions[submit]"));
        do {
            // Refresh page
            javaScriptExecutor("location.reload(true)");
        }
        while (!isTextPresent("Service details", 2));//condition
    }

    private static void enterDate(int day, int month, int year) throws IllegalBrowserException {
        enterText("receivedDate_day", String.valueOf(day), SelectorType.ID);
        enterText("receivedDate_month", String.valueOf(month), SelectorType.ID);
        enterText("receivedDate_year", String.valueOf(year), SelectorType.ID);
    }

    public void viewESBRInExternal() throws IllegalBrowserException {
        do {
            // Refresh page
            javaScriptExecutor("location.reload(true)");
        } while (isTextPresent("processing", 60));
    }

    public void uploadAndSubmitESBR(String state, int interval) throws MissingRequiredArgument, IllegalBrowserException, MalformedURLException {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        world.genericUtils.modifyXML(state, interval);
        GenericUtils.zipFolder();
        navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
        clickByLinkText("Bus");
        waitAndClick("//*[@id='main']/div[2]/ul/li[2]/a", SelectorType.XPATH);
        click(nameAttribute("button", "action"));
        String workingDir = System.getProperty("user.dir");
        uploadFile("//*[@id='fields[files][file]']", workingDir + zipFilePath, "document.getElementById('fields[files][file]').style.left = 0", SelectorType.XPATH);
        waitAndClick("//*[@name='form-actions[submit]']", SelectorType.XPATH);
    }

    public void searchAndViewApplication() throws IllegalBrowserException, MalformedURLException {
        selectValueFromDropDown("//select[@id='search-select']", SelectorType.XPATH, "Applications");

        String variationApplicationNumber = world.updateLicence.getVariationApplicationNumber();
        if (variationApplicationNumber != null) {
            do {
                SearchNavBar.search(variationApplicationNumber);
            } while (!isLinkPresent(variationApplicationNumber, 60));
            clickByLinkText(variationApplicationNumber);
            assertTrue(Boolean.parseBoolean(String.valueOf(Browser.navigate().getCurrentUrl().contains("variation"))));
        } else {
            do {
                SearchNavBar.search(String.valueOf(world.createLicence.getApplicationNumber()));
            } while (!isLinkPresent(world.createLicence.getApplicationNumber(), 200));
            clickByLinkText(world.createLicence.getApplicationNumber());
            if (isLinkPresent("Interim", 60))
                clickByLinkText("Interim ");
        }
    }

    public void searchAndViewLicence() throws IllegalBrowserException, MalformedURLException {
        selectValueFromDropDown("//select[@id='search-select']", SelectorType.XPATH, "Licence");
        do {
            SearchNavBar.search(String.valueOf(world.createLicence.getLicenceNumber()));
        } while (!isLinkPresent(world.createLicence.getLicenceNumber(), 200));
        clickByLinkText(world.createLicence.getLicenceNumber());
    }

    public void urlSearchAndViewApplication() throws IllegalBrowserException, MalformedURLException {
        Browser.navigate().get(String.format("https://iuap1.olcs.qa.nonprod.dvsa.aws/application/%s",world.createLicence.getApplicationNumber()));
    }

    public void urlSearchAndViewLicence() throws IllegalBrowserException, MalformedURLException {
        Browser.navigate().get(String.format("https://iuap1.olcs.qa.nonprod.dvsa.aws/licence/%s",world.createLicence.getLicenceId()));
    }

    public void createAdminFee(String amount, String feeType) throws IllegalBrowserException {
        waitAndClick("//button[@id='new']", SelectorType.XPATH);
        waitForTextToBePresent("Create new fee");
        selectValueFromDropDown("fee-details[feeType]", SelectorType.NAME, feeType);
        waitAndEnterText("amount", SelectorType.ID, amount);
        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void payFee(String amount, @NotNull String paymentMethod, String bankCardNumber, String cardExpiryMonth, String cardExpiryYear) throws IllegalBrowserException {
        if (paymentMethod.toLowerCase().trim().equals("cash") || paymentMethod.toLowerCase().trim().equals("cheque") || paymentMethod.toLowerCase().trim().equals("postal")) {
            enterText("details[received]", amount, SelectorType.NAME);
            enterText("details[payer]", "Automation payer", SelectorType.NAME);
            enterText("details[slipNo]", "1234567", SelectorType.NAME);

        }
        if (paymentMethod.toLowerCase().trim().equals("card") && (isTextPresent("Pay fee", 10))) {
            selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Card Payment");
            if (isTextPresent("Customer reference", 10)) {
                enterText("details[customerName]", "Veena Skish", SelectorType.NAME);
                enterText("details[customerReference]", "AutomationCardCustomerRef", SelectorType.NAME);
                findAddress();
            }
        }
        switch (paymentMethod.toLowerCase().trim()) {
            case "cash":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cash");
                if (isTextPresent("Customer reference", 10)) {
                    enterText("details[customerName]", "Jane Doe", SelectorType.NAME);
                    enterText("details[customerReference]", "AutomationCashCustomerRef", SelectorType.NAME);
                    findAddress();
                } else {
                    clickByName("form-actions[pay]");
                }
                break;
            case "cheque":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cheque");
                if (isTextPresent("Customer reference", 10)) {
                    enterText("details[customerReference]", "AutomationChequeCustomerRef", SelectorType.NAME);
                }
                enterText("details[chequeNo]", "12345", SelectorType.NAME);
                enterText("details[customerName]", "Jane Doe", SelectorType.NAME);
                enterText("details[chequeDate][day]", String.valueOf(getCurrentDayOfMonth()), SelectorType.NAME);
                enterText("details[chequeDate][month]", String.valueOf(getCurrentMonth()), SelectorType.NAME);
                enterText("details[chequeDate][year]", String.valueOf(getCurrentYear()), SelectorType.NAME);
                findAddress();
                break;
            case "postal":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Postal Order");
                if (isTextPresent("Payer name", 10)) {
                    enterText("details[payer]", "Jane Doe", SelectorType.NAME);
                }
                enterText("details[customerReference]", "AutomationPostalOrderCustomerRef", SelectorType.NAME);
                enterText("details[customerName]", "Jane Doe", SelectorType.NAME);
                enterText("details[poNo]", "123456", SelectorType.NAME);
                findAddress();
                break;
            case "card":
                customerPaymentModule(bankCardNumber, cardExpiryMonth, cardExpiryYear);
                break;
        }
    }

    public void selectFeeById(String feeNumber) throws IllegalBrowserException {
        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH));
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        waitForTextToBePresent("Outstanding");
        waitAndClick("//*[@value='" + feeNumber + "']", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Pay fee");
    }

    public void selectFee() throws IllegalBrowserException {
        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH));
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        isElementEnabled("//tbody",SelectorType.XPATH);
        waitAndClick("//tbody/tr/td[7]/input", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Pay fee");
    }

    private void customerPaymentModule(String bankCardNumber, String cardExpiryMonth, String cardExpiryYear) throws IllegalBrowserException {
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
        if (isElementPresent("//*[@id='scp_storeCardConfirmationPage_buttons_back_button']", SelectorType.XPATH)) {
            waitForTextToBePresent("Online Payments");
            click("//*[@value='Save']", SelectorType.XPATH);
        }
    }


    private void findAddress() throws IllegalBrowserException {
        enterText("address[searchPostcode][postcode]", "NG1 5FW", SelectorType.NAME);
        waitAndClick("address[searchPostcode][search]", SelectorType.NAME);
        waitAndSelectByIndex("", "//*[@id='fee_payment']/fieldset[2]/fieldset/div[3]/select[@name='address[searchPostcode][addresses]']", SelectorType.XPATH, 1);
        do {
            retryingFindClick(By.xpath("//*[@id='form-actions[pay]']"));
        } while (getAttribute("//*[@name='address[addressLine1]']", SelectorType.XPATH, "value").isEmpty());
    }

    public void addPerson(String firstName, String lastName) throws IllegalBrowserException {
        clickByName("add");
        waitForTextToBePresent("Add a director");
        selectValueFromDropDown("//select[@id='title']", SelectorType.XPATH, "Dr");
        enterText("forename", firstName, SelectorType.ID);
        enterText("familyname", lastName, SelectorType.ID);
        enterText("dob_day", String.valueOf(getPastDayOfMonth(5)), SelectorType.ID);
        enterText("dob_month", String.valueOf(getCurrentMonth()), SelectorType.ID);
        enterText("dob_year", String.valueOf(getPastYear(20)), SelectorType.ID);
        clickByName("form-actions[saveAndContinue]");
    }

    public void navigateToDirectorsPage() throws IllegalBrowserException {
        waitForTextToBePresent("Current licences");
        clickByLinkText(world.createLicence.getLicenceNumber());
        waitForTextToBePresent("View your licence");
        clickByLinkText("Directors");
        waitForTextToBePresent("Directors");
    }

    public void navigateToInternalTask() throws IllegalBrowserException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        world.UIJourneySteps.searchAndViewApplication();
        waitForTextToBePresent("Processing");
        clickByLinkText("Processing");
        isElementEnabled("//body", SelectorType.XPATH);
    }

    public void addPreviousConviction() throws IllegalBrowserException {
        selectValueFromDropDown("data[title]", SelectorType.ID, "Ms");
        enterText("data[forename]", Str.randomWord(8), SelectorType.NAME);
        enterText("data[familyName]", Str.randomWord(8), SelectorType.NAME);
        enterText("data[notes]", Str.randomWord(30), SelectorType.NAME);
        enterText("dob_day", String.valueOf(getPastDayOfMonth(5)), SelectorType.ID);
        enterText("dob_month", String.valueOf(getCurrentMonth()), SelectorType.ID);
        enterText("dob_year", String.valueOf(getPastYear(20)), SelectorType.ID);
        enterText("data[categoryText]", Str.randomWord(50), SelectorType.NAME);
        enterText("data[courtFpn]", "Clown", SelectorType.NAME);
        enterText("data[penalty]", "Severe", SelectorType.NAME);
        clickByName("form-actions[submit]");
    }

    public void navigateToInternalAdminUserLogin(String username, String emailAddress) throws MissingRequiredArgument, IllegalBrowserException, MalformedURLException {
        String newPassword = "Password1";
        String myURL = URL.build(ApplicationType.INTERNAL, env).toString();

        if (Browser.isBrowserOpen()) {
            Browser.navigate().manage().deleteAllCookies();
        }
        Browser.navigate().get(myURL);
        String password = S3.getTempPassword(emailAddress, getBucketName());

        try {
            signIn(username, password);
        } catch (Exception e) {
            //User is already registered
            signIn(username, getPassword());
        } finally {
            if (isTextPresent("Current password", 60)) {
                enterField(nameAttribute("input", "oldPassword"), password);
                enterField(nameAttribute("input", "newPassword"), newPassword);
                enterField(nameAttribute("input", "confirmPassword"), newPassword);
                click(nameAttribute("input", "submit"));
                setPassword(newPassword);
            }
        }
    }

    private String getTempPassword(String emailAddress) {
        if (env == EnvironmentType.LOCAL) {
            return localDefaultPassword;
        }
        return S3.getTempPassword(emailAddress, getBucketName());
    }

    private String getBucketName() {
        return "devapp-olcs-pri-olcs-autotest-s3";
    }

    public void navigateToExternalUserLogin(String username, String emailAddress) throws MissingRequiredArgument, IllegalBrowserException, MalformedURLException {
        String newPassword = "Password1";
        String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();

        if (Browser.isBrowserOpen()) {
            Browser.navigate().manage().deleteAllCookies();
        }
        Browser.navigate().get(myURL);
        String password = getTempPassword(emailAddress);

        try {
            signIn(username, password);
        } catch (Exception e) {
            //User is already registered
            signIn(username, getPassword());
        } finally {
            if (isTextPresent("Current password", 60)) {
                enterField(nameAttribute("input", "oldPassword"), password);
                enterField(nameAttribute("input", "newPassword"), newPassword);
                enterField(nameAttribute("input", "confirmPassword"), newPassword);
                click(nameAttribute("input", "submit"));
                setPassword(newPassword);
            }
        }
    }

    public void navigateToExternalSearch() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.EXTERNAL, env, "search/find-lorry-bus-operators/").toString();
        Browser.navigate().get(myURL);
    }

    public static void generateLetter() throws IllegalBrowserException {
        clickByLinkText("Docs & attachments");
        isTextPresent("1 Docs & attachments", 60);
        clickByName("New letter");
        findElement("//*[@id='modal-title']", SelectorType.XPATH, 600);
        waitAndSelectByIndex("Generate letter", "//*[@id='category']", SelectorType.XPATH, 1);
        waitAndSelectByIndex("Generate letter", "//*[@id='documentSubCategory']", SelectorType.XPATH, 1);
        waitAndSelectByIndex("Generate letter", "//*[@id='documentTemplate']", SelectorType.XPATH, 5);
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void removeInternalTransportManager() throws IllegalBrowserException {
        assertTrue(isTextPresent("Overview", 60));
        if (!isLinkPresent("Transport", 60) && isTextPresent("Granted", 60)) {
            clickByLinkText(world.createLicence.getLicenceNumber());
            tmCount = returnTableRows("//*[@id='lva-transport-managers']/fieldset/div/div[2]/table/tbody/tr", SelectorType.XPATH);
        }
        clickByLinkText("Transport");
        isTextPresent("TransPort Managers", 60);
        click("//*[@value='Remove']", SelectorType.XPATH);
    }

    public void addDirectorWithoutConvictions(String firstName, String lastName) throws MissingDriverException, IllegalBrowserException, MalformedURLException {
        world.UIJourneySteps.navigateToDirectorsPage();
        world.UIJourneySteps.addPerson(firstName, lastName);
        findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
        findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
    }

    public void changeVehicleReq(String noOfVehicles) throws IllegalBrowserException {
        clickByLinkText("Operating centres and authorisation");
        clickByLinkText("change your licence");
        waitAndClick("button[name='form-actions[submit]'", SelectorType.CSS);
        waitAndClick("//*[@id=\"OperatingCentres\"]/fieldset[1]/div/div[2]/table/tbody/tr/td[1]/input", SelectorType.XPATH);
        enterField(nameAttribute("input", "data[noOfVehiclesRequired]"), noOfVehicles);
        if (Integer.parseInt(noOfVehicles) > world.createLicence.getNoOfVehiclesRequired()) {
            click(nameAttribute("button", "form-actions[submit]"));
        }
        click(nameAttribute("button", "form-actions[submit]"));
    }

    public void changeVehicleAuth(String noOfAuthVehicles) throws IllegalBrowserException {
        enterField(nameAttribute("input", "data[totAuthVehicles]"), noOfAuthVehicles);
        click(nameAttribute("button", "form-actions[save]"));
    }

    public void signWithVerify(String verifyUsername, String verifyPassword) throws IllegalBrowserException {
        setVerifyUsername(verifyUsername);
        waitForTextToBePresent("Sign in with GOV.UK Verify");
        click("//*[@id='start_form_selection_false']", SelectorType.XPATH);
        click("//*[@id='next-button']", SelectorType.XPATH);
        click("//*[contains(text(),'Select Post')]", SelectorType.XPATH);
        waitForTextToBePresent("Verified");
        enterText("username", verifyUsername, SelectorType.NAME);
        enterText("password", verifyPassword, SelectorType.NAME);
        click("//*[@id='login']", SelectorType.XPATH);
        waitForTextToBePresent("Personal Details");
        click("//*[@id='agree']", SelectorType.XPATH);
    }

    public void addNewPersonAsTransportManager(String forename, String familyName, String emailAddress) throws IllegalBrowserException {
        this.externalTMEmail = emailAddress;
        externalTMUser = "TM".concat(Str.randomWord(3));
        waitForTextToBePresent("Transport Managers");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTextToBePresent("Add Transport Manager");
        waitAndClick("addUser", SelectorType.ID);
        enterText("forename", forename, SelectorType.ID);
        enterText("familyName", familyName, SelectorType.ID);
        String[] date = GenericUtils.getPastDate(25).toString().split("-");
        enterText("dob_day", date[2], SelectorType.ID);
        enterText("dob_month", date[1], SelectorType.ID);
        enterText("dob_year", date[0], SelectorType.ID);
        enterText("username", externalTMUser, SelectorType.ID);
        enterText("emailAddress", externalTMEmail, SelectorType.ID);
        enterText("emailConfirm", externalTMEmail, SelectorType.ID);
        waitAndClick("form-actions[continue]", SelectorType.ID);
    }

    public void addDirector(String forename, String familyName) throws IllegalBrowserException {
        addPerson(forename, familyName);
        world.genericUtils.findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
        world.genericUtils.findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
    }

    public void removeDirector() throws IllegalBrowserException {
        int sizeOfTable = size("//*/td[4]/input[@type='submit']", SelectorType.XPATH);
        click("//*/tr[" + sizeOfTable + "]/td[4]/input[@type='submit']", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void internalUserNavigateToDocsTable() throws IllegalBrowserException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        world.UIJourneySteps.searchAndViewApplication();
        clickByLinkText("Docs");
    }

    private static void signIn(@NotNull String emailAddress, @NotNull String password, int timeLimitInSeconds) throws IllegalBrowserException {
        LoginPage.email(emailAddress);
        LoginPage.password(password);
        LoginPage.submit();
        LoginPage.untilNotOnPage(timeLimitInSeconds);
    }

    public void addTransportManagerDetails() throws IllegalBrowserException, InterruptedException, MalformedURLException {
        //Add Personal Details
        String birthPlace = world.createLicence.getTown();
        String[] date = world.genericUtils.getPastDate(25).toString().split("-");
        enterText("dob_day", date[2], SelectorType.ID);
        enterText("dob_month", date[1], SelectorType.ID);
        enterText("dob_year", date[0], SelectorType.ID);
        enterText("birthPlace", birthPlace, SelectorType.ID);

        waitForElementToBeClickable("//*[contains(text(),'External')]",SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'External')]", SelectorType.XPATH);
        world.genericUtils.findSelectAllRadioButtonsByValue("Y");

        //Add Home Address
        addAddressDetails();


        //Hours Of Week
        waitForElementToBeClickable("//*[contains(@name,'responsibilities[hoursOfWeek]')]",SelectorType.XPATH);
        enterSameTextIntoMultipleFieldsPartialMatch("//*[contains(@name,'responsibilities[hoursOfWeek]')]",SelectorType.XPATH,"3");

        //Add Other Licences
        String role = "Transport Manager";
        waitAndClick("//*[contains(text(),'Add other licence')]", SelectorType.XPATH);
        javaScriptExecutor("location.reload(true)");
        waitAndEnterText("licNo", SelectorType.ID, "PB123456");
        selectValueFromDropDown("//*[@id='data[role]']",SelectorType.XPATH,role);
        enterText("//*[@id='operatingCentres']","Test", SelectorType.XPATH);
        enterText("//*[@id='hoursPerWeek']","1", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);

        //Add Other Employment
        waitForTextToBePresent("Add other employment");
        waitAndClick("//*[contains(text(),'Add other employment')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='tm-employer-name-details[employerName]']",SelectorType.XPATH,"test");
        String postCode = world.createLicence.getPostcode();
        enterText("postcodeInput1", postCode, SelectorType.ID);
        clickByName("address[searchPostcode][search]");
        waitAndClick("address[searchPostcode][addresses]", SelectorType.ID);
        selectValueFromDropDownByIndex("address[searchPostcode][addresses]", SelectorType.ID, 1);
        waitAndEnterText("//*[@id='tm-employment-details[position]']",SelectorType.XPATH,"test");
        waitAndEnterText("//*[@id='tm-employment-details[hoursPerWeek]']", SelectorType.XPATH, "Test");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);


        // Convictions
        waitForTextToBePresent("Add convictions and penalties");
        waitAndClick("//*[contains(text(),'Add convictions and penalties')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='conviction-date_day']",SelectorType.XPATH,"03");
        enterText("//*[@id='conviction-date_month']","03",SelectorType.XPATH);
        enterText("//*[@id='conviction-date_year']","2014",SelectorType.XPATH);
        enterText("//*[@id='category-text']","Test",SelectorType.XPATH);
        enterText("//*[@id='notes']","Test",SelectorType.XPATH);
        enterText("//*[@id='court-fpn']","Test",SelectorType.XPATH);
        enterText("//*[@id='penalty']","Test",SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);

        waitForTextToBePresent("Add licences");
        waitAndClick("//*[contains(text(),'Add licences')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='lic-no']",SelectorType.XPATH, "PD263849");
        waitAndEnterText("//*[@id='holderName']",SelectorType.XPATH, "PD263849");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addAddressDetails() throws IllegalBrowserException, InterruptedException {
        //Add Home Address
        String postCode = world.createLicence.getPostcode();
        enterText("postcodeInput1", postCode, SelectorType.ID);
        clickByName("homeAddress[searchPostcode][search]");
        waitAndClick("homeAddress[searchPostcode][addresses]", SelectorType.ID);
        selectValueFromDropDownByIndex("homeAddress[searchPostcode][addresses]", SelectorType.ID, 1);
        //Add Work Address
        waitAndEnterText("postcodeInput2", SelectorType.ID, postCode);
        waitAndClick("workAddress[searchPostcode][search]",SelectorType.ID);
        waitAndClick("workAddress[searchPostcode][addresses]", SelectorType.ID);
        selectValueFromDropDownByIndex("workAddress[searchPostcode][addresses]", SelectorType.ID, 1);
    }

    public void nominateOperatorUserAsTransportManager(int user) throws IllegalBrowserException, MalformedURLException {
        navigateToTransportManagersPage();
        if (isTextPresent("change your licence",5)) { // If a variational (for an already created licence)
            world.UIJourneySteps.changeLicenceOnTMPage();
        }
        click("//*[@name='table[action]']", SelectorType.XPATH);
        waitForTextToBePresent("Add Transport Manager");
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, user);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
        enterText("dob_day", String.valueOf(getPastDayOfMonth(5)), SelectorType.ID);
        enterText("dob_month", String.valueOf(getCurrentMonth()), SelectorType.ID);
        enterText("dob_year", String.valueOf(getPastYear(20)), SelectorType.ID);
        click("form-actions[send]", SelectorType.ID);
        waitForTextToBePresent("Transport Managers");
    }

    public void addOperatorAdminAsTransportManager(int user) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException {
        navigateToTransportManagersPage();
        click("//*[@name='table[action]']", SelectorType.XPATH);
        waitForTextToBePresent("Add Transport Manager");
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, user);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
    }

    public void navigateToTransportManagersPage() throws IllegalBrowserException {
        clickByLinkText("GOV.UK");
        try {
            clickByLinkText(world.createLicence.getLicenceNumber());
        } catch (Exception e){
            clickByLinkText(world.createLicence.getApplicationNumber());
        }
        clickByLinkText("Transport");
        waitForTextToBePresent("Transport Managers");
    }

    public void navigateToApplicationReviewDeclarationsPage() throws IllegalBrowserException {
        clickByLinkText(world.createLicence.getApplicationNumber());
        clickByLinkText("Review");
        waitForTextToBePresent("Review and declarations");
    }

    private static void signIn(@NotNull String emailAddress, @NotNull String password) throws IllegalBrowserException {
        int timeLimitInSeconds = 10;
        signIn(emailAddress, password, timeLimitInSeconds);
    }

    public void resettingExternalPassword() throws IllegalBrowserException, MalformedURLException {
        if (Browser.isBrowserOpen()) {
            Browser.navigate().manage().deleteAllCookies();
        }
        String env = System.getProperty("env");
        String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();
        Browser.navigate().get(myURL);
        clickByLinkText("Forgotten your password?");
    }

    public void updateTMDetailsAndNavigateToDeclarationsPage(String isOwner, String OtherLicence, String hasEmployment, String hasConvictions, String hasPreviousLicences) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException {
        String tmEmailAddress = "externalTM@vol.com";
        String hours = "8";
        findElement("//*[@value='" + OtherLicence + "'][@name='responsibilities[otherLicencesFieldset][hasOtherLicences]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + isOwner + "'][@name='responsibilities[isOwner]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + hasEmployment + "'][@name='otherEmployments[hasOtherEmployment]']", SelectorType.XPATH, 20).click();
        findElement("//*[@value='" + hasConvictions + "'][@name='previousHistory[hasConvictions]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + hasPreviousLicences + "'][@name='previousHistory[hasPreviousLicences]']", SelectorType.XPATH, 30).click();
        findElement("//*[@id='responsibilities']//*[contains(text(),'Internal')]", SelectorType.XPATH, 30).click();
        findElement("emailAddress", SelectorType.ID, 10).clear();
        if (findElement("emailAddress", SelectorType.ID, 10).getText().isEmpty()) {
            waitAndEnterText("emailAddress", SelectorType.ID, tmEmailAddress);
        }
        waitAndEnterText("birthPlace", SelectorType.ID, "Nottingham");
        waitAndEnterText("postcodeInput1", SelectorType.ID, "NG23HX");
        clickByName("homeAddress[searchPostcode][search]");
        untilElementPresent("//*[@id='homeAddress[searchPostcode][addresses]']", SelectorType.XPATH);
        selectValueFromDropDownByIndex("homeAddress[searchPostcode][addresses]", SelectorType.ID, 1);
        waitAndEnterText("postcodeInput2", SelectorType.ID, "NG23HX");
        waitAndClick("//*[@id='workAddress[searchPostcode][search]']", SelectorType.XPATH);
        untilElementPresent("//*[@id='workAddress[searchPostcode][addresses]']", SelectorType.XPATH);
        selectValueFromDropDownByIndex("workAddress[searchPostcode][addresses]", SelectorType.ID, 1);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursMon]", SelectorType.ID, hours);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursTue]", SelectorType.ID, hours);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursWed]", SelectorType.ID, hours);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursThu]", SelectorType.ID, hours);
        click("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("Check your answers");
        click("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("Declaration");
    }

    public void addOperatorUserAsTransportManager(int user, String isOwner) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException {
        world.UIJourneySteps.nominateOperatorUserAsTransportManager(user);
        world.UIJourneySteps.navigateToExternalUserLogin(world.UIJourneySteps.getOperatorUser(), world.UIJourneySteps.getOperatorUserEmail());
        clickByLinkText(world.createLicence.getApplicationNumber());
        waitForTextToBePresent("Transport Managers");
        clickByLinkText("Transport");
        clickByLinkText(world.UIJourneySteps.getOperatorForeName() + " " + world.UIJourneySteps.getOperatorFamilyName());
        updateTMDetailsAndNavigateToDeclarationsPage(isOwner, "N", "N", "N", "N");
    }

    public void submitTMApplicationAndNavigateToTMLandingPage() throws ElementDidNotAppearWithinSpecifiedTimeException, IllegalBrowserException {
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
        click("form-actions[submit]", SelectorType.ID);
        clickByLinkText("Back to Transport");
        waitForTextToBePresent("Transport Managers");
    }

    public void addInternalAdmin() throws IllegalBrowserException, MalformedURLException {
        operatorUser = Str.randomWord(3);
        operatorUserEmail = "operator".concat(Str.randomWord(2)).concat("@dvsa.com");
        operatorForeName = "OperatorUser";
        operatorFamilyName = "API";
        world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
        clickByLinkText("Manage");
        click("//*[@id='add']", SelectorType.XPATH);
        enterText("username", getOperatorUser(), SelectorType.ID);
        enterText("forename", getOperatorForeName(), SelectorType.ID);
        enterText("familyName", getOperatorFamilyName(), SelectorType.ID);
        enterText("main[emailAddress]", getOperatorUserEmail(), SelectorType.ID);
        enterText("main[emailConfirm]", getOperatorUserEmail(), SelectorType.ID);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void navigateToSurrendersStartPage() throws IllegalBrowserException, MalformedURLException {
        navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
        setLicenceNumber(Browser.navigate().findElements(By.xpath("//tr/td[1]")).stream().findFirst().get().getText());
        Browser.navigate().findElements(By.xpath("//tr/td[1]")).stream().findFirst().ifPresent(WebElement::click);
        waitForTextToBePresent("Summary");
        clickByLinkText("Apply to");
    }

    public void signDeclaration() throws IllegalBrowserException {
        waitAndClick("//*[contains(text(),'Sign your declaration online')]", SelectorType.XPATH);
        if (isTextPresent("Review and declarations", 10)) {
            click("//*[@name='form-actions[sign]']", SelectorType.XPATH);
        } else if (isTextPresent("Declaration", 10)) {
            click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
        }
    }

    public void navigateThroughApplication() throws IllegalBrowserException {
        clickByLinkText("Type");
        waitForTextToBePresent("Type of licence");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Business type");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Business details");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Addresses");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Directors");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Operating centres and authorisation");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Financial evidence");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Transport Managers");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Vehicle details");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        if (isTextPresent("Vehicle declarations", 30)) {
            waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        }
        waitForTextToBePresent("Safety and compliance");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Financial history");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Licence history");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitForTextToBePresent("Convictions and Penalties");
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void navigateToSurrenderReviewPage(String discToDestroy, String discsLost, String discsStolen) throws IllegalBrowserException, MalformedURLException {
        addDiscInformation(discToDestroy, discsLost, discsStolen);
        addOperatorLicenceDetails();
        if (world.createLicence.getLicenceType().equals("standard_international")) {
            addCommunityLicenceDetails();
        }
        assertTrue(Browser.navigate().getCurrentUrl().contains("review"));
        assertTrue(isTextPresent("Review your surrender", 40));
    }

    public void startSurrender() throws IllegalBrowserException {
        click("//*[@id='submit']", SelectorType.XPATH);
        waitForTextToBePresent("Review your contact information");
    }

    public void addDiscInformation(String discToDestroy, String discsLost, String discsStolen) throws IllegalBrowserException, MalformedURLException {
        assertTrue(Browser.navigate().getCurrentUrl().contains("current-discs"));
        click("//*[contains(text(),'In your possession')]", SelectorType.XPATH);
        waitForTextToBePresent("Number of discs you will destroy");
        waitAndEnterText("//*[@id='possessionSection[info][number]']", SelectorType.XPATH, discToDestroy);
        click("//*[contains(text(),'Lost')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='lostSection[info][number]']", SelectorType.XPATH, discsLost);
        waitAndEnterText("//*[@id='lostSection[info][details]']", SelectorType.XPATH, "lost");
        click("//*[contains(text(),'Stolen')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='stolenSection[info][number]']", SelectorType.XPATH, discsStolen);
        waitAndEnterText("//*[@id='stolenSection[info][details]']", SelectorType.XPATH, "stolen");
        waitAndClick("//*[@id='submit']", SelectorType.XPATH);
    }

    public void addOperatorLicenceDetails() throws IllegalBrowserException {
        click("//*[contains(text(),'Lost')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='operatorLicenceDocument[lostContent][details]']", SelectorType.XPATH, "lost in the washing");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addCommunityLicenceDetails() throws IllegalBrowserException {
        click("//*[contains(text(),'Stolen')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='communityLicenceDocument[stolenContent][details]']", SelectorType.XPATH, "Stolen on the way here");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void acknowledgeDestroyPage() throws IllegalBrowserException {
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Securely destroy");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Declaration");
    }

    public void updateContactDetails(String addressLine1, String addressLine2, String contactNumber) throws IllegalBrowserException {
        findElement("addressLine1", SelectorType.ID, 10).clear();
        enterText("addressLine1", addressLine1, SelectorType.ID);
        findElement("correspondence_address[addressLine2]", SelectorType.ID, 10).clear();
        enterText("correspondence_address[addressLine2]", addressLine2, SelectorType.ID);
        findElement("phone_primary", SelectorType.ID, 10).clear();
        enterText("phone_primary", contactNumber, SelectorType.ID);
        waitAndClick("form-actions[save]", SelectorType.ID);
    }

    public String getSurrenderAddressLine1() throws IllegalBrowserException {
        return getText("//*[@class='app-check-your-answers app-check-your-answers--long'][2]/div[@class='app-check-your-answers__contents'][1]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
    }

    public String getSurrenderTown() throws IllegalBrowserException {
        return getText("//*[@class='app-check-your-answers app-check-your-answers--long'][2]/div[@class='app-check-your-answers__contents'][2]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
    }

    public String getSurrenderContactNumber() throws IllegalBrowserException {
        return getText("//*[@class='app-check-your-answers app-check-your-answers--long'][3]/div[@class='app-check-your-answers__contents'][1]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
    }

    public void submitSurrender() throws MalformedURLException, IllegalBrowserException {
        world.UIJourneySteps.navigateToSurrendersStartPage();
        world.UIJourneySteps.startSurrender();
        waitAndClick("form-actions[submit]", SelectorType.ID);
        world.UIJourneySteps.addDiscInformation("2", "2", "1");
        waitForTextToBePresent("In your possession");
        world.UIJourneySteps.addOperatorLicenceDetails();
        if (world.createLicence.getLicenceType().equals("standard_international")) {
            assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
            world.UIJourneySteps.addCommunityLicenceDetails();
        }
        world.UIJourneySteps.acknowledgeDestroyPage();
        if (Browser.navigate().getCurrentUrl().contains("qa")) {
            waitAndClick("//*[@id='sign']", SelectorType.XPATH);
            world.UIJourneySteps.signWithVerify("pavlov", "Password1");
            waitForTextToBePresent("What happens next");
            Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
            Assert.assertTrue(isTextPresent(String.format("Application to surrender licence %s", world.createLicence.getLicenceNumber()), 10));
            Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy")), 20));
            assertTrue(isTextPresent("notifications@vehicle-operator-licensing.service.gov.uk", 10));
            waitAndClick("//*[contains(text(),'home')]", SelectorType.XPATH);
        } else {
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            world.UIJourneySteps.signManually();
        }
        assertEquals(getText("//*[@class='overview__status green']", SelectorType.XPATH), "SURRENDER UNDER CONSIDERATION");
    }

    public void caseworkManageSurrender() throws MalformedURLException, IllegalBrowserException {
        world.APIJourneySteps.createAdminUser();
        world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        world.UIJourneySteps.searchAndViewLicence();
        clickByLinkText("Surrender");
        waitForTextToBePresent("Surrender details");
        waitAndClick("//*[@for='checks[ecms]']",SelectorType.XPATH);
        // Refresh page
        javaScriptExecutor("location.reload(true)");
        waitAndClick("//*[contains(text(),'Digital signature')]", SelectorType.XPATH);
    }

    public void signManually() throws IllegalBrowserException, MalformedURLException {
        String defaultWindow = Browser.navigate().getWindowHandle();
        Set<String> windows;
        waitForTextToBePresent("A business owner");
        do {
            clickByLinkText("Print");
            windows = Browser.navigate().getWindowHandles();
        } while (windows.size() == 1);
        String printWindow = windows.stream().reduce((first, second) -> second).get();
        Browser.navigate().switchTo().window(printWindow).close();
        Browser.navigate().switchTo().window(defaultWindow);
        click("//*[contains(@title,'return to home')]", SelectorType.XPATH);
    }

    public void checkLicenceStatus(String arg0) throws IllegalBrowserException {
        do {
            System.out.println("Page not loaded yet");
        }
        while (!isTextPresent("Licence details", 2));//condition
        Assertions.assertEquals(getText("//*[contains(@class,'status')]", SelectorType.XPATH), arg0.toUpperCase());
    }

    public void removeDisc(String discDestroyed, String discLost, String discStolen) throws IllegalBrowserException, MalformedURLException, ElementDidNotAppearWithinSpecifiedTimeException {
        waitAndClick("form-actions[submit]", SelectorType.ID);
        world.UIJourneySteps.addDiscInformation(discDestroyed, discLost, discStolen);
        clickByLinkText("Home");
        clickByLinkText(world.createLicence.getLicenceNumber());
        clickByLinkText("Licence discs");
        waitAndClick("//*[@value='Remove']", SelectorType.XPATH);
        untilElementPresent("//*[@id='modal-title']", SelectorType.XPATH);
        waitAndClick("form-actions[submit]", SelectorType.NAME);
        javaScriptExecutor("location.reload(true)");
        waitForTextToBePresent("Disc number");
        clickByLinkText("Back");
    }

    public void addDisc() throws IllegalBrowserException {
        clickByLinkText("Home");
        clickByLinkText(world.createLicence.getLicenceNumber());
        clickByLinkText("Licence discs");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitAndEnterText("data[additionalDiscs]", SelectorType.ID, "2");
        waitAndClick("form-actions[submit]", SelectorType.NAME);
        world.updateLicence.printLicenceDiscs();
        clickByLinkText("Home");
        clickByLinkText(world.createLicence.getLicenceNumber());
    }

    public void createLicence(World world, String operatorType, String licenceType) {
        if (licenceType.equals("si")) {
            world.createLicence.setLicenceType("standard_international");
        } else if (licenceType.equals("sn")) {
            world.createLicence.setLicenceType("standard_national");
        } else {
            world.createLicence.setLicenceType("standard_national");
        }
        world.createLicence.setOperatorType(operatorType);
        world.APIJourneySteps.registerAndGetUserDetails();
        world.APIJourneySteps.createApplication();
        world.APIJourneySteps.submitApplication();
        if (String.valueOf(operatorType).equals("public")) {
            world.APIJourneySteps.grantLicenceAndPayFees();
            System.out.println("Licence: " + world.createLicence.getLicenceNumber());
        } else {
            world.APIJourneySteps.grantLicenceAndPayFees();
            System.out.println("Licence: " + world.createLicence.getLicenceNumber());
        }
    }

    public void closeCase() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("" + world.updateLicence.getCaseId() + "");
        do {
            System.out.println("waiting for page to load");
            javaScriptExecutor("location.reload(true)");
        } while (!Browser.navigate().getCurrentUrl().contains("case/details"));
        clickByLinkText("Close");
        waitForTextToBePresent("Close the case");
        click("form-actions[confirm]", SelectorType.ID);

    }

    public void closeBusReg() throws IllegalBrowserException {
        clickByLinkText("" + world.createLicence.getLicenceNumber() + "");
        click("menu-bus-registration-decisions-admin-cancel", SelectorType.ID);
        waitForTextToBePresent("Update status");
        enterText("fields[reason]", "Mistake", SelectorType.ID);
        click("form-actions[submit]", SelectorType.ID);
    }

    public void payFeesAndGrantNewBusReg() throws IllegalBrowserException {
        clickByLinkText("Fees");
        world.UIJourneySteps.selectFee();
        world.UIJourneySteps.payFee("60", "cash", null, null, null);
        do {
            System.out.println("link not present");
            javaScriptExecutor("location.reload(true)");
        } while (!isLinkPresent("Register service", 5));
        clickByLinkText("Register service");
        findSelectAllRadioButtonsByValue("Y");
        clickByName("form-actions[submit]");
        clickByLinkText("Service details");
        clickByLinkText("TA's");
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result\"]");
        click("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result group-option\"]");
        clickByName("form-actions[submit]");
        waitAndClick("//*[contains(text(),'Grant')]", SelectorType.XPATH);
    }

    public void createLicenceWithOpenCaseAndBusReg(String operatorType, String licenceType) throws IllegalBrowserException, MalformedURLException {
        if (licenceType.equals("si")) {
            world.createLicence.setLicenceType("standard_international");
        } else if (licenceType.equals("sn")) {
            world.createLicence.setLicenceType("standard_national");
        } else {
            world.createLicence.setLicenceType("standard_national");
        }
        world.createLicence.setTrafficArea("B");
        world.createLicence.setEnforcementArea("EA-B");
        world.createLicence.setOperatorType(operatorType);
        world.APIJourneySteps.registerAndGetUserDetails();
        world.APIJourneySteps.createApplication();
        world.APIJourneySteps.submitApplication();
        if (String.valueOf(operatorType).equals("public")) {
            world.APIJourneySteps.grantLicenceAndPayFees();
            System.out.println("Licence: " + world.createLicence.getLicenceNumber());
        } else {
            world.APIJourneySteps.grantLicenceAndPayFees();
            System.out.println("Licence: " + world.createLicence.getLicenceNumber());
        }
        world.APIJourneySteps.createAdminUser();
        world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        world.UIJourneySteps.searchAndViewLicence();
        world.UIJourneySteps.internalSiteAddBusNewReg(5);
        world.UIJourneySteps.payFeesAndGrantNewBusReg();
        world.updateLicence.createCase();
    }

    public void internalDigitalSurrenderMenu() throws IllegalBrowserException {
        do {
            System.out.println("waiting for page to load");
            javaScriptExecutor("location.reload(true)");
        } while (!isLinkPresent("" + world.createLicence.getLicenceNumber() + "", 10));
        clickByLinkText("" + world.createLicence.getLicenceNumber() + "");
        click("menu-licence_surrender", SelectorType.ID);
    }

    public void payForInterimApp() throws IllegalBrowserException {
        clickByLinkText("Financial");
        waitAndClick("//*[contains(text(),'Send')]", SelectorType.XPATH);
        waitAndClick("form-actions[save]", SelectorType.NAME);
        clickByLinkText("Review");
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        enterText("interim[goodsApplicationInterimReason]", "Testing", SelectorType.NAME);
        click("submitAndPay", SelectorType.ID);
        click("//*[@name='form-actions[pay]']", SelectorType.XPATH);
        world.UIJourneySteps.payFee(null, "card", "4006000000000600", "10", "20");
    }

    public void addNewOperatingCentre() throws IllegalBrowserException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        world.UIJourneySteps.searchAndViewLicence();
        clickByLinkText("Operating centres and authorisation");
        click("//*[@id='add']", SelectorType.XPATH);
        enterText("//*[@id='postcodeInput1']", "FK10 1AA", SelectorType.XPATH);
        click("//*[@id='address[searchPostcode][search]']", SelectorType.XPATH);
        waitForTextToBePresent("Please select");
        selectValueFromDropDownByIndex("address[searchPostcode][addresses]", SelectorType.ID, 1);
        waitForTextToBePresent("Total number of vehicles");
        assertTrue(isElementPresent("//*[@id='noOfVehiclesRequired']", SelectorType.XPATH));
        waitAndEnterText("noOfVehiclesRequired", SelectorType.ID, "1");
        findSelectAllRadioButtonsByValue("adPlaced");
        click("form-actions[submit]", SelectorType.ID);
    }

    public void caseWorkerCompleteConditionsAndUndertakings() throws IllegalBrowserException {
        clickByLinkText("Conditions and undertakings");
        click("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void caseWorkerCompleteReviewAndDeclarations() throws IllegalBrowserException {
        clickByLinkText("Review and declarations");
        waitAndClick("//*[@id='declarations[declarationConfirmation]']", SelectorType.XPATH);
        click("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void caseWorkerCompleteOverview() throws IllegalBrowserException, MalformedURLException {
        click("//*[@id='details[overrideOppositionDate]']", SelectorType.XPATH);
        Browser.navigate().findElements(By.xpath("//*[contains(@id,'tracking')]/option[2]")).stream().forEach(WebElement::click);
        click("//*[contains(@id,'save')]", SelectorType.XPATH);
    }

    public void caseWorkerGrantApplication() throws IllegalBrowserException {
        javaScriptExecutor("location.reload(true)");
        waitAndClick("//*[@id='menu-application-decisions-grant']", SelectorType.XPATH);
        waitAndClick("//*[@id='inspection-request-confirm[createInspectionRequest]']", SelectorType.XPATH);
        click("//*[@id='form-actions[grant]']", SelectorType.XPATH);
    }

    public void createPublicInquiry() throws IllegalBrowserException {
        click("//*[@id='menu-case_hearings_appeals']",SelectorType.XPATH);
        clickByLinkText("Add Public Inquiry");
        waitForTextToBePresent("Add Traffic Commissioner agreement and legislation");
        enterText("//*[@id='fields[agreedDate]_day']","21", SelectorType.XPATH);
        enterText("//*[@id='fields[agreedDate]_month']","6",SelectorType.XPATH);
        enterText("//*[@id='fields[agreedDate]_year']","2014",SelectorType.XPATH);
        selectValueFromDropDown("//*[@id='fields[agreedByTc]']",SelectorType.XPATH,"Nick Jones");
        selectValueFromDropDown("//*[@id='fields[agreedByTcRole]']",SelectorType.XPATH,"Traffic Commissioner");
        selectValueFromDropDown("//*[@id='assignedCaseworker']",SelectorType.XPATH,"ANDREW DREW");
        click("//*[@id='fields_piTypes__chosen']/ul",SelectorType.XPATH);
        selectFirstValueInList("//*[@id='fields_piTypes__chosen']/ul");
        click("//*[@id='fields_piTypes__chosen']/div/ul/li[1]",SelectorType.XPATH);
        selectFirstValueInList("//*[@id='fields_reasons__chosen']/ul/li/input");
        click("//*[@id='fields_reasons__chosen']/div/ul/li[2]",SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']",SelectorType.XPATH);
    }

    public void addAndPublishHearing() throws IllegalBrowserException {
        waitForTextToBePresent("Add hearing");
        clickByLinkText("Add hearing");
        waitForTextToBePresent("Venue");
        selectValueFromDropDown("//*[@id='venue']", SelectorType.XPATH,"Other");
        enterText("//*[@id='venueOther']","Test",SelectorType.XPATH);
        enterText("//*[@id='hearingDate_day']","21",SelectorType.XPATH);
        enterText("//*[@id='hearingDate_month']","6",SelectorType.XPATH);
        enterText("//*[@id='hearingDate_year']","2014",SelectorType.XPATH);
        selectValueFromDropDown("//*[@id='hearingDate_hour']",SelectorType.XPATH,"16");
        selectValueFromDropDown("//*[@id='hearingDate_minute']",SelectorType.XPATH,"00");
        selectValueFromDropDown("//*[@id='presidingTc']",SelectorType.XPATH,"Nick Jones");
        selectValueFromDropDown("//*[@id='presidedByRole']",SelectorType.XPATH,"Traffic Commissioner");
        enterText("//*[@id='fields[witnesses]']","1",SelectorType.XPATH);
        enterText("//*[@id='fields[drivers]']","1",SelectorType.XPATH);
        click("//*[@id='form-actions[publish]']",SelectorType.XPATH);
    }

    public void deleteCaseNote() throws IllegalBrowserException {
        clickByLinkText("Processing");
        clickByLinkText("Notes");
        click("//*[@name='id']", SelectorType.XPATH);
        click("//*[@id='delete']",SelectorType.XPATH);
        waitForTextToBePresent("Delete record");
        click("//*[@id='form-actions[confirm]']",SelectorType.XPATH);
    }

    public void changeLicenceOnTMPage() throws IllegalBrowserException, MalformedURLException {
        javaScriptExecutor("location.reload(true)");
        waitForTextToBePresent("change your licence");
        clickByLinkText("change your licence");
        waitForTextToBePresent("Applying to change a licence");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        javaScriptExecutor("location.reload(true)");
        waitForTextToBePresent("Transport Managers");
        resetApplicationNumberWithURLOnVariational();
        // Set application number because one already exists within the code for the previous test and this requires the new variational one.
    }

    public void addTransportManagerOnTMPage() throws IllegalBrowserException, MalformedURLException, InterruptedException {
        waitForTextToBePresent("Add Transport Manager");
        click("//*[@id='add']", SelectorType.XPATH);
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, 1);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
        String url = Browser.navigate().getCurrentUrl();
        String applicationNumber = GenericUtils.returnNthNumberSequenceInString(url,2);
        world.createLicence.setApplicationNumber(applicationNumber);
        addTransportManagerDetails();
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Revoked, curtailed or suspended Licences");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void resetApplicationNumberWithURLOnVariational() throws IllegalBrowserException, MalformedURLException {
        String url = Browser.navigate().getCurrentUrl();
        String applicationNumber = GenericUtils.returnNthNumberSequenceInString(url, 2);
        world.createLicence.setApplicationNumber(applicationNumber);
    }
}
