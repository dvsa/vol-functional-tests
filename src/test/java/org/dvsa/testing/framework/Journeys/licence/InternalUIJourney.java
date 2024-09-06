package org.dvsa.testing.framework.Journeys.licence;


import activesupport.aws.s3.SecretsManager;
import activesupport.dates.Dates;
import autoitx4java.AutoItX;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.joda.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.HashMap;

import static activesupport.autoITX.AutoITX.initiateAutoItX;
import static activesupport.driver.Browser.navigate;
import static activesupport.msWindowsHandles.MSWindowsHandles.focusWindows;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.returnNthNumberSequenceInString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InternalUIJourney extends BasePage {
    private final World world;

    public InternalUIJourney(World world) {
        this.world = world;
    }

    public void searchAndSelectAddress(String addressSelector, String postcode, int index) {
        enterText(addressSelector, SelectorType.ID, postcode);
        waitAndClick("address[searchPostcode][search]", SelectorType.ID);
        waitForElementToBeClickable("address[searchPostcode][addresses]", SelectorType.NAME);
        waitForPageLoad();
        waitAndSelectByIndex("address[searchPostcode][addresses]", SelectorType.NAME, index);
    }

    public void addNewOperatingCentre() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        searchAndSelectAddress("postcodeInput1", "FK10 1AA", 1);
        waitForTextToBePresent("Total number of vehicles");
        assertTrue(isElementPresent("//*[@id='noOfVehiclesRequired']", SelectorType.XPATH));
        waitAndEnterText("noOfVehiclesRequired", SelectorType.ID, "1");
        findSelectAllRadioButtonsByValue("adPlaced");
        UniversalActions.clickSubmit();
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
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Created record");
    }

    public void generateLetter() {
        clickByLinkText("Docs & attachments");
        waitForTextToBePresent("New Letter");
        clickById("New letter");
        waitForTextToBePresent("Generate letter");
        waitAndSelectByIndex("//*[@id='category']", SelectorType.XPATH, 5);
        waitAndSelectByIndex("//*[@id='documentSubCategory']", SelectorType.XPATH, 3);
        waitAndSelectValueFromDropDown("//*[@id='documentTemplate']", SelectorType.XPATH, "GV - Blank letter to operator");
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Amend letter");
    }

    public void generatePTRLetter() {
        clickByLinkText("Docs & attachments");
        waitForTextToBePresent("New Letter");
        clickById("New letter");
        waitForTextToBePresent("Generate letter");
        waitAndSelectByIndex("//*[@id='category']", SelectorType.XPATH, 2);
        waitAndSelectByIndex("//*[@id='documentSubCategory']", SelectorType.XPATH, 4);
        waitAndSelectByIndex("//*[@id='documentTemplate']", SelectorType.XPATH, 1);
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Amend letter");
    }

    public void saveDocumentInInternal() {
        UniversalActions.clickSubmit();
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
        UniversalActions.clickConfirm();
    }

    public void checkLicenceStatus(String arg0) {
        waitForElementToBeClickable("menu-admin-dashboard/admin-your-account/details", SelectorType.ID);
        waitForTextToBePresent("Licence status");
        assertEquals(arg0, getElementValueByText("//strong[contains(@class,'govuk-tag')]", SelectorType.XPATH));
    }

    public void closeCase() {
        clickByLinkText("" + world.updateLicence.getCaseId() + "");
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        String casePath = String.format("case/details/%s", world.updateLicence.getCaseId());
        navigate().get(myURL.concat(casePath));
        clickByLinkText("Close");
        waitForTextToBePresent("Close the case");
        UniversalActions.clickConfirm();
    }

    public void payForInterimApp() {
        clickByLinkText("Financial");
        waitAndClick("//*[contains(text(),'Send')]", SelectorType.XPATH);
        UniversalActions.clickSaveAndReturn();
        clickByLinkText("Review");
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        enterText("applicationInterimReason", SelectorType.ID, "Testing");
        click("submitAndPay", SelectorType.ID);
        UniversalActions.clickPay();
        world.feeAndPaymentJourney.customerPaymentModule();
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
        if (getText("//*/strong[contains(@class,'govuk-tag govuk-tag--orange')]", SelectorType.XPATH).equals("UNDER CONSIDERATION")) {
            waitAndClick("//*[@id='menu-application_case']", SelectorType.XPATH);
        } else if (getText("//*/span[contains(@class,'status')]", SelectorType.XPATH).equals("VALID")) {
            waitAndClick("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        }
        click("//*[@id='add']", SelectorType.XPATH);
        waitAndClick("//*[@id='fields_categorys__chosen']/ul", SelectorType.XPATH);
        click("//li[contains(text(),'Convictions')]", SelectorType.XPATH);
        enterText("//*[@id='fields[description]']", SelectorType.XPATH, "testing");
        enterText("//*[@id='fields[ecmsNo]']", SelectorType.XPATH, "12345");
        UniversalActions.clickSubmit();
    }

    public void caseWorkerCompleteConditionsAndUndertakings() {
        clickByLinkText("Conditions and undertakings");
        UniversalActions.clickSaveAndContinue();
    }

    public void caseWorkerCompleteReviewAndDeclarations() {
        clickByLinkText("Review and declarations");
        waitAndClick("//*[@id='declarations[declarationConfirmation]']", SelectorType.XPATH);
        UniversalActions.clickSaveAndContinue();
    }

    public void caseWorkerCompleteOverview() {
        click("//*[@id='details[overrideOppositionDate]']", SelectorType.XPATH);
        navigate().findElements(By.xpath("//*[contains(@id,'tracking')]/option[2]")).stream().forEach(WebElement::click);
        UniversalActions.clickSaveAndReturn();
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
        UniversalActions.clickSubmit();
    }

    public void loginIntoInternalAsExistingAdmin() {
        String user = SecretsManager.getSecretValue("adminUser");
        String password = SecretsManager.getSecretValue("defaultPassword");

        String internalURL = URL.build(ApplicationType.INTERNAL, world.configuration.env, "auth/login").toString();
        get(internalURL);
        world.globalMethods.signIn(user, password);
    }

    public void manualBusRegistration(Integer plusOrMinusDay, Integer plusOrMinusMonth, Integer plusOrMinusYear) {
        world.internalNavigation.getLicence();
        world.busRegistrationJourney.internalSiteAddBusNewReg(plusOrMinusDay,plusOrMinusMonth,plusOrMinusYear);
        clickByLinkText("Register");
        findSelectAllRadioButtonsByValue("Y");
        UniversalActions.clickSubmit();
        clickByLinkText("Service details");
        clickByLinkText("TA's");
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result\"]");
        click("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result group-option\"]");
        UniversalActions.clickSubmit();
    }

    public void payFee() {
        clickByLinkText("Fees");
        world.feeAndPaymentJourney.selectFee();
        world.feeAndPaymentJourney.payFee("60", "cash");
        waitAndClick("//*[contains(text(),'Grant')]", SelectorType.XPATH);
    }

    public void payFeeAndPublishLicence() {
        clickByLinkText("Fees");
        world.feeAndPaymentJourney.selectFee();
        world.feeAndPaymentJourney.payFee("257", "cash");
        waitForTextToBePresent("The payment was made successfully");
        waitForElementToBeClickable("Publish application", SelectorType.LINKTEXT);
        waitAndClick("Publish application", SelectorType.LINKTEXT);
        waitAndClick("//button[text()='Publish']", SelectorType.XPATH);
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
        selectRandomValueFromDropDown("//*[@id='presidingTc']", SelectorType.XPATH);
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
        UniversalActions.clickConfirm();
    }

    public void navigateToChangeHistory() {
        clickByLinkText("Processing");
        waitForTextToBePresent("Tasks");
        clickByLinkText("Change history");
        waitForTextToBePresent("Details");
    }

    public void createVariationInInternal(boolean variationFeeRequired) {
        String variationFeeDecision = variationFeeRequired ? "Yes" : "No";
        waitAndClick("//*[@id='menu-licence-quick-actions-create-variation']", SelectorType.XPATH);
        waitForTextToBePresent("Applying to change a licence");
        waitAndClick(String.format("//*[contains(text(),'%s')]", variationFeeDecision), SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Phone')]", SelectorType.XPATH);
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Variation details");
        UniversalActions.refreshPageWithJavascript();
        String url = navigate().getCurrentUrl();
        world.updateLicence.setVariationApplicationId(returnNthNumberSequenceInString(url, 1));

    }

    public void grantApplicationUnderDelegatedAuthority() {
        waitAndClick("//*[@id='menu-application-decisions-grant']", SelectorType.XPATH);
        waitAndClick("//input[@id='grant-authority']", SelectorType.XPATH);
        waitAndClick("//button[@id='form-actions[continue-to-grant]']", SelectorType.XPATH);
        if (isElementPresent("//*[@id='inspection-request-confirm[createInspectionRequest]']", SelectorType.XPATH))
            waitAndClick("//*[@id='inspection-request-confirm[createInspectionRequest]']", SelectorType.XPATH);
        click("//*[@id='form-actions[grant]']", SelectorType.XPATH);
        waitForTextToBePresent("The application has been granted");
    }

    public void uploadDocument(String filePath) {
        click("//*[@id='upload']", SelectorType.XPATH);
        waitForTextToBePresent("Upload document");
        waitAndEnterText("//*[@id='details[description]']", SelectorType.XPATH, "distinctiveName");
        selectValueFromDropDownByIndex("//*[@id='documentSubCategory']", SelectorType.XPATH, 1);
        navigate().findElement(By.xpath("//*[@id='details[file]']")).sendKeys(filePath);
        UniversalActions.clickSubmit();
        waitForElementToBeClickable("//*[@id='upload']", SelectorType.XPATH);
        assertTrue(isElementPresent("//a[contains(text(),'distinctiveName')]", SelectorType.XPATH));
    }

    public void printLicence() {
        clickByLinkText("Print licence");
        waitForTextToBePresent("Licence printed successfully");
        clickByLinkText("Docs & attachments");
        waitForTextToBePresent("GV Licence");
    }

    public void changeToPostOnOperatorProfile() {
        click("//p[@class='small-module__details']//a[1]", SelectorType.XPATH);
        waitForTextToBePresent("Operator profile");
        click("//input[@type='radio']", SelectorType.XPATH);
        clickById("form-actions[save]");
        waitForTextToBePresent("The operator has been updated successfully");
    }

    public void navigateToLicenceFromOperatorProfile() {
        clickByLinkText("Licences");
        waitForElementToBePresent("//a[@class='govuk-link']");
        click("//a[@class='govuk-link']", SelectorType.XPATH);
    }
}

