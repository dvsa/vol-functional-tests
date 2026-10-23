package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PeopleJourney extends BasePage {
    private final World world;

    public PeopleJourney(World world) {
        this.world = world;
    }

    public void addPartnerWithMinimalDetails(String title, String firstName, String lastName, String dateOfBirth) {
        clickAddPartnerButton();
        fillPersonalDetails(title, firstName, lastName, null, dateOfBirth);
        continueToFinancialHistory();
        answerNoToFinancialHistoryAndContinue();
        answerNoToLicenceHistoryAndContinue();
        answerNoToConvictionsAndContinue();
        verifyPersonIsAddedToTable(firstName + " " + lastName);
    }

    private void clickAddPartnerButton() {
        String addButtonXpath = "//button[@name='table[action]' and @value='Add']";
        waitAndClick(addButtonXpath, SelectorType.XPATH);
        waitForElementToBePresent("//h1[contains(text(), 'Add person')]");
    }

    private void fillPersonalDetails(String title, String firstName, String lastName, String otherNames, String dateOfBirth) {
        selectTitle(title);
        clearAndEnter("//input[@id='forename']", SelectorType.XPATH, firstName);
        clearAndEnter("//input[@id='familyname']", SelectorType.XPATH, lastName);

        if (otherNames != null && !otherNames.isEmpty()) {
            clearAndEnter("//input[@id='data[otherName]']", SelectorType.XPATH, otherNames);
        }

        fillDateOfBirth(dateOfBirth);
    }

    private void selectTitle(String title) {
        String titleSelectXpath = "//select[@id='title']";
        String titleValue = "title_" + title.toLowerCase().replace(".", "");
        waitAndClick(titleSelectXpath, SelectorType.XPATH);
        String optionXpath = titleSelectXpath + "//option[@value='" + titleValue + "']";
        waitAndClick(optionXpath, SelectorType.XPATH);
    }

    private void fillDateOfBirth(String dateOfBirth) {
        // dateOfBirth format: "DD MM YYYY" or "21 6 2014"
        String[] parts = dateOfBirth.trim().split("\\s+");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];

        clearAndEnter("//input[@id='dob_day']", SelectorType.XPATH, day);
        clearAndEnter("//input[@id='dob_month']", SelectorType.XPATH, month);
        clearAndEnter("//input[@id='dob_year']", SelectorType.XPATH, year);
    }

    private void continueToFinancialHistory() {
        String continueButtonXpath = "//button[@name='form-actions[saveAndContinue]']";
        waitAndClick(continueButtonXpath, SelectorType.XPATH);
        waitForElementToBePresent("//h1[contains(text(), 'Financial history')]");
    }

    private void answerNoToFinancialHistoryAndContinue() {
        waitForTitleToBePresent("Financial history");
        findSelectAllRadioButtonsByValue("N");
        UniversalActions.clickSaveAndContinue();
    }

    private void answerNoToLicenceHistoryAndContinue() {
        waitForTitleToBePresent("Licence history");
        findSelectAllRadioButtonsByValue("N");
        UniversalActions.clickSaveAndContinue();
    }

    private void answerNoToConvictionsAndContinue() {
        try {
            waitForTitleToBePresent("Convictions");
            findSelectAllRadioButtonsByValue("N");
            UniversalActions.clickSaveAndContinue();
        } catch (Exception e) {
            // May not be on convictions page; proceed to confirmation
        }
    }

    private void verifyPersonIsAddedToTable(String expectedPersonName) {
        waitForElementToBePresent("//h1[contains(text(), 'Partners or members')]");
        waitForElementToBePresent("//table");

        String personRowXpath = "//table//tr[contains(., '" + expectedPersonName + "')]";
        assertTrue(findElements(personRowXpath, SelectorType.XPATH).size() > 0,
                "Person " + expectedPersonName + " should be present in table");
    }

    public void removePartner(String partnerName) {
        String removeButtonXpath = "//table//tr[contains(., '" + partnerName + "')]//button[contains(@class, 'trigger-modal')]";
        waitAndClick(removeButtonXpath, SelectorType.XPATH);

        // Modal appears; confirm removal
        String confirmRemoveXpath = "//button[contains(text(), 'Remove')]";
        waitForElementToBePresent(confirmRemoveXpath);
        waitAndClick(confirmRemoveXpath, SelectorType.XPATH);

        waitForElementToBePresent("//table");
        String removedRowXpath = "//table//tr[contains(., '" + partnerName + "')]";
        assertTrue(findElements(removedRowXpath, SelectorType.XPATH).size() == 0,
                "Partner " + partnerName + " should be removed from table");
    }

    public void verifyPartnerIsPresent(String partnerName) {
        waitForElementToBePresent("//h1[contains(text(), 'Partners or members')]");
        String partnerRowXpath = "//table//tr[contains(., '" + partnerName + "')]";
        assertTrue(findElements(partnerRowXpath, SelectorType.XPATH).size() > 0,
                "Partner " + partnerName + " should be present in table");
    }

    public void verifyPartnerIsNotPresent(String partnerName) {
        waitForElementToBePresent("//table");
        String partnerRowXpath = "//table//tr[contains(., '" + partnerName + "')]";
        assertTrue(findElements(partnerRowXpath, SelectorType.XPATH).size() == 0,
                "Partner " + partnerName + " should not be present in table");
    }
}
