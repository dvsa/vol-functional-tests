package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.dates.Dates;
import activesupport.faker.FakerUtils;
import activesupport.string.Str;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConvictionsAndPenaltiesJourney extends BasePage {

    private World world;
    FakerUtils faker = new FakerUtils();
    private String convictionDescription;
    private String convictionCountBeforeInvalidSubmission;

    public void generateConvictionDescription() {
        convictionDescription = world.createApplication.getCompanyNumber();
    }

    public String getConvictionDescription() {
        return convictionDescription;
    }

    public ConvictionsAndPenaltiesJourney(World world) {
        this.world = world;
    }

    public void answerNoToAllQuestionsAndSubmit(String applicationType) {
        waitForTitleToBePresent("Convictions and Penalties");
        findSelectAllRadioButtonsByValue("N");
        clickById("convictionsConfirmation[convictionsConfirmation]");
        if (applicationType.equals("application")) {
            UniversalActions.clickSaveAndContinue();
        } else if (applicationType.equals("variation")) {
            UniversalActions.clickSaveAndReturn();
        }
    }

    public void answerYesToAllQuestionsAndSubmit() {
        waitForTitleToBePresent("Convictions and Penalties");
        findSelectAllRadioButtonsByValue("Y");
        clickById("add");
        addPreviousConviction();
        waitForTitleToBePresent("Convictions and Penalties");
        waitAndClick("convictionsConfirmation[convictionsConfirmation]", SelectorType.ID);
        UniversalActions.clickSaveAndContinue();
    }

    public void addPreviousConviction() {
        HashMap<String, String> dates;
        waitForTitleToBePresent("Add previous conviction");
        selectValueFromDropDown("data[title]", SelectorType.ID, "Mr");
        waitAndEnterText("data[forename]", SelectorType.ID, faker.generateFirstName());
        waitAndEnterText("data[familyName]", SelectorType.ID, faker.generateFirstName());
        waitAndEnterText("data[categoryText]", SelectorType.ID, faker.generateNatureOfBusiness());
        waitAndEnterText("data[notes]", SelectorType.ID, Str.randomWord(30));
        waitAndEnterText("data[courtFpn]", SelectorType.ID, Str.randomWord(10));
        waitAndEnterText("data[penalty]", SelectorType.ID, Str.randomWord(10));

        dates = world.globalMethods.date.getDateHashMap(-5, 0, -20);
        enterText("data[convictionDate][day]", SelectorType.NAME, dates.get("day"));
        enterText("data[convictionDate][month]", SelectorType.NAME, dates.get("month"));
        enterText("data[convictionDate][year]", SelectorType.NAME, dates.get("year"));
        UniversalActions.clickSubmit();
    }

    public void navigateToDeclarations(String type) {
        world.selfServeNavigation.navigateToPage(type, SelfServeSection.CONVICTIONS_AND_PENALTIES);
    }

    public void submitEmptyConviction() {
        convictionCountBeforeInvalidSubmission = getValue("data[table][rows]", SelectorType.ID);
        waitAndClick("//label[@for=//input[@name='data[question]' and @value='Y']/@id]", SelectorType.XPATH);
        waitAndClick("add", SelectorType.ID);
        waitForTitleToBePresent("Add previous conviction");
        UniversalActions.clickSubmit();
        waitForElementToBePresent("//*[@id='validationSummary']");
    }

    public void verifyConvictionRequiredFields(String context, Map<String, String> expectedErrors) {
        assertTrue(getCurrentUrl().contains("/" + context + "/")
                        && getCurrentUrl().contains("/convictions-penalties/add/"),
                "An empty conviction must not be saved");
        assertTrue(isElementPresent("lva-previous-conviction", SelectorType.ID),
                "The conviction entry form should remain open");
        assertEquals(expectedErrors.size(),
                findElements("//*[@id='validationSummary']//li/a", SelectorType.XPATH).size(),
                "The summary should contain each required-field error");
        expectedErrors.forEach((field, expectedError) -> {
            assertEquals(expectedError, getText("//*[@id='validationSummary']//a[@href='#" + field + "']",
                    SelectorType.XPATH), "Summary error for " + field);
            String inlineError = field.equals("dob_day")
                    ? "//*[@id='dob_day']/ancestor::fieldset[contains(concat(' ',normalize-space(@class),' '),' date ')]"
                            + "/p[contains(@class,'govuk-error-message')]"
                    : "//*[@id='" + field + "']/preceding-sibling::p[contains(@class,'govuk-error-message')]";
            assertEquals(expectedError, getText(inlineError, SelectorType.XPATH).replace("Error:", "").trim(),
                    "Inline error for " + field);
        });
    }

    public void cancelConvictionEntry() {
        UniversalActions.clickCancel();
        waitForTitleToBePresent("Convictions and Penalties");
        refreshPage();
    }

    public void verifyNoConvictionWasAdded() {
        assertNotNull(convictionCountBeforeInvalidSubmission, "The original conviction count must be available");
        assertEquals(convictionCountBeforeInvalidSubmission, getValue("data[table][rows]", SelectorType.ID),
                "Submitting an empty conviction should not add a record");
    }

    public void addApplicationConviction(Map<String, String> details) {
        waitAndClick("//label[@for=//input[@name='data[question]' and @value='Y']/@id]", SelectorType.XPATH);
        waitAndClick("add", SelectorType.ID);
        waitForTitleToBePresent("Add previous conviction");
        selectValueFromDropDown("data[title]", SelectorType.ID, details.get("title"));
        waitAndEnterText("data[forename]", SelectorType.ID, details.get("forename"));
        waitAndEnterText("data[familyName]", SelectorType.ID, details.get("familyName"));
        waitAndEnterText("data[categoryText]", SelectorType.ID, details.get("offence"));
        waitAndEnterText("data[notes]", SelectorType.ID, details.get("details"));
        waitAndEnterText("data[courtFpn]", SelectorType.ID, details.get("court"));
        waitAndEnterText("data[penalty]", SelectorType.ID, details.get("penalty"));
        enterText("data[convictionDate][day]", SelectorType.NAME, details.get("day"));
        enterText("data[convictionDate][month]", SelectorType.NAME, details.get("month"));
        enterText("data[convictionDate][year]", SelectorType.NAME, details.get("year"));
        UniversalActions.clickSubmit();
        waitForTitleToBePresent("Convictions and Penalties");
    }

    public void saveDeclarations() {
        if (!findElement("convictionsConfirmation[convictionsConfirmation]", SelectorType.ID).isSelected()) {
            waitAndClick("convictionsConfirmation[convictionsConfirmation]", SelectorType.ID);
        }
        UniversalActions.clickSaveAndReturn();
    }

    public void verifySavedConviction(String type, Map<String, String> details) {
        UniversalActions.clickHome();
        navigateToDeclarations(type);
        String row = "//tbody//tr[.//button[contains(@name,'table[action][edit]')"
                + " and contains(normalize-space(),'" + details.get("forename") + " " + details.get("familyName") + "')]]";
        String rowText = waitAndGetText(row, SelectorType.XPATH);
        assertEquals(1, findElements(row, SelectorType.XPATH).size(), "Exactly one matching conviction should be listed");
        String date = details.get("day") + "/" + details.get("month") + "/" + details.get("year");
        for (String value : new String[]{details.get("offence"), details.get("court"), details.get("penalty"), date}) {
            assertTrue(rowText.contains(value), "Saved conviction should include " + value);
        }
        assertTrue(findElement("//input[@name='data[question]' and @value='Y']", SelectorType.XPATH).isSelected(),
                "The conviction declaration should remain Yes");
    }

    public void editApplicationConviction(String name, String offence) {
        waitAndClick("//tbody//button[contains(@name,'table[action][edit]') and contains(normalize-space(),'"
                + name + "')]", SelectorType.XPATH);
        replaceText("data[categoryText]", SelectorType.ID, offence);
        UniversalActions.clickSubmit();
        waitForTitleToBePresent("Convictions and Penalties");
    }

    public void removeApplicationConviction(String name) {
        String row = "//tbody//tr[.//button[contains(@name,'table[action][edit]') and contains(normalize-space(),'"
                + name + "')]]";
        waitAndClick(row + "//button[contains(@name,'table[action][delete]')]", SelectorType.XPATH);
        waitAndClick("//form[@id='generic-delete-confirmation']//button[@name='form-actions[submit]']", SelectorType.XPATH);
        waitForElementNotToBePresent("//form[@id='generic-delete-confirmation']");
        waitForElementNotToBePresent(row);
    }

    public void verifyApplicationConvictionRemoved(String name) {
        refreshPage();
        assertFalse(isElementPresent("//tbody//button[contains(@name,'table[action][edit]') and contains(normalize-space(),'"
                + name + "')]", SelectorType.XPATH), "Deleted conviction should not be listed after reload");
    }

    public void declareNoConvictions() {
        waitAndClick("//label[@for=//input[@name='data[question]' and @value='N']/@id]", SelectorType.XPATH);
    }

    public void verifyOffenceEntryHidden() {
        assertFalse(findElement("add", SelectorType.ID).isDisplayed(),
                "The offence entry action should be hidden when No is selected");
    }

    public void verifyNoConvictionsSaved(String type) {
        UniversalActions.clickHome();
        navigateToDeclarations(type);
        assertTrue(findElement("//input[@name='data[question]' and @value='N']", SelectorType.XPATH).isSelected(),
                "The conviction declaration should remain No");
        assertFalse(findElement("add", SelectorType.ID).isDisplayed(),
                "The offence entry action should remain hidden");
    }

    public void addConvictionToCase() {
        generateConvictionDescription();
        waitAndClickByLinkText("Convictions");
        waitAndClick("add", SelectorType.ID);
        waitForElementToBePresent("//select[@id='defendantType']");
        selectValueFromDropDownByIndex("//select[@id='defendantType']", SelectorType.XPATH, 2);
        enterText("//input[@id='fields[personFirstname]']", SelectorType.XPATH, faker.generateFirstName());
        enterText("//input[@id='fields[personLastname]']", SelectorType.XPATH, faker.generateLastName());
        HashMap<String, String> birthDate = new Dates(LocalDate::new).getDateHashMap(0, 0, -20);
        enterDateFieldsByPartialId("fields[birthDate]", birthDate);
        selectValueFromDropDownByIndex("//select[@id='fields[msi]']", SelectorType.XPATH, 2);
        enterText("//textarea[@id='categoryText']", SelectorType.XPATH, convictionDescription);
        HashMap<String, String> offenceDate = new Dates(LocalDate::new).getDateHashMap(0, 0, -1);
        enterDateFieldsByPartialId("fields[offenceDate]", offenceDate);
        HashMap<String, String> convictionDate = new Dates(LocalDate::new).getDateHashMap(0, 0, -1);
        enterDateFieldsByPartialId("fields[convictionDate]", convictionDate);
        selectValueFromDropDownByIndex("//select[@id='fields[msi]']", SelectorType.XPATH, 2);
        selectValueFromDropDownByIndex("//select[@id='fields[isDeclared]']", SelectorType.XPATH, 2);
        UniversalActions.clickSubmit();
    }

    public void addComplaint() {
        waitAndClickByLinkText("Complaints");
        waitAndClick("//button[text()='Add complaint']", SelectorType.XPATH);
        enterText("//input[@id='complainantForename']", SelectorType.XPATH, (faker.generateFirstName()));
        enterText("//input[@id='complainantFamilyName']", SelectorType.XPATH, (faker.generateLastName()));
        HashMap<String, String> complaintDate = new Dates(LocalDate::new).getDateHashMap(0, 0, -1);
        enterDateFieldsByPartialId("complaintDate", complaintDate);
        selectValueFromDropDownByIndex("//select[@id='complaintType']", SelectorType.XPATH, 1);
        selectValueFromDropDownByIndex("//select[@id='status']", SelectorType.XPATH, 1);
        UniversalActions.clickSubmit();
    }

    public void completConditionUndertakings() {
        generateConvictionDescription();
        waitAndClickByLinkText("Conditions and undertakings");
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDownByIndex("type", SelectorType.ID, 2);
        selectValueFromDropDownByIndex("conditionCategory", SelectorType.ID, 3);
        enterText("notes", SelectorType.ID, convictionDescription);
        click("//*[@id='fields[fulfilled]']", SelectorType.XPATH);
        selectValueFromDropDownByIndex("attachedTo", SelectorType.ID, 2);
        UniversalActions.clickSubmit();
    }

    public void addANote() {
        waitAndClick("add", SelectorType.ID);
        UniversalActions.clickSubmit();
    }

}