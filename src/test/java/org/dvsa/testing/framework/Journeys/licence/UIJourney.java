package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.MissingRequiredArgument;
import activesupport.dates.Dates;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import apiCalls.enums.LicenceType;
import apiCalls.enums.VehicleType;
import autoitx4java.AutoItX;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.hooks.VFTLifeCycle;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.IOException;
import java.time.Duration;

import java.util.*;

import static activesupport.autoITX.AutoITX.initiateAutoItX;
import static activesupport.driver.Browser.navigate;
import static activesupport.msWindowsHandles.MSWindowsHandles.focusWindows;
import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.returnNthNumberSequenceInString;


public class UIJourney extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(UIJourney.class);
    private static World world;
    private FakerUtils faker = new FakerUtils();
    String uploadLaterRadioButton = "//input[@id='uploadLaterRadio']";
    String saveButton = "//*[@id='form-actions[save]']";

    public UIJourney(World world) {
        this.world = world;
    }

    public static void refreshPageWithJavascript() {
        javaScriptExecutor("location.reload(true)");
    }

    public void searchAndSelectAddress(String addressSelector, String postcode, int index) {
        enterText(addressSelector, SelectorType.ID, postcode);
        click("address[searchPostcode][search]", SelectorType.ID);
        waitForElementToBeClickable("address[searchPostcode][addresses]", SelectorType.NAME);
        waitAndSelectByIndex("address[searchPostcode][addresses]", SelectorType.NAME, index);
        waitForPageLoad();
    }

    public void addNewOperator(String applicationID, boolean existingApplication) {
        enterText("username", SelectorType.ID, world.DataGenerator.getOperatorUser());
        enterText("forename", SelectorType.ID, faker.generateFirstName());
        enterText("familyName", SelectorType.ID, faker.generateLastName());
        enterText("fields[emailAddress]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        enterText("fields[emailConfirm]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        if (existingApplication) {
            findSelectAllRadioButtonsByValue("Y");
            enterText("fields[licenceNumber]", SelectorType.ID, applicationID);
        } else {
            findSelectAllRadioButtonsByValue("N");
            enterText("fields[organisationName]", SelectorType.ID, faker.generateCompanyName());
            waitAndClick("//*[contains(text(),'Limited')]", SelectorType.XPATH);
        }
        click("termsAgreed", SelectorType.ID);
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }

    public void addNewInternalUser() {
        selectValueFromDropDown("search-select", SelectorType.ID, "Users");
        enterText("search", SelectorType.NAME, "");
        waitAndClick("//input[@name='submit']", SelectorType.XPATH);
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDown("userType[userType]", SelectorType.NAME, "Internal");
        selectValueFromDropDown("userType[team]", SelectorType.NAME, "VOL Development team");
        selectValueFromDropDown("userType[role]", SelectorType.NAME, "Internal - Admin");
        waitAndEnterText("forename", SelectorType.ID, world.DataGenerator.getOperatorForeName());
        waitAndEnterText("familyName", SelectorType.ID, world.DataGenerator.getOperatorFamilyName());
        waitAndEnterText("userContactDetails[emailAddress]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        waitAndEnterText("userContactDetails[emailConfirm]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        waitAndEnterText("username", SelectorType.ID, world.DataGenerator.getOperatorUser());
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("Created record");
    }

    public void CheckSkipToMainContentOnExternalUserLogin() throws MissingRequiredArgument {
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env).toString();

        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
        }
        navigate().get(myURL);
        skipToMainContentAndCheck();
    }

    public void generateLetter() {
        clickByLinkText("Docs & attachments");
        waitForTextToBePresent("New Letter");
        clickById("New letter");
        waitForTextToBePresent("Generate letter");
        waitAndSelectByIndex( "//*[@id='category']", SelectorType.XPATH, 1);
        waitAndSelectByIndex("//*[@id='documentSubCategory']", SelectorType.XPATH, 1);
        waitAndSelectByIndex( "//*[@id='documentTemplate']", SelectorType.XPATH, 1);
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("Amend letter");
    }

    public void saveDocumentInInternal() {
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitAndClick("//*[@id='close']", SelectorType.XPATH);
        waitForTextToBePresent("The document has been saved");
    }

    public void editDocumentWithWebDav() throws IOException, InterruptedException {
        // Forgive us for using sleeps. There's no other way as this is not a window that selenium can recognise.
        String window = "Olcs - ".concat(world.applicationDetails.getLicenceNumber()).concat(" - Google Chrome");
        String wordLoginWindow = StringUtils.removeEnd(URL.build(ApplicationType.INTERNAL, world.configuration.env).toString(), "/");

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
            autoIt.send(world.updateLicence.getInternalUserLogin());
            autoIt.mouseClick("left", 1100, 720, 2, 1);
            autoIt.send(world.globalMethods.getLoginPassword());
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

    public void deleteLicenceDocument() {
        clickByLinkText("Docs & attachments");
        deleteDocument();
    }

    public void deleteDocument() {
        waitAndClick("//input[@name='id[]']", SelectorType.XPATH);
        waitAndClick("//button[@id='delete']", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to remove the selected record(s)?");
        waitAndClick("//button[@id='form-actions[confirm]']", SelectorType.XPATH);
    }

    public void signWithVerify() {
        String verifyUsername = world.configuration.config.getString("verifyUsername");
        String verifyPassword = world.configuration.config.getString("verifyPassword");

        if (isTitlePresent("Sign in with GOV.UK Verify", 3)) {
            click("//*[@id='start_form_selection_false']", SelectorType.XPATH);
            click("//*[@id='next-button']", SelectorType.XPATH);
            click("//*[contains(text(),'Select Post')]", SelectorType.XPATH);
        }
        if (isTextPresent("The last company selected on this device was Post Office Stub.")) {
            waitAndClick("//*[@value='Post Office Stub']", SelectorType.XPATH);
        }
        if (getDriver().getCurrentUrl().contains("login")) {
            enterText("username", SelectorType.NAME, verifyUsername);
            enterText("password", SelectorType.NAME, verifyPassword);
            while (size("//*[contains(text(),'Verified ID Login')]", SelectorType.XPATH) > 0) {
                click("//*[@value='SignIn']", SelectorType.XPATH);
            }
            waitForTextToBePresent("Personal Details");
            click("//*[@id='agree']", SelectorType.XPATH);
        }
    }

    public void resettingExternalPassword() {
        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
        }
        world.selfServeNavigation.navigateToLoginPage();
        clickByLinkText("Forgotten your password?");
    }

    public void addUser() {
        clickByLinkText("Manage");
        click("//*[@id='addUser']", SelectorType.XPATH);
        enterText("username", SelectorType.ID, world.DataGenerator.getOperatorUser());
        enterText("forename", SelectorType.ID, world.DataGenerator.getOperatorForeName());
        enterText("familyName", SelectorType.ID, world.DataGenerator.getOperatorFamilyName());
        enterText("main[emailAddress]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        enterText("main[emailConfirm]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void completeFinancialEvidencePage() {
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.FINANCIAL_EVIDENCE);
        click(uploadLaterRadioButton, SelectorType.XPATH);
        clickSaveAndReturn();
    }

    public void signDeclaration() {
        waitAndClick("//*[contains(text(),'Sign your declaration online')]", SelectorType.XPATH);
        if (isTitlePresent("Review and declarations", 10)) {
            click("//*[@name='form-actions[sign]']", SelectorType.XPATH);
        } else if (isTitlePresent("Declaration", 10)) {
            click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
        }
    }

    public void signDeclarationForVariation() {
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.REVIEW_AND_DECLARATIONS);
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        if (size("//*[@id='submitAndPay']", SelectorType.XPATH) != 0) {
            click("//*[@id='submitAndPay']", SelectorType.XPATH);
        } else if (size("//*[@id='submit']", SelectorType.XPATH) != 0)
            click("//*[@id='submit']", SelectorType.XPATH);
    }

    public void signManually() {
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

    public void checkLicenceStatus(String arg0) {
        waitForElementToBeClickable("menu-admin-dashboard/admin-your-account/details", SelectorType.ID);
        waitForTextToBePresent("Licence details");
        Assertions.assertEquals(getText("//*[contains(@class,'status')]", SelectorType.XPATH), arg0.toUpperCase());
    }

    public void closeCase() {
        clickByLinkText("" + world.updateLicence.getCaseId() + "");

        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        String casePath = String.format("/case/details/%s", world.updateLicence.getCaseId());
        navigate().get(myURL.concat(casePath));
        clickByLinkText("Close");
        waitForTextToBePresent("Close the case");
        click("form-actions[confirm]", SelectorType.ID);
    }

    public void payForInterimApp() {
        clickByLinkText("Financial");
        waitAndClick("//*[contains(text(),'Send')]", SelectorType.XPATH);
        clickSaveAndReturn();
        clickByLinkText("Review");
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        enterText("interim[goodsApplicationInterimReason]", SelectorType.NAME, "Testing");
        click("submitAndPay", SelectorType.ID);
        click("//*[@name='form-actions[pay]']", SelectorType.XPATH);
        world.feeAndPaymentJourney.customerPaymentModule();
    }

    public void addNewOperatingCentre() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getLicence();
        clickByLinkText("Operating centres and authorisation");
        click("//*[@id='add']", SelectorType.XPATH);
        searchAndSelectAddress("postcodeInput1", "FK10 1AA", 1);
        waitForTextToBePresent("Total number of vehicles");
        assertTrue(isElementPresent("//*[@id='noOfVehiclesRequired']", SelectorType.XPATH));
        waitAndEnterText("noOfVehiclesRequired", SelectorType.ID, "1");
        findSelectAllRadioButtonsByValue("adPlaced");
        click("form-actions[submit]", SelectorType.ID);
    }

    public void caseWorkerCompleteConditionsAndUndertakings() {
        clickByLinkText("Conditions and undertakings");
        clickSaveAndContinue();
    }

    public void caseWorkerCompleteReviewAndDeclarations() {
        clickByLinkText("Review and declarations");
        waitAndClick("//*[@id='declarations[declarationConfirmation]']", SelectorType.XPATH);
        clickSaveAndContinue();
    }

    public void caseWorkerCompleteOverview() {
        click("//*[@id='details[overrideOppositionDate]']", SelectorType.XPATH);
        navigate().findElements(By.xpath("//*[contains(@id,'tracking')]/option[2]")).stream().forEach(WebElement::click);
        click("//*[contains(@id,'form-actions[save')]", SelectorType.XPATH);
    }

    public void createPublicInquiry() {
        click("//*[@id='menu-case_hearings_appeals']", SelectorType.XPATH);
        clickByLinkText("Add Public Inquiry");
        waitForTextToBePresent("Add Traffic Commissioner agreement and legislation");
        HashMap<String, String> agreedDate = new Dates(LocalDate::new).getDateHashMap(0, 0, -7);
        enterDateFieldsByPartialId("fields[agreedDate]", agreedDate);
        selectValueFromDropDownByIndex("fields[agreedByTc]", SelectorType.ID, 1);
        selectValueFromDropDown("//*[@id='fields[agreedByTcRole]']", SelectorType.XPATH, "Traffic Commissioner");
        selectValueFromDropDownByIndex("assignedCaseworker", SelectorType.ID, 1);
        click("//*[@id='fields_piTypes__chosen']/ul", SelectorType.XPATH);
        selectFirstValueInList("//*[@id='fields_piTypes__chosen']/ul");
        click("//*[@id='fields_piTypes__chosen']/div/ul/li[1]", SelectorType.XPATH);
        selectFirstValueInList("//*[@id='fields_reasons__chosen']/ul/li/input");
        click("//*[@id='fields_reasons__chosen']/div/ul/li[2]", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addAndPublishHearing() {
        waitForTextToBePresent("Add hearing");
        clickByLinkText("Add hearing");
        waitForTextToBePresent("Venue");
        selectValueFromDropDown("//*[@id='venue']", SelectorType.XPATH, "Other");
        enterText("//*[@id='venueOther']", SelectorType.XPATH, "Test");
        HashMap<String, String> hearingDate = new Dates(LocalDate::new).getDateHashMap(0, 0, -7);
        enterDateFieldsByPartialId("hearingDate", hearingDate);
        selectValueFromDropDown("//*[@id='hearingDate_hour']", SelectorType.XPATH, "16");
        selectValueFromDropDown("//*[@id='hearingDate_minute']", SelectorType.XPATH, "00");
        selectValueFromDropDown("//*[@id='presidingTc']", SelectorType.XPATH, "Nick Jones");
        selectValueFromDropDown("//*[@id='presidedByRole']", SelectorType.XPATH, "Traffic Commissioner");
        enterText("//*[@id='fields[witnesses]']", SelectorType.XPATH, "1");
        enterText("//*[@id='fields[drivers]']", SelectorType.XPATH, "1");
        click("//*[@id='form-actions[publish]']", SelectorType.XPATH);
    }

    public void deleteCaseNote() {
        clickByLinkText("Processing");
        clickByLinkText("Notes");
        click("//*[@name='id']", SelectorType.XPATH);
        click("//*[@id='delete']", SelectorType.XPATH);
        waitForTextToBePresent("Delete record");
        click("//*[@id='form-actions[confirm]']", SelectorType.XPATH);
    }

    public void navigateToChangeHistory() {
        clickByLinkText("Processing");
        waitForTextToBePresent("Tasks");
        clickByLinkText("Change history");
        waitForTextToBePresent("Details");
    }

    public void createCaseUI(String target) {
        switch (target.toLowerCase()) {
            case "licence":
                world.internalNavigation.getLicence();
                break;
            case "application":
                world.internalNavigation.getApplication();
                break;
            case "variation":
                world.internalNavigation.getVariationApplication();
                break;
        }
        if (getText("//*/span[contains(@class,'status')]", SelectorType.XPATH).equals("UNDER CONSIDERATION")) {
            waitAndClick("//*[@id='menu-application_case']", SelectorType.XPATH);
        } else if (getText("//*/span[contains(@class,'status')]", SelectorType.XPATH).equals("VALID")) {
            waitAndClick("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        }
        click("//*[@id='add']", SelectorType.XPATH);
        waitAndClick("//*[@id='fields_categorys__chosen']/ul", SelectorType.XPATH);
        click("//li[contains(text(),'Convictions')]", SelectorType.XPATH);
        enterText("//*[@id='fields[description]']", SelectorType.XPATH, "testing");
        enterText("//*[@id='fields[ecmsNo]']", SelectorType.XPATH, "12345");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void changeLicenceForVariation() {
        refreshPageWithJavascript();
        waitForPageLoad();
        waitForElementToBePresent("//*[contains(text(),'change your licence')]");
        waitAndClick("//*[contains(text(),'change your licence')]", SelectorType.XPATH);
        waitForTextToBePresent("Applying to change a licence");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForPageLoad();

        Wait<WebDriver> wait = new FluentWait<>(navigate())
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class);

        ExpectedCondition<Boolean> expect = driver -> {
            try {
                return Browser.navigate().getCurrentUrl().contains("variation");
            } catch (Exception e) {
                return false;
            }
        };

        wait.until(WebDriver ->
                expect);
        try {
            assertTrue(Browser.navigate().getCurrentUrl().contains("variation"));
        } catch (Exception e) {
            LOGGER.info("Page URL doesn't contain variation and therefore isn't storing the variationNumber.");
        }

        String url = navigate().getCurrentUrl();
        world.updateLicence.setVariationApplicationId(returnNthNumberSequenceInString(url, 2));
    }

    public void removeFirstVehicleOnVehiclePage() {
        navigate().findElements(By.xpath("//tbody//input[@type='checkbox']")).stream().findFirst().get().click();
        navigate().findElements(By.xpath("//tbody//input[@type='submit'][@value='Remove']")).stream().findFirst().get().click();
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addNewAddressDetails(HashMap<String, String> address, String postcodeMatchingTrafficArea, String typeOfAddress) {
        String[] addressFields = {"addressLine1", "addressLine2", "addressLine3", "addressLine4", "town"};
        for (String addressField : addressFields)
            replaceText(String.format("//*[contains(@name,'%s[%s]')]", typeOfAddress, addressField), SelectorType.XPATH, address.get(addressField));
        replaceText(String.format("//*[contains(@name,'%s[postcode]')]", typeOfAddress), SelectorType.XPATH, postcodeMatchingTrafficArea);
    }

    public void checkAddressDetails(HashMap<String, String> address, String postcode, String typeOfAddress) {
        checkValue(String.format("//*[@name='%s[addressLine1]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine1"));
        checkValue(String.format("//*[@name='%s[addressLine2]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine2"));
        checkValue(String.format("//*[@name='%s[addressLine3]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine3"));
        checkValue(String.format("//*[@name='%s[addressLine4]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine4"));
        checkValue(String.format("//*[@name='%s[town]']", typeOfAddress), SelectorType.XPATH, address.get("town"));
        checkValue(String.format("//*[@name='%s[postcode]']", typeOfAddress), SelectorType.XPATH, postcode);
    }

    public void skipToMainContentAndCheck() {
        navigate().findElement(By.xpath("//body")).sendKeys(Keys.TAB);
        navigate().switchTo().activeElement().sendKeys(Keys.RETURN);
        navigate().findElement(By.xpath("//body")).sendKeys(Keys.TAB);
        WebElement currentElement = navigate().switchTo().activeElement();
        while (!currentElement.getTagName().equals("main")) {
            currentElement = currentElement.findElement(By.xpath(".//.."));
        }
    }

    public void uploadDocument(String filePath) {
        click("//*[@id='upload']", SelectorType.XPATH);
        waitForTextToBePresent("Upload document");
        waitAndEnterText("//*[@id='details[description]']", SelectorType.XPATH, "distinctiveName");
        selectValueFromDropDownByIndex("//*[@id='documentSubCategory']", SelectorType.XPATH, 1);
        navigate().findElement(By.xpath("//*[@id='details[file]']")).sendKeys(filePath);
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForElementToBeClickable("//*[@id='upload']", SelectorType.XPATH);
        assertTrue(isElementPresent("//a[contains(text(),'distinctiveName')]", SelectorType.XPATH));
    }

    public void addAVehicle(String licenceNumber) {
        findSelectAllRadioButtonsByValue("add");
        waitAndClick("next", SelectorType.ID);
        waitAndEnterText("vehicle-search[search-value]", SelectorType.ID, licenceNumber);
        waitAndClick("vehicle-search[submit]", SelectorType.ID);
    }

    public void removeVehicle() {
        findSelectAllRadioButtonsByValue("remove");
        waitAndClick("next", SelectorType.ID);
    }

    public void vehicleRemovalConfirmationPage() {
        removeVehicle();
        waitAndClick("//*[@name='table[id][]'][1]", SelectorType.XPATH);
        waitAndClick("action-button", SelectorType.ID);
    }

    public void createAndSubmitSubmission() {
        click("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        clickByLinkText(Integer.toString(world.updateLicence.getCaseId()));
        clickByLinkText("Submissions");
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDownByIndex("fields[submissionSections][submissionType]", SelectorType.NAME, 1);
        waitAndClick("form-actions[submit]", SelectorType.NAME);
    }

    public List<WebElement> getTableBodyRowList() {
        return findElements("//tbody", SelectorType.XPATH);
    }

    public void checkValue(String selector, SelectorType selectorType, String text) {
        assertEquals(getValue(selector, selectorType), text);
    }

    public void grantApplicationUnderDelegatedAuthority() {
        waitAndClick("//*[@id='menu-application-decisions-grant']", SelectorType.XPATH);
        waitAndClick("//input[@id='grant-authority']", SelectorType.XPATH);
        waitAndClick("//button[@id='form-actions[continue-to-grant]']", SelectorType.XPATH);
        if (isElementPresent("//*[@id='inspection-request-confirm[createInspectionRequest]']", SelectorType.XPATH))
            waitAndClick("//*[@id='inspection-request-confirm[createInspectionRequest]']", SelectorType.XPATH);
        click("//*[@id='form-actions[grant]']", SelectorType.XPATH);
    }

    public void createVariationInInternal(boolean variationFeeRequired) {
        String variationFeeDecision = variationFeeRequired ? "Yes" : "No";
        waitAndClick("//*[@id='menu-licence-quick-actions-create-variation']", SelectorType.XPATH);
        waitForTextToBePresent("Applying to change a licence");
        waitAndClick(String.format("//*[contains(text(),'%s')]", variationFeeDecision), SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Phone')]", SelectorType.XPATH);
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("Variation details");
        String url = navigate().getCurrentUrl();
        world.updateLicence.setVariationApplicationId(returnNthNumberSequenceInString(url, 2));
    }

    public static void clickSaveAndContinue() {
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void clickSubmit() {
        click("form-actions[submit]", SelectorType.NAME);
    }

    public static void clickSaveAndReturn() {
        waitAndClick("//*[@id='form-actions[save]']", SelectorType.XPATH);
    }

    public void clickCancel() {
        waitAndClick("form-actions[cancel]", SelectorType.NAME);
    }

    public void clickOk() {
        waitAndClick("//*[@id='form-actions[ok]']", SelectorType.XPATH);
    }

    public static void inputLicenceAndVehicleType(String licenceType, String vehicleType, String lgvUndertaking) {
        if (isElementPresent("//input[@value='" + LicenceType.valueOf(licenceType.toUpperCase()).asString() + "']", SelectorType.XPATH)) {
            clickByXPath("//input[@value='" + LicenceType.valueOf(licenceType.toUpperCase()).asString() + "']");
        } else {
            clickByXPath("//label[@value='" + LicenceType.valueOf(licenceType.toUpperCase()).asString() + "']");
        }

        if (licenceType.equals("standard_international")) {
            if (!"no_selection".equals(vehicleType)) {
                clickByXPath("//input[@value='" + VehicleType.valueOf(vehicleType.toUpperCase()).asString() + "']");
                if (lgvUndertaking.equals("checked")) {
                    clickByXPath(world.typeOfLicence.lgvDeclarationCheckbox);
                }
            }
        }
        clickSaveAndContinue();
    }
}