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
import org.dvsa.testing.lib.Login;
import org.dvsa.testing.lib.pages.BasePage;
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

    public String getVerifyUsername() {
        return verifyUsername;
    }

    private void setVerifyUsername(String verifyUsername) {
        this.verifyUsername = verifyUsername;
    }

    public UIJourneySteps(World world) {
        this.world = world;
    }

    public void internalSearchForBusReg() throws IllegalBrowserException {
        selectValueFromDropDown("//select[@id='search-select']", SelectorType.XPATH, "Bus registrations");
        do {
            SearchNavBar.search(world.createLicence.getLicenceNumber());
        } while (!isLinkPresent(world.createLicence.getLicenceNumber(), 60));
        clickByLinkText(world.createLicence.getLicenceNumber());
    }

    public void internalSiteAddBusNewReg(int month) throws IllegalBrowserException {
        clickByLinkText(world.createLicence.getLicenceNumber());
        click(nameAttribute("button", "action"));
        waitForTextToBePresent("Service details");
        assertTrue(isTextPresent("Service No. & type", 5));
        enterText("serviceNo", "123", SelectorType.ID);
        enterText("startPoint", Str.randomWord(9), SelectorType.ID);
        enterText("finishPoint", Str.randomWord(11), SelectorType.ID);
        enterText("via", Str.randomWord(5), SelectorType.ID);
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        //This will need to be moved into Page Objects//
        Browser.navigate().findElements(By.xpath("//*[@class=\"active-result\"]")).stream().findFirst().get().click();
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

    public void uploadAndSubmitESBR(String state, int interval) throws MissingRequiredArgument, MalformedURLException, IllegalBrowserException, MissingDriverException {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        world.genericUtils.modifyXML(state, interval);
        GenericUtils.zipFolder();
        navigateToExternalUserLogin();
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
            world.genericUtils.retryingFindClick(By.xpath("//*[@id='form-actions[pay]']"));
        } while (getAttribute("//*[@name='address[addressLine1]']", SelectorType.XPATH, "value").isEmpty());
    }

    public void addPerson(String firstName, String lastName) throws IllegalBrowserException {
        waitForTextToBePresent("Current licences");
        clickByLinkText(world.createLicence.getLicenceNumber());
        waitForTextToBePresent("View your licence");
        clickByLinkText("Directors");
        waitForTextToBePresent("Directors");
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

    public String navigateToInternalTask(World world) throws IllegalBrowserException, MissingDriverException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        world.UIJourneySteps.internalAdminUserLogin();
        world.UIJourneySteps.searchAndViewApplication();
        clickByLinkText("Processing");
        clickByLinkText("Add director(s)");
        waitForTextToBePresent("Linked to");
        return findElement("//div[4]/label", SelectorType.XPATH, 30).getAttribute("class");
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

    public void internalAdminUserLogin() throws MissingRequiredArgument, IllegalBrowserException, MissingDriverException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, env).toString();
        String newPassword = "Password1";
        String password = S3.getTempPassword(world.updateLicence.adminUserEmailAddress);

        if (Browser.isBrowserOpen()) {
            //Quit Browser and open a new window
            Browser.navigate().manage().deleteAllCookies();
        }
        Browser.navigate().get(myURL);
        System.out.println(world.updateLicence.adminUserLogin + "UserLogin");

        if (activesupport.driver.Browser.navigate().getCurrentUrl().contains("da")) {
            Login.signIn(world.updateLicence.adminUserLogin, password);
        }
        if (isTextPresent("Username", 60))
            Login.signIn(world.updateLicence.adminUserLogin, password);
        if (isTextPresent("Current password", 60)) {
            enterField(nameAttribute("input", "oldPassword"), password);
            enterField(nameAttribute("input", "newPassword"), newPassword);
            enterField(nameAttribute("input", "confirmPassword"), newPassword);
            click(nameAttribute("input", "submit"));
        }
    }

    public void navigateToExternalUserLogin() throws MissingRequiredArgument, IllegalBrowserException, MissingDriverException, MalformedURLException {
        String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();
        if (Browser.isBrowserOpen()) {
            //Quit Browser and open a new window
            Browser.navigate().manage().deleteAllCookies();
        }
        Browser.navigate().get(myURL);
        String password = S3.getTempPassword(world.createLicence.getEmailAddress());
        //check if user exists

        if (isTextPresent("Username", 60))
            Login.signIn(world.createLicence.getLoginId(), password);
        if (isTextPresent("Current password", 60)) {
            enterField(nameAttribute("input", "oldPassword"), password);
            enterField(nameAttribute("input", "newPassword"), "Password1");
            enterField(nameAttribute("input", "confirmPassword"), "Password1");
            click(nameAttribute("input", "submit"));
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
        world.UIJourneySteps.navigateToExternalUserLogin();
        world.UIJourneySteps.addPerson(firstName, lastName);
        world.genericUtils.selectAllExternalRadioButtons("No");
        clickByName("form-actions[saveAndContinue]");
        world.genericUtils.selectAllExternalRadioButtons("No");
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
        waitForTextToBePresent("Review and declarations");
        click("//*[@id='declarationsAndUndertakings[signatureOptions]']", SelectorType.XPATH);
        click("//*[@id='sign']", SelectorType.XPATH);
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

    public void addNewPersonAsTransportManager(String forename, String familyName) throws IllegalBrowserException {
        String username = Str.randomWord(3);
        clickByLinkText("change your licence");
        waitForTextToBePresent("Applying to change a licence");
        click("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("Transport Managers");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTextToBePresent("Add Transport Manager");
        waitAndClick("addUser", SelectorType.ID);
        enterText("forename", forename, SelectorType.ID);
        enterText("familyName", familyName, SelectorType.ID);
        String[] date = world.genericUtils.getPastDate(25).toString().split("-");
        enterText("dob_day", date[2], SelectorType.ID);
        enterText("dob_month", date[1], SelectorType.ID);
        enterText("dob_year", date[0], SelectorType.ID);
        enterText("username", "TM".concat(username), SelectorType.ID);
        enterText("emailAddress", "TM@vol.com", SelectorType.ID);
        enterText("emailConfirm", "TM@vol.com", SelectorType.ID);
        waitAndClick("form-actions[continue]", SelectorType.ID);
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

    public void addExistingPersonAsTransportManager() throws IllegalBrowserException {
        waitForTextToBePresent("Apply for a new licence");
        clickByLinkText("Transport");
        waitForTextToBePresent("Transport Managers");
        click("//*[@name='table[action]']",SelectorType.XPATH);
        waitForTextToBePresent("Add Transport Manager");
        selectValueFromDropDownByIndex("data[registeredUser]",SelectorType.ID,1);
        click("//*[@id='form-actions[continue]']",SelectorType.XPATH);
        waitForTextToBePresent("Transport Manager details");
    }
    public void navigateToExternalReviewAndDeclarationsPage() throws IllegalBrowserException, MalformedURLException, MissingDriverException {
        world.UIJourneySteps.navigateToExternalUserLogin();
        clickByLinkText(world.createLicence.getApplicationNumber());
        clickByLinkText("Review");
    }

    public void updateTMDetailsAndNavigateToDeclarationsPage(String isOwner) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException {
        String hours = "8";
        world.genericUtils.findSelectAllRadioButtonsByValue("N");
        findElement("//*[@id='responsibilities']//*[contains(text(),'Internal')]",SelectorType.XPATH,10).click();
        findElement("//*[contains(text(),"+isOwner+")]//*[@id='responsibilities[isOwner]']",SelectorType.XPATH,10).click();
        waitAndEnterText("birthPlace",  SelectorType.ID,"Nottingham");
        waitAndEnterText("postcodeInput1", SelectorType.ID,"NG23HX");
        clickByName("homeAddress[searchPostcode][search]");
        untilElementPresent("//*[@id='homeAddress[searchPostcode][addresses]']",SelectorType.XPATH);
        selectValueFromDropDownByIndex("homeAddress[searchPostcode][addresses]", SelectorType.ID, 1);
        waitAndEnterText("postcodeInput2",  SelectorType.ID,"NG23HX");
        waitAndClick("//*[@id='workAddress[searchPostcode][search]']",SelectorType.XPATH);
        untilElementPresent("//*[@id='workAddress[searchPostcode][addresses]']",SelectorType.XPATH);
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
}