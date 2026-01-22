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
import org.dvsa.testing.lib.url.webapp.webAppURL;
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
        waitForElementToBeClickable("address[searchPostcode][addresses]", SelectorType.XPATH);
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
        waitAndClickByLinkText("Docs & attachments");
        waitForTextToBePresent("New Letter");
        clickById("New letter");
        waitForTextToBePresent("Generate letter");
        waitAndSelectByIndex("//*[@id='category']", SelectorType.XPATH, 5);
        waitAndSelectValueFromDropDown("//*[@id='documentSubCategory']", SelectorType.XPATH,  "Other Documents");
        waitAndSelectValueFromDropDown("//*[@id='documentTemplate']", SelectorType.XPATH, "GV - Blank letter to operator");
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Amend letter");
    }

    public void generatePTRLetter() {
        waitAndClickByLinkText("Docs & attachments");
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
        var window = "Olcs - ".concat(world.applicationDetails.getLicenceNumber()).concat(" - Google Chrome");
        var wordLoginWindow = StringUtils.removeEnd(webAppURL.build(ApplicationType.INTERNAL, world.configuration.env).toString(), "/");

        Thread.sleep(1000);
        waitAndClickByLinkText("BUS");
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
        waitAndClickByLinkText("Docs & attachments");
        deleteDocument();
    }

    public void deleteDocument() {
        waitAndClick("//input[@name='id[]']", SelectorType.XPATH);
        waitAndClick("//button[@id='delete']", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to remove the selected record(s)?");
        UniversalActions.clickConfirm();
    }

    public void checkLicenceStatus(String expectedStatus) {
        waitForElementToBeClickable("menu-admin-dashboard/admin-your-account/details", SelectorType.ID);
        waitForTextToBePresent("Licence status");
        String actualStatus = waitAndGetElementValueByText("//strong[contains(@class,'govuk-tag')]", SelectorType.XPATH);
        assertEquals(expectedStatus.toUpperCase(), actualStatus.toUpperCase());
    }

    public void closeCase() {
        waitAndClickByLinkText("" + world.updateLicence.getCaseId() + "");
        var myURL = webAppURL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        var casePath = String.format("case/details/%s", world.updateLicence.getCaseId());
        navigate().get(myURL.concat(casePath));
        waitAndClickByLinkText("Close");
        waitForTextToBePresent("Close the case");
        UniversalActions.clickConfirm();
    }

    public void payForInterimApp() {
        waitAndClickByLinkText("Financial");
        waitAndClick("//*[contains(text(),'Upload documents later')]", SelectorType.XPATH);
        UniversalActions.clickSaveAndReturn();
        waitAndClickByLinkText("Review");
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        enterText("applicationInterimReason", SelectorType.ID, "Testing");
        click("submitAndPay", SelectorType.ID);
        UniversalActions.clickPay();
        world.feeAndPaymentJourney.customerPaymentModule();
    }

    public void createCaseUI(String target) {
        switch (target.toLowerCase()) {
            case "licence" -> world.internalNavigation.getLicence();
            case "application" -> world.internalNavigation.getApplication();
            case "variation" -> world.internalNavigation.getVariationApplication();
        }
        String actualText = getText("//strong[@class='govuk-tag govuk-tag--orange']", SelectorType.XPATH);
        assertTrue(actualText.equalsIgnoreCase("UNDER CONSIDERATION"));
        if (isTextPresent(actualText)) {
            waitAndClick("//*[@id='menu-application_case']", SelectorType.XPATH);
        } else if (getText("//*/span[contains(@class,'status')]", SelectorType.XPATH).equalsIgnoreCase("VALID")) {
            waitAndClick("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        }
        click("//*[@id='add']", SelectorType.XPATH);
        waitAndClick("//*[@id='fields_categorys__chosen']/ul", SelectorType.XPATH);
        click("//li[contains(text(),'Convictions')]", SelectorType.XPATH);
        enterText("//*[@id='fields[description]']", SelectorType.XPATH, "testing");
        enterText("//*[@id='fields[ecmsNo]']", SelectorType.XPATH, "12345");
        UniversalActions.clickSubmit();
    }

    public void addAPrepCase() {
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitAndClick("//*[@id='fields_categorys__chosen']/ul", SelectorType.XPATH);
        selectValueFromDropDownByIndex("fields[caseType]", SelectorType.ID, 1);
        waitAndClick("//*[@id='fields_categorys__chosen']/ul", SelectorType.XPATH);
        waitAndClick("//li[contains(text(),'Convictions')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='fields[description]']", SelectorType.XPATH, "testing");
        waitAndEnterText("//*[@id='fields[ecmsNo]']", SelectorType.XPATH, "12345");
        UniversalActions.clickSubmit();
    }


    public void caseWorkerCompleteConditionsAndUndertakings() {
        waitAndClickByLinkText("Conditions and undertakings");
        UniversalActions.clickSaveAndContinue();
    }

    public void caseWorkerCompleteReviewAndDeclarations() {
        waitAndClickByLinkText("Review and declarations");
        waitAndClick("//*[@id='declarations[declarationConfirmation]']", SelectorType.XPATH);
        UniversalActions.clickSaveAndContinue();
    }

    public void caseWorkerCompleteOverview() {
        waitAndClick("//*[@id='details[overrideOppositionDate]']", SelectorType.XPATH);
        navigate().findElements(By.xpath("//*[contains(@id,'tracking')]/option[2]")).stream().forEach(WebElement::click);
        UniversalActions.clickSaveAndReturn();
    }

    public void createPublicInquiry() {
        click("//*[@id='menu-case_hearings_appeals']", SelectorType.XPATH);
        waitAndClickByLinkText("Add Public Inquiry");
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

        String internalURL = webAppURL.build(ApplicationType.INTERNAL, world.configuration.env, "auth/login").toString();
        get(internalURL);
        world.globalMethods.signIn(user, password);
    }

    public void manualBusRegistration(Integer plusOrMinusDay, Integer plusOrMinusMonth, Integer plusOrMinusYear) {
        world.internalNavigation.getLicence();
        world.busRegistrationJourney.internalSiteAddBusNewReg(plusOrMinusDay,plusOrMinusMonth,plusOrMinusYear);
        refreshPage();
        waitAndClickByLinkText("Register");
        findSelectAllRadioButtonsByValue("Y");
        UniversalActions.clickSubmit();
        refreshPage();
        waitAndClickByLinkText("Service details");
        UniversalActions.clickSubmit();
        refreshPage();
        waitAndClickByLinkText("TA's");
        selectFirstValueFromDropdown("//*[@class='chosen-choices']", "//*[@class='active-result']");
        selectFirstValueFromDropdown("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']",  "//*[@class=\"active-result group-option\"]");
        UniversalActions.clickSubmit();
    }

    public void payFee() {
        refreshPage();
        waitAndClickByLinkText("Fees");
        world.feeAndPaymentJourney.selectFee();

        String feeAmountXpath = "//td[@data-heading='Fee amount']";
        String feeAmountText = getDriver().findElement(By.xpath(feeAmountXpath)).getText();
        String amount = feeAmountText.replace("£", "").trim();

        if (amount.equals("257.00")) {
            world.feeAndPaymentJourney.payFee("257", "cash");
        } else if (amount.equals("60.00")) {
            world.feeAndPaymentJourney.payFee("60", "cash");
        }
    }

    public void addAndPublishHearing() {
        waitForTextToBePresent("Add hearing");
        waitAndClickByLinkText("Add hearing");
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
        waitAndClickByLinkText("Processing");
        waitAndClickByLinkText("Notes");
        click("//*[@name='id']", SelectorType.XPATH);
        click("//*[@id='delete']", SelectorType.XPATH);
        waitForTextToBePresent("Delete record");
        UniversalActions.clickConfirm();
    }

    public void navigateToChangeHistory() {
        waitAndClickByLinkText("Processing");
        waitForTextToBePresent("Tasks");
        waitAndClickByLinkText("Change history");
        waitForTextToBePresent("Details");
    }

    public void createVariationInInternal(boolean variationFeeRequired) {
        var variationFeeDecision = variationFeeRequired ? "Yes" : "No";
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
        waitAndClickByLinkText("Print licence");
        waitForTextToBePresent("Licence printed successfully");
        waitAndClickByLinkText("Docs & attachments");
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
        waitAndClickByLinkText("Licences");
        waitForElementToBePresent("//a[@class='govuk-link']");
        click("//a[@class='govuk-link']", SelectorType.XPATH);
    }

    public void enterAddress(String Street, String Town, String PostCode) {
        waitAndClickByLinkText("Enter the address yourself");
        waitAndEnterText("addressLine1", SelectorType.ID, Street);
        waitAndEnterText("addressTown", SelectorType.ID, Town);
        waitAndEnterText("postcode", SelectorType.ID, PostCode);
    }
}

