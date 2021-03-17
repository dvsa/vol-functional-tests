package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.faker.FakerUtils;
import activesupport.string.Str;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectorJourneySteps extends BasePage {

    private World world;
    private FakerUtils faker = new FakerUtils();
    private String directorFirstName;
    private String directorLastName;

    public String directorsTitle = "Directors";
    public String directorLinks = "//tbody/tr/td[1]/input";
    public String addButton = "//button[@name='table[action]']";
    public String directorDetailsTitle = "Add a director";
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
    public String titleValidation = "Title is required";
    public String firstNameValidation = "First name is required";
    public String lastNameValidation = "Last name is required";
    public String dateOfBirthEmptyFieldValidation = "Date of birth is required";
    public String dateOfBirthIncorrectValueValidation1 = "The date should be entered in number format";
    public String dateOfBirthIncorrectValueValidation2 = "Please enter all 4 digits of the year";
    public String dateOfBirthIncorrectValueValidation3 = "The input does not appear to be a valid value";
    public String bankruptcyValidation = "Bankruptcy: Choose an option";
    public String liquidationValidation = "Liquidation: Choose an option";
    public String receivershipValidation = "Receivership: Choose an option";
    public String administrationValidation = "Administration: Choose an option";
    public String disqualifiedValidation = "Disqualified: Choose an option";
    public String convictionsAndPenaltiesValidation = "validation message not decided yet";


    public DirectorJourneySteps(World world){
        this.world = world;
    }

    public String getDirectorName() {
        return directorFirstName.concat(" ").concat(directorLastName);
    }

    public void addDirectorWithNoFinancialHistoryConvictionsOrPenalties() throws IllegalBrowserException, MalformedURLException {
        click(addButton, SelectorType.XPATH);
        addDirectorDetails();
        completeDirectorFinancialHistory("N");
        completeConvictionsAndPenalties("N");
    }

    public void addDirectorDetails() throws IllegalBrowserException, MalformedURLException {
        waitForTitleToBePresent(directorDetailsTitle);
        selectValueFromDropDown(directorTitleDropdown, SelectorType.XPATH, "Dr");
        directorFirstName = faker.generateFirstName();
        directorLastName = faker.generateLastName();
        enterText(firstNameField, directorFirstName, SelectorType.XPATH);
        enterText(lastNameField, directorLastName, SelectorType.XPATH);
        HashMap<String, String> dates = world.globalMethods.date.getDateHashMap(-5, 0, -20);
        replaceDateFieldsByPartialId("dob", dates);
        clickByXPath(saveAndContinue);
    }

    public void completeDirectorFinancialHistory(String financialHistoryAnswers) throws MalformedURLException, IllegalBrowserException {
        world.genericUtils.findSelectAllRadioButtonsByValue(financialHistoryAnswers);
        clickByXPath(saveAndContinue);
    };

    public void completeConvictionsAndPenalties(String convictionsAndPenaltiesAnswers) throws MalformedURLException, IllegalBrowserException {
        world.genericUtils.findSelectAllRadioButtonsByValue(convictionsAndPenaltiesAnswers);
        clickByXPath(saveAndContinue);
    };

    public void removeDirector() throws IllegalBrowserException, MalformedURLException {
        click(deleteDirectorButtons, SelectorType.XPATH);
        waitForTextToBePresent(deleteDirectorConfirmationTitle);
        clickByXPath(deleteDirectorConfirmation);
    }

    public boolean isDirectorPresentInDirectorTable(List<WebElement> directors, String director) {
        return directors.stream().anyMatch(d -> d.getAttribute("value").contains(director));
    }

    public void answerConvictionsAndPenalties(String convictionsAndPenaltiesAnswer) throws MalformedURLException, IllegalBrowserException {
        if (convictionsAndPenaltiesAnswer.equals("No")) {
            findSelectAllRadioButtonsByValue("N");
        } else {
            findSelectAllRadioButtonsByValue("Y");
            click("add", SelectorType.ID);
            world.UIJourneySteps.addPreviousConviction();
        }
    }

    public void answerFinancialHistory(String answerToFinancialQuestions) throws MalformedURLException, IllegalBrowserException {
        if (answerToFinancialQuestions.equals("No")) {
            findSelectAllRadioButtonsByValue("N");
        } else {
            findSelectAllRadioButtonsByValue("Y");
            enterText(world.directorJourneySteps.additionalInformation, Str.randomWord(150), SelectorType.XPATH);
        }
    }

    public void assertTaskCheckBoxSelected() throws MalformedURLException, IllegalBrowserException {
        String isSelected = findElement(world.directorJourneySteps.internalUrgentCheckboxLabel, SelectorType.XPATH, 30).getAttribute("class");
        assertEquals("selected", isSelected);
    }

    public void assertTaskCheckBoxUnselected() throws MalformedURLException, IllegalBrowserException {
        String isSelected = findElement(world.directorJourneySteps.internalUrgentCheckboxLabel, SelectorType.XPATH, 30).getAttribute("class");
        assertEquals("", isSelected);
    }

    public void assertDirectorCount(int count) throws MalformedURLException, IllegalBrowserException {
        waitForTitleToBePresent(world.directorJourneySteps.directorsTitle);
        List<WebElement> directors = listOfWebElements(world.directorJourneySteps.directorLinks, SelectorType.XPATH);
        long directorCount = directors.size();
        assertEquals(count, directorCount);
    }

    public void assertLastDirectorTaskNotCreated() throws MalformedURLException, IllegalBrowserException {
        List<WebElement> directors = world.UIJourneySteps.getTableBodyRowList();
        assertFalse(directors.stream().anyMatch(d -> d.getText().contains(lastDirectorRemovedMessage)));
    }

    public void assertLastDirectorTaskCreated() throws MalformedURLException, IllegalBrowserException {
        List<WebElement> directors = world.UIJourneySteps.getTableBodyRowList();
        assertTrue(directors.stream().anyMatch(d -> d.getText().contains(lastDirectorRemovedMessage)));
    }

    public void assertDirectorChangeInTable() throws MalformedURLException, IllegalBrowserException {
        List<WebElement> listOfInternalLicenceDocuments = listOfWebElements("//tbody/tr[*]/td[2]", SelectorType.XPATH);
        String documentPrefix = world.licenceCreation.isGoodsLicence() ? GVPeopleChangeSnapshot : PSVPeopleChangeSnapshot;
        String peopleChangeDocument = String.format("//a[contains(text(),'%s')]", documentPrefix);
        assertTrue(listOfInternalLicenceDocuments.stream().anyMatch(d -> d.getText().contains("Application")));
        assertTrue(isElementPresent(peopleChangeDocument, SelectorType.XPATH));
    }

    public void assertNewDirectorExistsAndMultiplePresent(String director) throws MalformedURLException, IllegalBrowserException {
        List<WebElement> directorList = listOfWebElements("//*/tbody/tr[*]/td[1]/input", SelectorType.XPATH);
        long directorsCount = directorList.size();
        MatcherAssert.assertThat(directorsCount, greaterThan(1L));
        assertTrue(directorList.stream().anyMatch(d -> d.getAttribute("value").contains(director)));
    }
}
