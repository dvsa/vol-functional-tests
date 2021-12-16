package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.faker.FakerUtils;
import activesupport.string.Str;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.dvsa.testing.framework.stepdefs.vol.SubmitSelfServeApplication.accessibilityScanner;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectorJourney extends BasePage {

    private World world;
    private FakerUtils faker = new FakerUtils();
    private String directorFirstName;
    private String directorLastName;

    public String directorsTitle = "Directors";
    public String directorLinks = "//tbody/tr/td[1]/input";
    public String addButton = "//button[@name='table[action]']";
    public String directorDetailsTitle = "Add person";
    public String directorVariationDetailsTitle = "Add a director";
    public String directorTitleDropdown = "//select[@id='title']";
    public String firstNameField = "//input[@name='data[forename]']";
    public String lastNameField = "//input[@name='data[familyName]']";
    public String saveAndContinue = "//button[@name='form-actions[saveAndContinue]']";
    public String additionalInformation = "//*[@id='data[insolvencyDetails]']";
    public String deleteDirectorButtons = "//input[contains(@name,'table[action][delete]')]";
    public String deleteDirectorConfirmationTitle = "Are you sure you want to remove this person?";
    public String deleteDirectorConfirmation = "//button[@name='form-actions[submit]']";
    public String lastDirectorRemovedMessage = "Last director removed";

    public String internalDirectorTask = "//a[text()='Add director(s)']";
    public String internalUrgentCheckboxLabel = "//div[4]/label";
    public String GVPeopleChangeSnapshot = "GV81";
    public String PSVPeopleChangeSnapshot = "PSV431";

    public String validationTitle = "There is a problem";
    public String listOfSummaryErrors = "//ol/li/a";
    public String listOfInlineErrors = "//p[@class='error__text']";
    public String titleValidation = "Select an option for: \"Title\"";
    public String firstNameValidation = "Enter first name";
    public String lastNameValidation = "Enter last name";
    public String dateOfBirthEmptyFieldValidation = "Enter date of birth";
    public String dateOfBirthIncorrectValueValidation1 = "The date should be entered in number format";
    public String dateOfBirthIncorrectValueValidation2 = "Please enter all 4 digits of the year";
    public String dateOfBirthIncorrectValueValidation3 = "The input does not appear to be a valid value";
    public String bankruptcyValidation = "Bankruptcy: Choose an option";
    public String liquidationValidation = "Liquidation: Choose an option";
    public String receivershipValidation = "Receivership: Choose an option";
    public String administrationValidation = "Administration: Choose an option";
    public String disqualifiedValidation = "Disqualified: Choose an option";
    public String convictionsAndPenaltiesValidation = "Value is required";


    public DirectorJourney(World world){
        this.world = world;
    }

    public String getDirectorName() {
        return directorFirstName.concat(" ").concat(directorLastName);
    }

    public void addDirectorWithNoFinancialHistoryConvictionsOrPenalties()  {
        click(addButton, SelectorType.XPATH);
        if(isTitlePresent(directorDetailsTitle,30)) {
            addPersonDetails();
        }else if(isTitlePresent(directorVariationDetailsTitle,30)){
            addDirectorDetails();
        }
        try {
            accessibilityScanner();
        } catch (IllegalBrowserException | IOException e) {
            e.printStackTrace();
        }
        completeDirectorFinancialHistory("N");
        completeConvictionsAndPenalties("N");
    }

    public void addDirectorDetails()  {
        try {
            accessibilityScanner();
        } catch (IllegalBrowserException | IOException e) {
            e.printStackTrace();
        }
        personDetails();
        clickByName("form-actions[saveAndContinue]");
    }

    public void addPersonDetails()  {
        try {
            accessibilityScanner();
        } catch (IllegalBrowserException | IOException e) {
            e.printStackTrace();
        }
        personDetails();
        clickByName("form-actions[submit]");
    }

    private void personDetails() {
        selectValueFromDropDown(directorTitleDropdown, SelectorType.XPATH, "Dr");
        directorFirstName = faker.generateFirstName();
        directorLastName = faker.generateLastName();
        enterText(firstNameField, SelectorType.XPATH, directorFirstName);
        enterText(lastNameField, SelectorType.XPATH, directorLastName);
        HashMap<String, String> dates = world.globalMethods.date.getDateHashMap(-5, 0, -20);
        enterDateFieldsByPartialId("dob", dates);
    }


    public void completeDirectorFinancialHistory(String financialHistoryAnswers) {
        world.genericUtils.findSelectAllRadioButtonsByValue(financialHistoryAnswers);
        clickByXPath(saveAndContinue);
    };

    public void completeConvictionsAndPenalties(String convictionsAndPenaltiesAnswers) {
        world.genericUtils.findSelectAllRadioButtonsByValue(convictionsAndPenaltiesAnswers);
        clickByXPath(saveAndContinue);
    };

    public void removeDirector()  {
        click(deleteDirectorButtons, SelectorType.XPATH);
        waitForTextToBePresent(deleteDirectorConfirmationTitle);
        clickByXPath(deleteDirectorConfirmation);
    }

    public boolean isDirectorPresentInDirectorTable(List<WebElement> directors, String director) {
        return directors.stream().anyMatch(d -> d.getAttribute("value").contains(director));
    }

    public void answerConvictionsAndPenalties(String convictionsAndPenaltiesAnswer) {
        if (convictionsAndPenaltiesAnswer.equals("No")) {
            findSelectAllRadioButtonsByValue("N");
        } else {
            findSelectAllRadioButtonsByValue("Y");
            click("add", SelectorType.ID);
            world.convictionsAndPenaltiesJourney.addPreviousConviction();
        }
    }

    public void answerFinancialHistory(String answerToFinancialQuestions) {
        if (answerToFinancialQuestions.equals("No")) {
            findSelectAllRadioButtonsByValue("N");
        } else {
            findSelectAllRadioButtonsByValue("Y");
            enterText(world.directorJourney.additionalInformation, SelectorType.XPATH, Str.randomWord(150));
        }
    }

    public void assertTaskCheckBoxSelected() {
        String isSelected = findElement(world.directorJourney.internalUrgentCheckboxLabel, SelectorType.XPATH, 30).getAttribute("class");
        assertEquals("selected", isSelected);
    }

    public void assertTaskCheckBoxUnselected() {
        String isSelected = findElement(world.directorJourney.internalUrgentCheckboxLabel, SelectorType.XPATH, 30).getAttribute("class");
        assertEquals("", isSelected);
    }

    public void assertDirectorCount(int count) {
        waitForTitleToBePresent(world.directorJourney.directorsTitle);
        List<WebElement> directors = findElements(world.directorJourney.directorLinks, SelectorType.XPATH);
        long directorCount = directors.size();
        assertEquals(count, directorCount);
    }

    public void assertLastDirectorTaskNotCreated() {
        List<WebElement> directors = world.UIJourney.getTableBodyRowList();
        assertFalse(directors.stream().anyMatch(d -> d.getText().contains(lastDirectorRemovedMessage)));
    }

    public void assertLastDirectorTaskCreated() {
        List<WebElement> directors = world.UIJourney.getTableBodyRowList();
        assertTrue(directors.stream().anyMatch(d -> d.getText().contains(lastDirectorRemovedMessage)));
    }

    public void assertDirectorChangeInTable() {
        List<WebElement> listOfInternalLicenceDocuments = findElements("//tbody/tr[*]/td[2]", SelectorType.XPATH);
        String documentPrefix = world.licenceCreation.isGoodsLicence() ? GVPeopleChangeSnapshot : PSVPeopleChangeSnapshot;
        String peopleChangeDocument = String.format("//a[contains(text(),'%s')]", documentPrefix);
        assertTrue(listOfInternalLicenceDocuments.stream().anyMatch(d -> d.getText().contains("Application")));
        assertTrue(isElementPresent(peopleChangeDocument, SelectorType.XPATH));
    }

    public void assertNewDirectorExistsAndMultiplePresent(String director) {
        List<WebElement> directorList = findElements("//*/tbody/tr[*]/td[1]/input", SelectorType.XPATH);
        long directorsCount = directorList.size();
        MatcherAssert.assertThat(directorsCount, greaterThan(1L));
        assertTrue(directorList.stream().anyMatch(d -> d.getAttribute("value").contains(director)));
    }
}
