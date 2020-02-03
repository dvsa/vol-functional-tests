package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.MissingDriverException;
import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.S3;
import activesupport.config.Configuration;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.driver.Browser;
import activesupport.string.Str;
import activesupport.system.Properties;
import autoitx4java.AutoItX;
import com.typesafe.config.Config;
import enums.UserRoles;
import org.apache.commons.lang.StringUtils;
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
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static activesupport.autoITX.AutoITX.initiateAutoItX;
import static activesupport.driver.Browser.navigate;
import static activesupport.msWindowsHandles.MSWindowsHandles.focusWindows;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.returnNthNumberSequenceInString;


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

    private HashMap<String, Integer> myDates;
    private Dates date;
    private Config config;


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
        this.date = new Dates(new LocalDateCalendar());
        config = new Configuration(env.toString()).getConfig();

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

    public void internalSearchForBusReg() throws IllegalBrowserException, MalformedURLException {
        selectValueFromDropDown("//*[@id='search-select']", SelectorType.XPATH, "Bus registrations");
        do {
            SearchNavBar.search(world.createLicence.getLicenceNumber());
        } while (!isLinkPresent(world.createLicence.getLicenceNumber(), 60));
        clickByLinkText(world.createLicence.getLicenceNumber());
    }

    public void internalSiteAddBusNewReg(int month) throws IllegalBrowserException, MalformedURLException {
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

        myDates = date.getDate(0,0,0);

        enterDate(myDates.get("day"),myDates.get("month"),myDates.get("year"));
        enterText("effectiveDate_day", myDates.get("day"), SelectorType.ID);
        enterText("effectiveDate_month", myDates.get("month"), SelectorType.ID);
        enterText("effectiveDate_year", myDates.get("year"), SelectorType.ID);
        click(nameAttribute("button", "form-actions[submit]"));
        do {
            // Refresh page
            javaScriptExecutor("location.reload(true)");
        }
        while (!isTextPresent("Service details", 2));//condition
    }

    private static void enterDate(int day, int month, int year) throws IllegalBrowserException, MalformedURLException {
        enterText("receivedDate_day", String.valueOf(day), SelectorType.ID);
        enterText("receivedDate_month", String.valueOf(month), SelectorType.ID);
        enterText("receivedDate_year", String.valueOf(year), SelectorType.ID);
    }

    public void viewESBRInExternal() throws IllegalBrowserException, MalformedURLException {
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
        clickByLinkText("Bus registrations");
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
            assertTrue(Boolean.parseBoolean(String.valueOf(navigate().getCurrentUrl().contains("variation"))));
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
        String myURL = URL.build(ApplicationType.INTERNAL, env).toString();
        navigate().get(myURL.concat(String.format("application/%s", world.createLicence.getApplicationNumber())));
    }

    public void urlSearchAndViewLicence() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, env).toString();
        navigate().get(myURL.concat(String.format("licence/%s", world.createLicence.getLicenceId())));
    }

    public void urlSearchAndViewVariational() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, env).toString();
        navigate().get(myURL.concat(String.format("variation/%s", world.updateLicence.getVariationApplicationNumber())));
    }

    public void urlSearchAndViewEditFee(String feeNumber) throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, env).toString();
        navigate().get(myURL.concat(String.format("admin/payment-processing/fees/edit-fee/%s", feeNumber)));
    }

    public void urlSearchAndViewInternalUserAccount(String adminUserId) throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, env).toString();
        navigate().get(myURL.concat(String.format("admin/user-management/users/edit/%s", adminUserId)));
    }

    public void createAdminFee(String amount, String feeType) throws IllegalBrowserException, MalformedURLException {
        waitAndClick("//button[@id='new']", SelectorType.XPATH);
        waitForTextToBePresent("Create new fee");
        selectValueFromDropDown("fee-details[feeType]", SelectorType.NAME, feeType);
        waitAndEnterText("amount", SelectorType.ID, amount);
        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void payFee(String amount, @NotNull String paymentMethod, String bankCardNumber, String cardExpiryMonth, String cardExpiryYear) throws IllegalBrowserException, MalformedURLException {
        String payment = paymentMethod.toLowerCase().trim();
        waitForTextToBePresent("Pay fee");
        if (payment.equals("cash") || payment.equals("cheque") || payment.equals("postal")) {
            enterText("details[received]", amount, SelectorType.NAME);
            enterText("details[payer]", "Automation payer", SelectorType.NAME);
            enterText("details[slipNo]", "1234567", SelectorType.NAME);
        }
        switch (payment) {
            case "cash":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cash");
                if (isTextPresent("Customer reference", 10)) {
                    enterText("details[customerName]", "Jane Doe", SelectorType.NAME);
                    enterText("details[customerReference]", "AutomationCashCustomerRef", SelectorType.NAME);
                    findAddress(paymentMethod);
                    clickPayAndConfirm(paymentMethod);
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

                myDates = date.getDate(0,0,0);

                enterText("details[chequeDate][day]", myDates.get("day").toString(), SelectorType.NAME);
                enterText("details[chequeDate][month]", myDates.get("month").toString(), SelectorType.NAME);
                enterText("details[chequeDate][year]", myDates.get("year").toString(), SelectorType.NAME);
                findAddress(paymentMethod);
                clickPayAndConfirm(paymentMethod);
                break;
            case "postal":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Postal Order");
                if (isTextPresent("Payer name", 10)) {
                    enterText("details[payer]", "Jane Doe", SelectorType.NAME);
                }
                enterText("details[customerReference]", "AutomationPostalOrderCustomerRef", SelectorType.NAME);
                enterText("details[customerName]", "Jane Doe", SelectorType.NAME);
                enterText("details[poNo]", "123456", SelectorType.NAME);
                findAddress(paymentMethod);
                clickPayAndConfirm(paymentMethod);
                break;
            case "card":
                if (payment.equals("card") && (isTextPresent("Pay fee", 10))) {
                    selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Card Payment");
                    if (isTextPresent("Customer reference", 10)) {
                        enterText("details[customerName]", "Veena Skish", SelectorType.NAME);
                        enterText("details[customerReference]", "AutomationCardCustomerRef", SelectorType.NAME);
                        findAddress(paymentMethod);
                        clickPayAndConfirm(paymentMethod);
                    }
                }
                customerPaymentModule(bankCardNumber, cardExpiryMonth, cardExpiryYear);
                break;
        }
    }

    public void selectFeeById(String feeNumber) throws IllegalBrowserException, MalformedURLException {
        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH));
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        waitForTextToBePresent("Outstanding");
        clickByLinkText("50");
        waitAndClick("//*[@value='" + feeNumber + "']", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Payment method");
    }

    public void selectFee() throws IllegalBrowserException, MalformedURLException {
        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH));
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        isElementEnabled("//tbody", SelectorType.XPATH);
        waitAndClick("//tbody/tr/td[7]/input", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Pay fee");
    }

    public void customerPaymentModule(String bankCardNumber, String cardExpiryMonth, String cardExpiryYear) throws IllegalBrowserException, MalformedURLException {
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


    private void findAddress(String paymentMethod) throws IllegalBrowserException, MalformedURLException {
        enterText("address[searchPostcode][postcode]", "NG1 5FW", SelectorType.NAME);
        waitAndClick("address[searchPostcode][search]", SelectorType.NAME);
        waitAndSelectByIndex("", "//*[@id='fee_payment']/fieldset[2]/fieldset/div[3]/select[@name='address[searchPostcode][addresses]']", SelectorType.XPATH, 1);
        waitForPageLoad();
    }

    public void clickPayAndConfirm(String paymentMethod) throws IllegalBrowserException, MalformedURLException {
        waitForElementToBeClickable("//*[@id='address[searchPostcode][search]']",SelectorType.XPATH);
        waitAndClick("//*[@id='form-actions[pay]']",SelectorType.XPATH);
        if (!paymentMethod.toLowerCase().trim().equals("card"))
            waitForTextToBePresent("The payment was made successfully");
    }

    public void addPerson(String firstName, String lastName) throws IllegalBrowserException, MalformedURLException {
        clickByName("add");
        waitForTextToBePresent("Add a director");
        selectValueFromDropDown("//select[@id='title']", SelectorType.XPATH, "Dr");
        enterText("forename", firstName, SelectorType.ID);
        enterText("familyname", lastName, SelectorType.ID);

        myDates = date.getDate(-5,0,-20);

        enterText("dob_day", myDates.get("day"), SelectorType.ID);
        enterText("dob_month", myDates.get("month"), SelectorType.ID);
        enterText("dob_year", myDates.get("year"), SelectorType.ID);
        clickByName("form-actions[saveAndContinue]");
    }

    public void navigateToNavBarPage(String page) throws IllegalBrowserException, MalformedURLException {
        switch (page.toLowerCase()) {
            case "home":
                clickByLinkText("Home");
                waitForTextToBePresent("You must keep your records up to date");
                break;
            case "manage users":
                clickByLinkText("Manage users");
                waitForTextToBePresent("Permission");
                break;
            case "your account":
                clickByLinkText("Your account");
                waitForTextToBePresent("Username");
                break;
            case "sign out":
                clickByLinkText("Sign out");
                waitForTextToBePresent("Thank you");
                break;
        }
    }

    public void navigateToSelfServePage(String type, String page) throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("GOV.UK");
        waitForTextToBePresent("You must keep your records up to date");
        String applicationStatus = null;
        String variationApplicationStatus = null;
        String overviewStatus;
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.createLicence.getLicenceNumber());
                waitForTextToBePresent("View and amend your licence");
                break;
            case "application":
                overviewStatus = String.format("//table//tr[td//*[contains(text(),'%s')]]//span[contains(@class,'overview__status')]", world.createLicence.getApplicationNumber());
                applicationStatus = getText(overviewStatus,SelectorType.XPATH);
                clickByLinkText(world.createLicence.getApplicationNumber());
                if (applicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTextToBePresent("Apply for a new licence");
                } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTextToBePresent("Application overview");
                }
                break;
            case "variation":
                overviewStatus = String.format("//table//tr[td//*[contains(text(),'%s')]]//span[contains(@class,'overview__status')]", world.updateLicence.getVariationApplicationNumber());
                variationApplicationStatus = getText(overviewStatus,SelectorType.XPATH);
                clickByLinkText(world.updateLicence.getVariationApplicationNumber());
                if (variationApplicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTextToBePresent("Apply to change a licence");
                } else if (variationApplicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTextToBePresent("Application overview");
                }
                break;
        }
        switch (page.toLowerCase()) {
            case "view":
                switch (type.toLowerCase()) {
                    case "licence":
                        waitForTextToBePresent("View and amend your licence");
                        break;
                    case "application":
                        if (applicationStatus.equals("NOT YET SUBMITTED")) {
                            waitForTextToBePresent("Apply for a new licence");
                        } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                            waitForTextToBePresent("What you need to do next");
                        }
                        break;
                    case "variation":
                        if (variationApplicationStatus.equals("NOT YET SUBMITTED")) {
                            waitForTextToBePresent("Apply to change a licence");
                        } else if (variationApplicationStatus.equals("UNDER CONSIDERATION")) {
                            waitForTextToBePresent("What happens next?");
                        }
                        break;
                }
                break;
            case "type of licence":
                clickByLinkText("Type of licence");
                waitForTextToBePresent("Operator location");
                break;
            case "business type":
                clickByLinkText("Business type");
                waitForTextToBePresent("Business type");
                break;
            case "business details":
                clickByLinkText("Business details");
                waitForTextToBePresent("Business details");
                break;
            case "address":
                clickByLinkText("Address");
                waitForTextToBePresent("Address");
                break;
            case "addresses":
                clickByLinkText("Addresses");
                waitForTextToBePresent("Addresses");
                break;
            case "directors":
                clickByLinkText("Directors");
                waitForTextToBePresent("Directors");
                break;
            case "operating centres":
                clickByLinkText("Operating centres and authorisation");
                waitForTextToBePresent("Operating centres and authorisation");
                break;
            case "transport managers":
                clickByLinkText("Transport Managers");
                waitForTextToBePresent("Transport Managers");
                break;
            case "vehicles":
                clickByLinkText("Vehicles");
                waitForTextToBePresent("Vehicle details");
                break;
            case "vehicle declarations":
                clickByLinkText("Vehicle declarations");
                waitForTextToBePresent("Vehicle declarations");
                break;
            case "trailers":
                clickByLinkText("Trailers");
                waitForTextToBePresent("Trailers");
                break;
            case "licence discs":
                clickByLinkText("Licence discs");
                waitForTextToBePresent("Licence discs");
                break;
            case "safety and compliance":
                clickByLinkText("Safety and compliance");
                waitForTextToBePresent("Safety and compliance");
                break;
            case "conditions and undertakings":
                clickByLinkText("Conditions and undertakings");
                waitForTextToBePresent("Conditions and undertakings");
                break;
            case "financial history":
                clickByLinkText("Financial history");
                waitForTextToBePresent("Financial history");
                break;
            case "financial evidence":
                clickByLinkText("Financial evidence");
                waitForTextToBePresent("Financial evidence");
                break;
            case "licence history":
                clickByLinkText("Licence history");
                waitForTextToBePresent("Licence history");
                break;
            case "convictions and penalties":
                clickByLinkText("Convictions and penalties");
                waitForTextToBePresent("Convictions and Penalties");
                break;
            case "review and declarations":
                clickByLinkText("Review and declarations");
                waitForTextToBePresent("Review and declarations");
                break;
        }
    }

    public void navigateToDirectorsPage(String type) throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("GOV.UK");
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.createLicence.getLicenceNumber());
                break;
            case "application":
                clickByLinkText(world.createLicence.getApplicationNumber());
                break;
            case "variation":
                clickByLinkText(world.updateLicence.getVariationApplicationNumber());
                break;
        }
        clickByLinkText("Directors");
        waitForTextToBePresent("Directors");
    }

    public void navigateToInternalTask() throws IllegalBrowserException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        urlSearchAndViewApplication();
        waitForTextToBePresent("Processing");
        clickByLinkText("Processing");
        isElementEnabled("//body", SelectorType.XPATH);
    }

    public void addPreviousConviction() throws IllegalBrowserException, MalformedURLException {
        selectValueFromDropDown("data[title]", SelectorType.ID, "Ms");
        enterText("data[forename]", Str.randomWord(8), SelectorType.NAME);
        enterText("data[familyName]", Str.randomWord(8), SelectorType.NAME);
        enterText("data[notes]", Str.randomWord(30), SelectorType.NAME);

        myDates = date.getDate(-5,0,-20);

        enterText("dob_day", myDates.get("day").toString(), SelectorType.ID);
        enterText("dob_month", myDates.get("month").toString(), SelectorType.ID);
        enterText("dob_year", myDates.get("year").toString(), SelectorType.ID);

        enterText("data[categoryText]", Str.randomWord(50), SelectorType.NAME);
        enterText("data[courtFpn]", "Clown", SelectorType.NAME);
        enterText("data[penalty]", "Severe", SelectorType.NAME);
        clickByName("form-actions[submit]");
    }

    public void navigateToInternalAdminUserLogin(String username, String emailAddress) throws MissingRequiredArgument, IllegalBrowserException, MalformedURLException {
        String newPassword = config.getString("internalNewPassword");
        String myURL = URL.build(ApplicationType.INTERNAL, env).toString();

        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
            navigate().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }
        navigate().get(myURL);
        String password = S3.getTempPassword(emailAddress, getBucketName());

        try {
            signIn(username, password);
        } catch (Exception e) {
            //User is already registered
            signIn(username, getPassword());
        } finally {
            if (isTextPresent("Current password", 2000)) {
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
        String newPassword = config.getString("internalNewPassword");
        String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();

        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
            navigate().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }
        get(myURL);
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

    public void CheckSkipToMainContentOnExternalUserLogin() throws MissingRequiredArgument, IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();

        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
        }
        navigate().get(myURL);
        skipToMainContentAndCheck();
    }

    public void navigateToExternalSearch() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.EXTERNAL, env, "search/find-lorry-bus-operators/").toString();
        navigate().get(myURL);
    }

    public void generateLetter() throws IllegalBrowserException, MalformedURLException {
        Browser.navigate().manage().window().maximize();
        clickByLinkText("Docs & attachments");
        waitForElementToBePresent("//button[@id='New letter']");
        clickByName("New letter");
        findElement("//*[@id='modal-title']", SelectorType.XPATH, 60);
        waitAndSelectByIndex("Generate letter", "//*[@id='category']", SelectorType.XPATH, 1);
        waitAndSelectByIndex("Generate letter", "//*[@id='documentSubCategory']", SelectorType.XPATH, 1);
        waitAndSelectByIndex("Generate letter", "//*[@id='documentTemplate']", SelectorType.XPATH, 1);
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Amend letter");
    }

    public void saveDocumentInInternal() throws IllegalBrowserException, MalformedURLException {
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitAndClick("//*[@id='close']", SelectorType.XPATH);
        waitForTextToBePresent("The document has been saved");
    }

    public void editDocumentWithWebDav() throws IllegalBrowserException, IOException, InterruptedException {
        // Forgive us for using sleeps. There's no other way as this is not a window that selenium can recognise.
        String window = "Olcs - ".concat(world.createLicence.getLicenceNumber()).concat(" - Google Chrome");
        String wordLoginWindow = StringUtils.removeEnd(URL.build(ApplicationType.INTERNAL, env).toString(), "/");

        Thread.sleep(1000);
        clickByLinkText("BUS");
        AutoItX autoIt = initiateAutoItX("jacob-1.16", "lib/jacob-1.16");

        autoIt.winWaitActive(window, "Chrome Legacy Window", 20);
        Thread.sleep(1000);
        autoIt.mouseClick("left", 1200, 230, 2, 20);

        autoIt.winWaitActive(wordLoginWindow, "", 20);
        Thread.sleep(3000);
        if (autoIt.winExists(wordLoginWindow, "")) {
            autoIt.mouseClick("left", 1100, 630, 2, 1);
            autoIt.send(world.updateLicence.getAdminUserLogin());
            autoIt.mouseClick("left", 1100, 720, 2, 1);
            autoIt.send(getPassword());
            autoIt.mouseClick("left", 990, 780, 2, 1);
        }
        Thread.sleep(2000);
        focusWindows("OpusApp", null);
        //Document edit and save
        Thread.sleep(3000);
        autoIt.mouseClick("left", 955, 750, 2, 1);
        autoIt.send("WebDav Change!");
        Thread.sleep(500);
        autoIt.mouseClick("left", 210, 27, 2, 1);
        Thread.sleep(500);
        autoIt.mouseClick("left", 2240, 27, 2, 1);

        saveDocumentInInternal();
    }

    public void printLicence() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("Docs & attachments");
        waitForElementToBePresent("//a[@id='menu-licence-quick-actions-print-licence']");
        clickByLinkText("Print licence");
        waitForTextToBePresent("Licence printed successfully");
    }

    public void deleteLicenceDocument() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("Docs & attachments");
        deleteDocument();
    }

    public void deleteLetterDocument() throws IllegalBrowserException, MalformedURLException {
        waitForTextToBePresent("Bus Registration");
        deleteDocument();
    }

    public void deleteDocument() throws IllegalBrowserException, MalformedURLException {
        waitAndClick("//input[@name='id[]']", SelectorType.XPATH);
        waitAndClick("//button[@id='delete']", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to remove the selected record(s)?");
        waitAndClick("//button[@id='form-actions[confirm]']", SelectorType.XPATH);
    }

    public void removeInternalTransportManager() throws IllegalBrowserException, MalformedURLException {
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
        navigateToSelfServePage("licence", "directors");
        addPerson(firstName, lastName);
        findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
        findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
    }

    public void changeVehicleReq(String noOfVehicles) throws IllegalBrowserException, MalformedURLException, InterruptedException {
        click("//*[@id='overview-item__operating_centres']", SelectorType.XPATH);
        changeLicenceForVariation();
        waitAndClick("//*[@id=\"OperatingCentres\"]/fieldset[1]/div/div[2]/table/tbody/tr/td[1]/input", SelectorType.XPATH);
        enterField(nameAttribute("input", "data[noOfVehiclesRequired]"), noOfVehicles);
        world.updateLicence.setVariationApplicationNumber(returnNthNumberSequenceInString(navigate().getCurrentUrl(), 2));
        if (Integer.parseInt(noOfVehicles) > world.createLicence.getNoOfVehiclesRequired()) {
            click(nameAttribute("button", "form-actions[submit]"));
        }
        click(nameAttribute("button", "form-actions[submit]"));
    }

    public void changeVehicleAuth(String noOfAuthVehicles) throws IllegalBrowserException, MalformedURLException {
        enterField(nameAttribute("input", "data[totAuthVehicles]"), noOfAuthVehicles);
        click(nameAttribute("button", "form-actions[save]"));
    }

    public void signWithVerify() throws IllegalBrowserException, MalformedURLException {
        setVerifyUsername(verifyUsername);
        String verifyUsername = config.getString("verifyUsername");
        String verifyPassword = config.getString("verifyPassword");

        waitForTextToBePresent("Sign in with GOV.UK Verify");
        click("//*[@id='start_form_selection_false']", SelectorType.XPATH);
        click("//*[@id='next-button']", SelectorType.XPATH);
        click("//*[contains(text(),'Select Post')]", SelectorType.XPATH);
        waitForTextToBePresent("Verified");
        enterText("username", verifyUsername, SelectorType.NAME);
        enterText("password", verifyPassword, SelectorType.NAME);
        while (size("//*[contains(text(),'Verified ID Login')]",SelectorType.XPATH) > 0) {
            click("//*[@value='SignIn']",SelectorType.XPATH);
        }
        waitForTextToBePresent("Personal Details");
        click("//*[@id='agree']", SelectorType.XPATH);
    }

    public void addNewPersonAsTransportManager(String forename, String familyName, String emailAddress) throws IllegalBrowserException, MalformedURLException {
        this.externalTMEmail = emailAddress;
        externalTMUser = "TM".concat(Str.randomWord(3));
        waitForTextToBePresent("Transport Managers");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTextToBePresent("Add Transport Manager");
        waitAndClick("addUser", SelectorType.ID);
        enterText("forename", forename, SelectorType.ID);
        enterText("familyName", familyName, SelectorType.ID);

        myDates = date.getDate(0,0,25);
        enterText("dob_day", myDates.get("day").toString(), SelectorType.ID);
        enterText("dob_month", myDates.get("month").toString(), SelectorType.ID);
        enterText("dob_year", myDates.get("year").toString(), SelectorType.ID);

        enterText("username", externalTMUser, SelectorType.ID);
        enterText("emailAddress", externalTMEmail, SelectorType.ID);
        enterText("emailConfirm", externalTMEmail, SelectorType.ID);
        waitAndClick("form-actions[continue]", SelectorType.ID);
    }

    public void addDirector(String forename, String familyName) throws IllegalBrowserException, MalformedURLException {
        addPerson(forename, familyName);
        world.genericUtils.findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
        world.genericUtils.findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
    }

    public void removeDirector() throws IllegalBrowserException, MalformedURLException {
        int sizeOfTable = size("//*/td[4]/input[@type='submit']", SelectorType.XPATH);
        click("//*/tr[" + sizeOfTable + "]/td[4]/input[@type='submit']", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void internalUserNavigateToDocsTable() throws IllegalBrowserException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        urlSearchAndViewApplication();
        clickByLinkText("Docs");
    }

    private static void signIn(@NotNull String emailAddress, @NotNull String password) throws IllegalBrowserException, MalformedURLException {
        LoginPage.email(emailAddress);
        LoginPage.password(password);
        LoginPage.submit();
        LoginPage.untilNotOnPage(2);
    }

    public void addTransportManagerDetails() throws IllegalBrowserException, InterruptedException, MalformedURLException {
        //Add Personal Details
        String birthPlace = world.createLicence.getTown();

        myDates = date.getDate(0,0,-25);

        enterText("dob_day", myDates.get("day").toString(), SelectorType.ID);
        enterText("dob_month", myDates.get("month").toString(), SelectorType.ID);
        enterText("dob_year", myDates.get("year").toString(), SelectorType.ID);
        enterText("birthPlace", birthPlace, SelectorType.ID);

        waitForElementToBeClickable("//*[contains(text(),'External')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'External')]", SelectorType.XPATH);
        world.genericUtils.findSelectAllRadioButtonsByValue("Y");

        //Add Home Address
        addAddressDetails();


        //Hours Of Week
        waitForElementToBeClickable("//*[contains(@name,'responsibilities[hoursOfWeek]')]", SelectorType.XPATH);
        enterSameTextIntoMultipleFieldsPartialMatch("//*[contains(@name,'responsibilities[hoursOfWeek]')]", SelectorType.XPATH, "3");

        //Add Other Licences
        String role = "Transport Manager";
        waitAndClick("//*[contains(text(),'Add other licence')]", SelectorType.XPATH);
        javaScriptExecutor("location.reload(true)");
        waitAndEnterText("licNo", SelectorType.ID, "PB123456");
        selectValueFromDropDown("//*[@id='data[role]']", SelectorType.XPATH, role);
        enterText("//*[@id='operatingCentres']", "Test", SelectorType.XPATH);
        enterText("//*[@id='hoursPerWeek']", "1", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);

        //Add Other Employment
        waitForTextToBePresent("Add other employment");
        waitAndClick("//*[contains(text(),'Add other employment')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='tm-employer-name-details[employerName]']", SelectorType.XPATH, "test");
        String postCode = world.createLicence.getPostcode();
        enterText("postcodeInput1", postCode, SelectorType.ID);
        clickByName("address[searchPostcode][search]");
        waitAndClick("address[searchPostcode][addresses]", SelectorType.ID);
        selectValueFromDropDownByIndex("address[searchPostcode][addresses]", SelectorType.ID, 1);
        waitAndEnterText("//*[@id='tm-employment-details[position]']", SelectorType.XPATH, "test");
        waitAndEnterText("//*[@id='tm-employment-details[hoursPerWeek]']", SelectorType.XPATH, "Test");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);


        // Convictions
        waitForTextToBePresent("Add convictions and penalties");
        waitAndClick("//*[contains(text(),'Add convictions and penalties')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='conviction-date_day']", SelectorType.XPATH, "03");
        enterText("//*[@id='conviction-date_month']", "03", SelectorType.XPATH);
        enterText("//*[@id='conviction-date_year']", "2014", SelectorType.XPATH);
        enterText("//*[@id='category-text']", "Test", SelectorType.XPATH);
        enterText("//*[@id='notes']", "Test", SelectorType.XPATH);
        enterText("//*[@id='court-fpn']", "Test", SelectorType.XPATH);
        enterText("//*[@id='penalty']", "Test", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);

        waitForTextToBePresent("Add licences");
        waitAndClick("//*[contains(text(),'Add licences')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='lic-no']", SelectorType.XPATH, "PD263849");
        waitAndEnterText("//*[@id='holderName']", SelectorType.XPATH, "PD263849");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addAddressDetails() throws IllegalBrowserException, InterruptedException, MalformedURLException {
        //Add Home Address
        String postCode = world.createLicence.getPostcode();
        enterText("postcodeInput1", postCode, SelectorType.ID);
        clickByName("homeAddress[searchPostcode][search]");
        waitAndClick("homeAddress[searchPostcode][addresses]", SelectorType.ID);
        selectValueFromDropDownByIndex("homeAddress[searchPostcode][addresses]", SelectorType.ID, 1);
        //Add Work Address
        waitAndEnterText("postcodeInput2", SelectorType.ID, postCode);
        waitAndClick("workAddress[searchPostcode][search]", SelectorType.ID);
        waitAndClick("workAddress[searchPostcode][addresses]", SelectorType.ID);
        selectValueFromDropDownByIndex("workAddress[searchPostcode][addresses]", SelectorType.ID, 1);
    }

    public void nominateOperatorUserAsTransportManager(int user, boolean applicationOrNot) throws IllegalBrowserException, MalformedURLException, InterruptedException {
        if (applicationOrNot) {
            navigateToTransportManagersPage("application");
        } else {
            navigateToTransportManagersPage("licence");
            changeLicenceForVariation(); // If licence already created then this creates variational
        }
        waitForTextToBePresent("Transport Managers");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTextToBePresent("Add Transport Manager");
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, user);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);

        myDates = date.getDate(-5,0,-20);
        enterText("dob_day", myDates.get("day").toString(), SelectorType.ID);
        enterText("dob_month", myDates.get("month").toString(), SelectorType.ID);
        enterText("dob_year", myDates.get("year").toString(), SelectorType.ID);

        waitForElementToBeClickable("form-actions[send]", SelectorType.ID);
        click("form-actions[send]", SelectorType.ID);
        waitForTextToBePresent("Transport Managers");
    }

    public void addOperatorAdminAsTransportManager(int user) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException {
        navigateToTransportManagersPage("application");
        click("//*[@name='table[action]']", SelectorType.XPATH);
        waitForTextToBePresent("Add Transport Manager");
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, user);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
    }

    public void navigateToTransportManagersPage(String type) throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("GOV.UK");
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.createLicence.getLicenceNumber());
                break;
            case "application":
                clickByLinkText(world.createLicence.getApplicationNumber());
                break;
            case "variation":
                clickByLinkText(world.updateLicence.getVariationApplicationNumber());
                break;
        }
        clickByLinkText("Transport Managers");
        waitForTextToBePresent("Transport Managers");
    }

    public void navigateToViewAndAmendPage(String type) throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("GOV.UK");
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.createLicence.getLicenceNumber());
                break;
            case "application":
                clickByLinkText(world.createLicence.getApplicationNumber());
                break;
            case "variation":
                clickByLinkText(world.updateLicence.getVariationApplicationNumber());
                break;
        }
    }

    public void navigateToOperatingCentresPage(String type) throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("GOV.UK");
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.createLicence.getLicenceNumber());
                break;
            case "application":
                clickByLinkText(world.createLicence.getApplicationNumber());
                break;
            case "variation":
                clickByLinkText(world.updateLicence.getVariationApplicationNumber());
                break;
        }
        clickByLinkText("Operating centres and authorisation");
        waitForTextToBePresent("How many vehicles do you want to authorise on the licence");
    }

    public void navigateToVehiclesPage() throws IllegalBrowserException, MalformedURLException {
//        clickByLinkText("GOV.UK");
//        clickByLinkText(world.createLicence.getApplicationNumber());
        clickByLinkText("Vehicles");
        waitForTextToBePresent("Vehicle details");
    }

    public void navigateToReviewDeclarationsPage(String type) throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("GOV.UK");
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.createLicence.getLicenceNumber());
                break;
            case "application":
                clickByLinkText(world.createLicence.getApplicationNumber());
                break;
            case "variation":
                clickByLinkText(world.updateLicence.getVariationApplicationNumber());
                break;
        }
        clickByLinkText("Review");
        waitForTextToBePresent("Review and declarations");
    }

    public void resettingExternalPassword() throws IllegalBrowserException, MalformedURLException {
        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
        }
        String env = System.getProperty("env");
        String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();
        navigate().get(myURL);
        clickByLinkText("Forgotten your password?");
    }

    public void updateTMDetailsAndNavigateToDeclarationsPage(String isOwner, String OtherLicence, String hasEmployment, String hasConvictions, String hasPreviousLicences) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException {
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

    public void addOperatorUserAsTransportManager(int user, String isOwner, boolean applicationOrNot) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException, InterruptedException {
        nominateOperatorUserAsTransportManager(user, applicationOrNot);
        navigateToExternalUserLogin(getOperatorUser(), getOperatorUserEmail());
        if (applicationOrNot) {
            navigateToTransportManagersPage("application");
        } else {
            navigateToTransportManagersPage("variation");
        }
        clickByLinkText(getOperatorForeName() + " " + getOperatorFamilyName());
        updateTMDetailsAndNavigateToDeclarationsPage(isOwner, "N", "N", "N", "N");
    }

    public void submitTMApplicationAndNavigateToTMLandingPage() throws ElementDidNotAppearWithinSpecifiedTimeException, IllegalBrowserException, MalformedURLException {
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
        navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
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
        setLicenceNumber(navigate().findElements(By.xpath("//tr/td[1]")).stream().findFirst().get().getText());
        navigate().findElements(By.xpath("//tr/td[1]")).stream().findFirst().ifPresent(WebElement::click);
        waitForTextToBePresent("Summary");
        clickByLinkText("Apply to");
    }

    public void navigateToFinancialEvidencePage(String type) throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("GOV.UK");
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.createLicence.getLicenceNumber());
                break;
            case "application":
                clickByLinkText(world.createLicence.getApplicationNumber());
                break;
            case "variation":
                clickByLinkText(world.updateLicence.getVariationApplicationNumber());
                break;
        }
        clickByLinkText("Financial evidence");
        waitForTextToBePresent("need to prove you have enough money");
    }

    public void updateFinancialInformation() throws IllegalBrowserException, MalformedURLException {
        navigateToFinancialEvidencePage("variation");
        javaScriptExecutor("location.reload(true)");
        click("//*[@id='uploadLaterRadio']", SelectorType.XPATH);
        click("//*[@id='form-actions[save]']", SelectorType.XPATH);
    }

    public void signDeclaration() throws IllegalBrowserException, MalformedURLException {
        waitAndClick("//*[contains(text(),'Sign your declaration online')]", SelectorType.XPATH);
        if (isTextPresent("Review and declarations", 10)) {
            click("//*[@name='form-actions[sign]']", SelectorType.XPATH);
        } else if (isTextPresent("Declaration", 10)) {
            click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
        }
    }

    public void signDeclarationForVariation() throws IllegalBrowserException, MalformedURLException {
        navigateToReviewDeclarationsPage("variation");
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        if (size("//*[@id='submitAndPay']",SelectorType.XPATH) != 0) {
            click("//*[@id='submitAndPay']", SelectorType.XPATH);
        } else if (size("//*[@id='submit']",SelectorType.XPATH) != 0)
            click("//*[@id='submit']", SelectorType.XPATH);
    }

    public void navigateThroughApplication() throws IllegalBrowserException, MalformedURLException {
        waitForTextToBePresent("Apply for a new licence");
        clickByLinkText("Type of licence");
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
        assertTrue(getCurrentUrl().contains("review"));
        assertTrue(isTextPresent("Review your surrender", 40));
    }

    public void startSurrender() throws IllegalBrowserException, MalformedURLException {
        click("//*[@id='submit']", SelectorType.XPATH);
        waitForTextToBePresent("Review your contact information");
    }

    public void addDiscInformation(String discToDestroy, String discsLost, String discsStolen) throws IllegalBrowserException, MalformedURLException {
        assertTrue(getCurrentUrl().contains("current-discs"));
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

    public void addOperatorLicenceDetails() throws IllegalBrowserException, MalformedURLException {
        click("//*[contains(text(),'Lost')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='operatorLicenceDocument[lostContent][details]']", SelectorType.XPATH, "lost in the washing");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addCommunityLicenceDetails() throws IllegalBrowserException, MalformedURLException {
        click("//*[contains(text(),'Stolen')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='communityLicenceDocument[stolenContent][details]']", SelectorType.XPATH, "Stolen on the way here");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void acknowledgeDestroyPage() throws IllegalBrowserException, MalformedURLException {
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Securely destroy");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Declaration");
    }

    public void updateContactDetails(String addressLine1, String addressLine2, String contactNumber) throws IllegalBrowserException, MalformedURLException {
        findElement("addressLine1", SelectorType.ID, 10).clear();
        enterText("addressLine1", addressLine1, SelectorType.ID);
        findElement("correspondence_address[addressLine2]", SelectorType.ID, 10).clear();
        enterText("correspondence_address[addressLine2]", addressLine2, SelectorType.ID);
        findElement("phone_primary", SelectorType.ID, 10).clear();
        enterText("phone_primary", contactNumber, SelectorType.ID);
        waitAndClick("form-actions[save]", SelectorType.ID);
    }

    public String getSurrenderAddressLine1() throws IllegalBrowserException, MalformedURLException {
        return getText("//*[@class='app-check-your-answers app-check-your-answers--long'][2]/div[@class='app-check-your-answers__contents'][1]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
    }

    public String getSurrenderTown() throws IllegalBrowserException, MalformedURLException {
        return getText("//*[@class='app-check-your-answers app-check-your-answers--long'][2]/div[@class='app-check-your-answers__contents'][2]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
    }

    public String getSurrenderContactNumber() throws IllegalBrowserException, MalformedURLException {
        return getText("//*[@class='app-check-your-answers app-check-your-answers--long'][3]/div[@class='app-check-your-answers__contents'][1]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
    }

    public void submitSurrender() throws MalformedURLException, IllegalBrowserException {
        submitSurrenderUntilChoiceOfVerification();
        if (navigate().getCurrentUrl().contains("qa")) {
            waitAndClick("//*[@id='sign']", SelectorType.XPATH);
            signWithVerify();
            checkVerifyConfirmation();
        } else {
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            signManually();
        }
        assertEquals(getText("//*[@class='overview__status green']", SelectorType.XPATH), "SURRENDER UNDER CONSIDERATION");
    }

    public void checkVerifyConfirmation() throws IllegalBrowserException, MalformedURLException {
        waitForTextToBePresent("What happens next");
        Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
        Assert.assertTrue(isTextPresent(String.format("Application to surrender licence %s", world.createLicence.getLicenceNumber()), 10));
        Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy")), 20));
        assertTrue(isTextPresent("notifications@vehicle-operator-licensing.service.gov.uk", 10));
        waitAndClick("//*[contains(text(),'home')]", SelectorType.XPATH);
    }

    public void submitSurrenderUntilChoiceOfVerification() throws IllegalBrowserException, MalformedURLException {
        navigateToSurrendersStartPage();
        startSurrender();
        waitAndClick("form-actions[submit]", SelectorType.ID);
        addDiscInformation("2", "2", "1");
        waitForTextToBePresent("In your possession");
        addOperatorLicenceDetails();
        if (world.createLicence.getLicenceType().equals("standard_international")) {
            assertTrue(navigate().getCurrentUrl().contains("community-licence"));
            addCommunityLicenceDetails();
        }
        acknowledgeDestroyPage();
    }

    public void caseworkManageSurrender() throws MalformedURLException, IllegalBrowserException {
        world.APIJourneySteps.createAdminUser();
        navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        searchAndViewLicence();
        clickByLinkText("Surrender");
        waitForTextToBePresent("Surrender details");
        waitAndClick("//*[@for='checks[ecms]']", SelectorType.XPATH);
        // Refresh page
        javaScriptExecutor("location.reload(true)");
        waitAndClick("//*[contains(text(),'Digital signature')]", SelectorType.XPATH);
    }

    public void signManually() throws IllegalBrowserException, MalformedURLException {
        String defaultWindow = navigate().getWindowHandle();
        Set<String> windows;
        waitForTextToBePresent("A business owner");
        do {
            clickByLinkText("Print");
            windows = navigate().getWindowHandles();
        } while (windows.size() == 1);
        String printWindow = windows.stream().reduce((first, second) -> second).get();
        navigate().switchTo().window(printWindow).close();
        navigate().switchTo().window(defaultWindow);
        click("//*[contains(@title,'return to home')]", SelectorType.XPATH);
    }

    public void checkLicenceStatus(String arg0) throws IllegalBrowserException, MalformedURLException {
        do {
            System.out.println("Page not loaded yet");
        }
        while (!isTextPresent("Licence details", 2));//condition
        Assertions.assertEquals(getText("//*[contains(@class,'status')]", SelectorType.XPATH), arg0.toUpperCase());
    }

    public void removeDisc(String discDestroyed, String discLost, String discStolen) throws IllegalBrowserException, MalformedURLException, ElementDidNotAppearWithinSpecifiedTimeException {
        waitAndClick("form-actions[submit]", SelectorType.ID);
        addDiscInformation(discDestroyed, discLost, discStolen);
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

    public void addDisc() throws IllegalBrowserException, MalformedURLException {
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
        world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
        world.APIJourneySteps.createApplication();
        world.APIJourneySteps.submitApplication();
        world.APIJourneySteps.grantLicenceAndPayFees();
    }

    public void closeCase() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("" + world.updateLicence.getCaseId() + "");

        String myURL = URL.build(ApplicationType.INTERNAL, env).toString();
        String casePath = String.format("/case/details/%s", String.valueOf(world.updateLicence.getCaseId()));
        navigate().get(myURL.concat(casePath));
        clickByLinkText("Close");
        waitForTextToBePresent("Close the case");
        click("form-actions[confirm]", SelectorType.ID);

    }

    public void closeBusReg() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("" + world.createLicence.getLicenceNumber() + "");
        click("menu-bus-registration-decisions-admin-cancel", SelectorType.ID);
        waitForTextToBePresent("Update status");
        enterText("fields[reason]", "Mistake", SelectorType.ID);
        click("form-actions[submit]", SelectorType.ID);
    }

    public void payFeesAndGrantNewBusReg() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("Fees");
        selectFee();
        payFee("60", "cash", null, null, null);
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

    public void createLicenceWithOpenCaseAndBusReg(String operatorType, String licenceType) throws IllegalBrowserException, MalformedURLException, InterruptedException {
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
        world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
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
        navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        urlSearchAndViewLicence();
        internalSiteAddBusNewReg(5);
        payFeesAndGrantNewBusReg();
        world.updateLicence.createCase();
    }

    public void internalDigitalSurrenderMenu() throws IllegalBrowserException, MalformedURLException {
        if (!isElementPresent("menu-licence_surrender", SelectorType.ID)) {
            do {
                System.out.println("waiting for page to load");
                javaScriptExecutor("location.reload(true)");
            } while (!isLinkPresent("" + world.createLicence.getLicenceNumber() + "", 10));
            clickByLinkText("" + world.createLicence.getLicenceNumber() + "");
        }
        click("menu-licence_surrender", SelectorType.ID);
    }

    public void payForInterimApp() throws IllegalBrowserException, InterruptedException, MalformedURLException {
        clickByLinkText("Financial");
        waitAndClick("//*[contains(text(),'Send')]", SelectorType.XPATH);
        waitAndClick("form-actions[save]", SelectorType.NAME);
        clickByLinkText("Review");
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        enterText("interim[goodsApplicationInterimReason]", "Testing", SelectorType.NAME);
        click("submitAndPay", SelectorType.ID);
        click("//*[@name='form-actions[pay]']", SelectorType.XPATH);
        customerPaymentModule(config.getString("cardNumber"), config.getString("cardExpiryMonth"), config.getString("cardExpiryYear"));
    }

    public void addNewOperatingCentre() throws IllegalBrowserException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        searchAndViewLicence();
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

    public void caseWorkerCompleteConditionsAndUndertakings() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("Conditions and undertakings");
        click("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void caseWorkerCompleteReviewAndDeclarations() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("Review and declarations");
        waitAndClick("//*[@id='declarations[declarationConfirmation]']", SelectorType.XPATH);
        click("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void caseWorkerCompleteOverview() throws IllegalBrowserException, MalformedURLException {
        click("//*[@id='details[overrideOppositionDate]']", SelectorType.XPATH);
        navigate().findElements(By.xpath("//*[contains(@id,'tracking')]/option[2]")).stream().forEach(WebElement::click);
        click("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void caseWorkerGrantApplication() throws IllegalBrowserException, MalformedURLException {
        javaScriptExecutor("location.reload(true)");
        waitAndClick("//*[@id='menu-application-decisions-grant']", SelectorType.XPATH);
        waitAndClick("//*[@id='inspection-request-confirm[createInspectionRequest]']", SelectorType.XPATH);
        click("//*[@id='form-actions[grant]']", SelectorType.XPATH);
    }

    public void createPublicInquiry() throws IllegalBrowserException, MalformedURLException {
        click("//*[@id='menu-case_hearings_appeals']", SelectorType.XPATH);
        clickByLinkText("Add Public Inquiry");
        waitForTextToBePresent("Add Traffic Commissioner agreement and legislation");
        enterText("//*[@id='fields[agreedDate]_day']", "21", SelectorType.XPATH);
        enterText("//*[@id='fields[agreedDate]_month']", "6", SelectorType.XPATH);
        enterText("//*[@id='fields[agreedDate]_year']", "2014", SelectorType.XPATH);
        selectValueFromDropDown("//*[@id='fields[agreedByTc]']", SelectorType.XPATH, "Nick Jones");
        selectValueFromDropDown("//*[@id='fields[agreedByTcRole]']", SelectorType.XPATH, "Traffic Commissioner");
        selectValueFromDropDown("//*[@id='assignedCaseworker']", SelectorType.XPATH, "ANDREW DREW");
        click("//*[@id='fields_piTypes__chosen']/ul", SelectorType.XPATH);
        selectFirstValueInList("//*[@id='fields_piTypes__chosen']/ul");
        click("//*[@id='fields_piTypes__chosen']/div/ul/li[1]", SelectorType.XPATH);
        selectFirstValueInList("//*[@id='fields_reasons__chosen']/ul/li/input");
        click("//*[@id='fields_reasons__chosen']/div/ul/li[2]", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addAndPublishHearing() throws IllegalBrowserException, MalformedURLException {
        waitForTextToBePresent("Add hearing");
        clickByLinkText("Add hearing");
        waitForTextToBePresent("Venue");
        selectValueFromDropDown("//*[@id='venue']", SelectorType.XPATH, "Other");
        enterText("//*[@id='venueOther']", "Test", SelectorType.XPATH);
        enterText("//*[@id='hearingDate_day']", "21", SelectorType.XPATH);
        enterText("//*[@id='hearingDate_month']", "6", SelectorType.XPATH);
        enterText("//*[@id='hearingDate_year']", "2014", SelectorType.XPATH);
        selectValueFromDropDown("//*[@id='hearingDate_hour']", SelectorType.XPATH, "16");
        selectValueFromDropDown("//*[@id='hearingDate_minute']", SelectorType.XPATH, "00");
        selectValueFromDropDown("//*[@id='presidingTc']", SelectorType.XPATH, "Nick Jones");
        selectValueFromDropDown("//*[@id='presidedByRole']", SelectorType.XPATH, "Traffic Commissioner");
        enterText("//*[@id='fields[witnesses]']", "1", SelectorType.XPATH);
        enterText("//*[@id='fields[drivers]']", "1", SelectorType.XPATH);
        click("//*[@id='form-actions[publish]']", SelectorType.XPATH);
    }

    public void deleteCaseNote() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("Processing");
        clickByLinkText("Notes");
        click("//*[@name='id']", SelectorType.XPATH);
        click("//*[@id='delete']", SelectorType.XPATH);
        waitForTextToBePresent("Delete record");
        click("//*[@id='form-actions[confirm]']", SelectorType.XPATH);
    }

    public void navigateToChangeHistory() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("Processing");
        waitForTextToBePresent("Tasks");
        clickByLinkText("Change history");
        waitForTextToBePresent("Details");
    }

    public void createCaseUI(String target) throws IllegalBrowserException, MalformedURLException {
        switch (target.toLowerCase()) {
            case "licence":
                urlSearchAndViewLicence();
                break;
            case "application":
                urlSearchAndViewApplication();
                break;
            case "variation":
                urlSearchAndViewVariational();
                break;
        }
        if (getText("//*/span[contains(@class,'status')]",SelectorType.XPATH).equals("UNDER CONSIDERATION")) {
            waitAndClick("//*[@id='menu-application_case']", SelectorType.XPATH);
        } else if (getText("//*/span[contains(@class,'status')]",SelectorType.XPATH).equals("VALID")) {
            waitAndClick("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        }
        click("//*[@id='add']", SelectorType.XPATH);
        waitAndClick("//*[@id='fields_categorys__chosen']/ul", SelectorType.XPATH);
        click("//li[contains(text(),'Convictions')]", SelectorType.XPATH);
        enterText("//*[@id='fields[description]']", "testing", SelectorType.XPATH);
        enterText("//*[@id='fields[ecmsNo]']", "12345", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void changeLicenceForVariation() throws IllegalBrowserException, MalformedURLException {
        javaScriptExecutor("location.reload(true)");
        waitForTextToBePresent("change your licence");
        clickByLinkText("change your licence");
        waitForTextToBePresent("Applying to change a licence");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForPageLoad();

        Wait<WebDriver> wait = new FluentWait<WebDriver>(navigate())
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class);

        ExpectedCondition<Boolean> expect = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    return Browser.navigate().getCurrentUrl().contains("variation");
                } catch (Exception e) {
                    return false;
                }
            }
        };

        wait.until(expect);
        try {
            assertTrue(Browser.navigate().getCurrentUrl().contains("variation"));
        } catch (Exception e) {
            System.out.println("Page URL doesn't contain variation and therefore isn't storing the variationNumber.");
        }

        String url = navigate().getCurrentUrl();
        world.updateLicence.setVariationApplicationNumber(returnNthNumberSequenceInString(url, 2));
    }

    public void addTransportManagerOnTMPage() throws IllegalBrowserException, MalformedURLException, InterruptedException {
        waitForTextToBePresent("Add Transport Manager");
        click("//*[@id='add']", SelectorType.XPATH);
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, 1);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
        String url = navigate().getCurrentUrl();
        String applicationNumber = GenericUtils.returnNthNumberSequenceInString(url, 2);
        world.createLicence.setApplicationNumber(applicationNumber);
        addTransportManagerDetails();
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Revoked, curtailed or suspended Licences");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void removeFirstVehicleOnVehiclePage() throws IllegalBrowserException, MalformedURLException {
        navigate().findElements(By.xpath("//tbody//input[@type='checkbox']")).stream().findFirst().get().click();
        navigate().findElements(By.xpath("//tbody//input[@type='submit'][@value='Remove']")).stream().findFirst().get().click();
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addNewOperatingCentreSelfServe(String postcode, int vehicles, int trailers) throws IllegalBrowserException, MalformedURLException {
        waitForTextToBePresent("Operating centres");
        click("//*[@id='add']", SelectorType.XPATH);
        enterText("//*[@id='postcodeInput1']", postcode, SelectorType.XPATH);
        click("//*[@id='address[searchPostcode][search]']", SelectorType.XPATH);
        waitForElementToBeClickable("//*[@id='address[searchPostcode][addresses]']", SelectorType.XPATH);
        selectValueFromDropDownByIndex("//*[@id='address[searchPostcode][addresses]']", SelectorType.XPATH, 1);
        waitForElementToBeClickable("//*[@id='addressLine1']", SelectorType.XPATH);
        enterText("//*[@id='noOfVehiclesRequired']", Integer.toString(vehicles), SelectorType.XPATH);
        enterText("//*[@id='noOfTrailersRequired']", Integer.toString(trailers), SelectorType.XPATH);
        click("//*[@id='permission']", SelectorType.XPATH);
        click("//*[@value='adPlacedLater']", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Operating centre added");
        replaceText("//*[@id='totAuthVehicles']", Integer.toString(vehicles));
        replaceText("//*[@id='totAuthTrailers']", Integer.toString(vehicles));
        click("//*[@id='form-actions[save]']", SelectorType.XPATH);
    }

    public void skipToMainContentAndCheck() throws MalformedURLException, IllegalBrowserException {
        navigate().findElement(By.xpath("//body")).sendKeys(Keys.TAB);
        navigate().switchTo().activeElement().sendKeys(Keys.RETURN);
        navigate().findElement(By.xpath("//body")).sendKeys(Keys.TAB);
        WebElement currentElement = navigate().switchTo().activeElement();
        while (!currentElement.getTagName().equals("main")) {
            currentElement = currentElement.findElement(By.xpath(".//.."));
        }
    }

    public void uploadDocument(String filePath) throws IllegalBrowserException, MalformedURLException {
        click("//*[@id='upload']", SelectorType.XPATH);
        waitForTextToBePresent("Upload document");
        waitAndEnterText("//*[@id='details[description]']", SelectorType.XPATH,"distinctiveName");
        selectValueFromDropDownByIndex("//*[@id='documentSubCategory']", SelectorType.XPATH, 1);
        navigate().findElement(By.xpath("//*[@id='details[file]']")).sendKeys(filePath);
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForElementToBeClickable("//*[@id='upload']", SelectorType.XPATH);
        assertTrue(isElementPresent("//a[contains(text(),'distinctiveName')]",SelectorType.XPATH));
    }
}