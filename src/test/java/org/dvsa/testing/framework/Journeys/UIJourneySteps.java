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
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.exception.ElementDidNotAppearWithinSpecifiedTimeException;
import org.dvsa.testing.lib.pages.internal.SearchNavBar;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

import java.net.MalformedURLException;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getFutureDate;


public class UIJourneySteps extends BasePage {

    private World world;
    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    static int tmCount;
    private static final String zipFilePath = "/src/test/resources/ESBR.zip";
    private String verifyUsername;
    private String operatorUser;
    private String operatorUserEmail;
    private String operatorForeName;
    private String operatorFamilyName;
    private String password;
    private String externalTMUser;
    private String externalTMEmail;


    public String getVerifyUsername() {
        return verifyUsername;
    }

    private void setVerifyUsername(String verifyUsername) {
        this.verifyUsername = verifyUsername;
    }

    public UIJourneySteps(World world) {
        this.world = world;
    }

    public String getOperatorUser() { return operatorUser; }

    public void setOperatorUser(String operatorUser) {
        this.operatorUser = operatorUser;
    }

    public String getOperatorUserEmail() { return operatorUserEmail; }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExternalTMUser() { return externalTMUser; }

    public void setExternalTMUser(String externalTMUser) { this.externalTMUser = externalTMUser; }

    public String getExternalTMEmail() { return externalTMEmail; }

    public void setExternalTMEmail(String externalTMEmail) { this.externalTMEmail = externalTMEmail; }

    public void internalSearchForBusReg() throws IllegalBrowserException {
        selectValueFromDropDown("//select[@id='search-select']", SelectorType.XPATH, "Bus registrations");
        do {
            SearchNavBar.search(world.createLicence.getLicenceNumber());
        } while (!isLinkPresent(world.createLicence.getLicenceNumber(), 60));
        clickByLinkText(world.createLicence.getLicenceNumber());
    }

    public void internalSiteAddBusNewReg(int month) throws IllegalBrowserException {
        clickByLinkText(world.createLicence.getLicenceNumber());
        waitForTextToBePresent("Overview");
        clickByLinkText("Bus");
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

    public void uploadAndSubmitESBR(String state, int interval) throws MissingRequiredArgument, IllegalBrowserException {
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

    public void searchAndViewApplication() throws IllegalBrowserException {
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

    public void selectFee() throws IllegalBrowserException {
        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH));
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        waitForTextToBePresent("Outstanding");
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
            click("//*[@id='scp_storeCardConfirmationPage_buttons_back_button']", SelectorType.XPATH);
            waitForTextToBePresent("Payment successful");
            clickByLinkText("Back");
            waitForTextToBePresent("There are currently no outstanding fees to pay");
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

    public void navigateToInternalTask() throws IllegalBrowserException, MissingDriverException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        world.UIJourneySteps.navigateToInternalAdminUserLogin();
        world.UIJourneySteps.searchAndViewApplication();
        waitForTextToBePresent("Processing");
        clickByLinkText("Processing");
        waitUntilElementIsEnabled("//body", SelectorType.XPATH);
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

    public void navigateToInternalAdminUserLogin() throws MissingRequiredArgument, IllegalBrowserException {
        String myURL = URL.build(ApplicationType.INTERNAL, env).toString();
        String newPassword = "Password1";
        String password = S3.getTempPassword(world.updateLicence.adminUserEmailAddress);

        if (Browser.isBrowserOpen()) {
            Browser.navigate().manage().deleteAllCookies();
        }
        Browser.navigate().get(myURL);
        System.out.println(world.updateLicence.adminUserLogin + "UserLogin");

        if (Browser.navigate().getCurrentUrl().contains("da")) {
            signIn(world.updateLicence.adminUserLogin, password);
        }
        if (isTextPresent("Username", 60))
            signIn(world.updateLicence.adminUserLogin, password);
        if (isTextPresent("Current password", 60)) {
            enterField(nameAttribute("input", "oldPassword"), password);
            enterField(nameAttribute("input", "newPassword"), newPassword);
            enterField(nameAttribute("input", "confirmPassword"), newPassword);
            click(nameAttribute("input", "submit"));
        }
    }

    private String getBucketName() {
        String s3bucketName;
        if (env == EnvironmentType.LOCAL) {
            s3bucketName = "devapp-olcs-pri-olcs-autotest-s3";
        } else {
            s3bucketName = "devapp-olcs-pri-olcs-autotest-s3";
        }
        return s3bucketName;
    }

    public void navigateToExternalUserLogin(String username, String emailAddress) throws MissingRequiredArgument, IllegalBrowserException {
        String newPassword = "Password1";
        String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();

        if (Browser.isBrowserOpen()) {
            Browser.navigate().manage().deleteAllCookies();
        }
        Browser.navigate().get(myURL);
        String password = S3.getTempPassword(emailAddress, getBucketName());

        try {
            signIn(username, password);
        } catch (Exception e){
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

    public void navigateToExternalSearch() throws IllegalBrowserException {
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
        selectAllExternalRadioButtons("No");
        clickByName("form-actions[saveAndContinue]");
        selectAllExternalRadioButtons("No");
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

    public void signWithVerify(String username, String password) throws IllegalBrowserException {
        setVerifyUsername(username);
        click("//*[contains(text(),'Sign your declaration online')]", SelectorType.XPATH);
        if(isTextPresent("Review and declarations",10)) {
            click("//*[@name='form-actions[sign]']", SelectorType.XPATH);
        }else if (isTextPresent("Declaration",10)) {
            click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
        }
        waitForTextToBePresent("Sign in with GOV.UK Verify");
        click("//*[@id='start_form_selection_false']", SelectorType.XPATH);
        click("//*[@id='next-button']", SelectorType.XPATH);
        click("//*[contains(text(),'Select Post')]", SelectorType.XPATH);
        waitForTextToBePresent("Verified");
        enterText("username", username, SelectorType.NAME);
        enterText("password", password, SelectorType.NAME);
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

    public void internalUserNavigateToDocsTable() throws IllegalBrowserException {
        world.APIJourneySteps.createAdminUser();
        world.UIJourneySteps.navigateToInternalAdminUserLogin();
        world.UIJourneySteps.searchAndViewApplication();
        clickByLinkText("Docs");
    }

    private static void signIn(@NotNull String emailAddress, @NotNull String password, int timeLimitInSeconds) throws IllegalBrowserException {
        LoginPage.email(emailAddress);
        LoginPage.password(password);
        LoginPage.submit();
        LoginPage.untilNotOnPage(timeLimitInSeconds);
    }

    public void addTransportManagerDetails() throws IllegalBrowserException {
        //Add Personal Details
        String birthPlace = world.createLicence.getTown();
        String[] date = world.genericUtils.getPastDate(25).toString().split("-");
        enterText("dob_day", date[2], SelectorType.ID);
        enterText("dob_month", date[1], SelectorType.ID);
        enterText("dob_year", date[0], SelectorType.ID);
        enterText("birthPlace", birthPlace, SelectorType.ID);
        //Add Home Address
        String postCode = world.createLicence.getPostcode();
        enterText("postcodeInput1", postCode, SelectorType.ID);
        clickByName("homeAddress[searchPostcode][search]");
        selectValueFromDropDownByIndex("homeAddress[searchPostcode][addresses]", SelectorType.ID, 1);
        //Add Work Address
        enterText("postcodeInput2", postCode, SelectorType.ID);
        clickByName("workAddress[searchPostcode][search]");
        selectValueFromDropDownByIndex("workAddress[searchPostcode][addresses]", SelectorType.ID, 1);
        //Add Responsibilities
        click("//*[contains(text(),'External')]", SelectorType.XPATH);
        world.genericUtils.selectAllExternalRadioButtons("Y");
        //Add Other Licences
        String role = "Transport Manager";
        click("//*[contains(text(),'Add other licences')]", SelectorType.XPATH);
        waitForTextToBePresent("Add other licence");
        enterText("licNo", "PB123456", SelectorType.ID);
        selectValueFromDropDown("data[role]", SelectorType.ID, role);
    }

    public void nominateOperatorUserAsTransportManager(int user) throws IllegalBrowserException {
        navigateToTransportManagersPage();
        click("//*[@name='table[action]']", SelectorType.XPATH);
        waitForTextToBePresent("Add Transport Manager");
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, user);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
        enterText("dob_day", String.valueOf(getPastDayOfMonth(5)), SelectorType.ID);
        enterText("dob_month", String.valueOf(getCurrentMonth()), SelectorType.ID);
        enterText("dob_year", String.valueOf(getPastYear(20)), SelectorType.ID);
        click("form-actions[send]",SelectorType.ID);
        waitForTextToBePresent("Transport Managers");
    }

    public void addOperatorAdminAsTransportManager(int user) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException {
        navigateToTransportManagersPage();
        click("//*[@name='table[action]']", SelectorType.XPATH);
        waitForTextToBePresent("Add Transport Manager");
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, user);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
        updateTMDetailsAndNavigateToDeclarationsPage("Yes","No","No","No","No");
    }

    public void navigateToTransportManagersPage() throws IllegalBrowserException {
        waitForTextToBePresent("Apply for a new licence");
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

    public void resettingExternalPassword() throws IllegalBrowserException {
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
        findElement("//*[@id='responsibilities']//*[contains(text(),'Internal')]", SelectorType.XPATH, 10).click();
        findElement( "//*[@class='govuk-radios__input'][@value='N'][@name='responsibilities[otherLicencesFieldset][hasOtherLicences]']", SelectorType.XPATH, 10).click();
        findElement("//*[@class='govuk-radios__input'][@value='N'][@name='responsibilities[isOwner]']", SelectorType.XPATH, 10).click();
        findElement("//*[@class='govuk-radios__input'][@value='N'][@name='otherEmployments[hasOtherEmployment]']", SelectorType.XPATH, 10).click();
        findElement("//*[@class='govuk-radios__input'][@value='N'][@name='previousHistory[hasConvictions]']", SelectorType.XPATH, 10).click();
        findElement("//*[@class='govuk-radios__input'][@value='N'][@name='previousHistory[hasPreviousLicences]']", SelectorType.XPATH, 10).click();
        findElement("emailAddress", SelectorType.ID, 10).clear();
        if(findElement("emailAddress",SelectorType.ID,10).getText().isEmpty()) {
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

    public void addOperatorUserAsTransportManager(int user, String isOwner) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException {
        clickByLinkText("Home");
        clickByLinkText(world.createLicence.getApplicationNumber());
        world.UIJourneySteps.nominateOperatorUserAsTransportManager(user);
        world.UIJourneySteps.navigateToExternalUserLogin(world.UIJourneySteps.getOperatorUser(),world.UIJourneySteps.getOperatorUserEmail());
        clickByLinkText(world.createLicence.getApplicationNumber());
        waitForTextToBePresent("Transport Managers");
        clickByLinkText("Transport");
        clickByLinkText(world.UIJourneySteps.getOperatorForeName() + " " + world.UIJourneySteps.getOperatorFamilyName());
        updateTMDetailsAndNavigateToDeclarationsPage(isOwner, "No", "No", "No", "No");
    }

    public void submitTMApplicationAndNavigateToTMLandingPage() throws ElementDidNotAppearWithinSpecifiedTimeException, IllegalBrowserException {
        updateTMDetailsAndNavigateToDeclarationsPage("Yes", "No", "No", "No", "No");
        click("form-actions[submit]", SelectorType.ID);
        clickByLinkText("Back to Transport");
        waitForTextToBePresent("Transport Managers");
    }

    public void addInternalAdmin() throws IllegalBrowserException {
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
}