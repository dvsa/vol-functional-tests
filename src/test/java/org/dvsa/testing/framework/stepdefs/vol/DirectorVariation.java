package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.IllegalBrowserException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.Journeys.DirectorJourneySteps;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectorVariation extends BasePage {

    World world;
    private DirectorJourneySteps directorJourney;

    public DirectorVariation(World world) {
        this.world = world;
        directorJourney = world.directorJourneySteps;
    }

    @And("i navigate to the directors page")
    public void iNavigateToTheDirectorsPage() {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
    }

    @When("^I begin adding a new director and their details$")
    public void iBeginAddingANewDirectorAndTheirDetails() {
        clickByXPath(directorJourney.addButton);
        directorJourney.addDirectorDetails();
    }

    @Then("^a new director should be added to my licence$")
    public void aNewDirectorShouldBeAddedToMyLicence() {
        directorJourney.assertDirectorCount(2);
        List<WebElement> directors = listOfWebElements(directorJourney.directorLinks, SelectorType.XPATH);
        assertTrue(directorJourney.isDirectorPresentInDirectorTable(directors, directorJourney.getDirectorName()));
    }

    @And("^a non urgent task is created in internal$")
    public void aNonUrgentTaskIsCreatedInInternal() {
        world.internalNavigation.logIntoInternalAndClickOnTask(directorJourney.internalDirectorTask);
        directorJourney.assertTaskCheckBoxUnselected();
    }

    @When("^i enter \"([^\"]*)\" to previous convictions details question$")
    public void iEnterPreviousToConvictionDetailsQuestion (String answer) {
        directorJourney.answerConvictionsAndPenalties(answer);
        clickByXPath(directorJourney.saveAndContinue);
    }

    @And("^an urgent task is created in internal$")
    public void anUrgentTaskIsCreatedInInternal() {
        world.internalNavigation.logIntoInternalAndClickOnTask(directorJourney.internalDirectorTask);
        directorJourney.assertTaskCheckBoxSelected();
    }

    @And("^i enter \"([^\"]*)\" to financial details question$")
    public void iEnterToFinancialDetailsQuestion(String answer) {
        directorJourney.answerFinancialHistory(answer);
        clickByXPath(directorJourney.saveAndContinue);
    }

    @Then("^a snapshot should be created in internal$")
    public void aSnapshotShouldBeCreatedInInternal() {
        world.internalNavigation.logInAndNavigateToDocsTable();
        directorJourney.assertDirectorChangeInTable();
    }

    @Given("^i add a director$")
    public void iAddADirector() {
        directorJourney.addDirectorWithNoFinancialHistoryConvictionsOrPenalties();
    }

    @Then("^i should have multiple directors on my application$")
    public void iShouldHaveMultipleDirectorOnMyApplication() {
        waitForTitleToBePresent(directorJourney.directorsTitle);
        directorJourney.assertNewDirectorExistsAndMultiplePresent(directorJourney.getDirectorName());
    }

    @When("^i add another new director$")
    public void iAddAnotherNewDirector() {
        directorJourney.addDirectorWithNoFinancialHistoryConvictionsOrPenalties();
    }

    @When("^i remove a director$")
    public void iRemoveADirector() {
        directorJourney.removeDirector();
    }

    @When("^i remove the last director$")
    public void iRemoveTheLastDirector() {
        directorJourney.removeDirector();
    }

    @Then("^a task should not be created in internal$")
    public void aTaskShouldNotBeCreatedInInternal() {
        world.internalNavigation.logInAndNavigateToTask();
        directorJourney.assertLastDirectorTaskNotCreated();
    }

    @Then("^the last director deleted task is created in internal$")
    public void aLastDirectorDeletedTaskIsCreatedInInternal() {
        world.internalNavigation.logInAndNavigateToTask();
        directorJourney.assertLastDirectorTaskCreated();
    }

    @When("I begin adding a director but submit empty fields")
    public void iBeginAddingADirectorButSubmitEmptyFields() {
        clickByXPath(directorJourney.addButton);
        clickByXPath(directorJourney.saveAndContinue);
        waitForTextToBePresent(directorJourney.validationTitle);
    }

    @When("I submit the empty page")
    public void iSubmitAnEmptyPage() {
        clickByXPath(directorJourney.saveAndContinue);
        waitForTextToBePresent(directorJourney.validationTitle);
    }

    @Then("the add a director page empty field validation should appear")
    public void theAddADirectorPageEmptyFieldValidationShouldAppear() {
        List<WebElement> listOfSummaryErrors = findElements(directorJourney.listOfSummaryErrors, SelectorType.XPATH);
        assertEquals(directorJourney.titleValidation, listOfSummaryErrors.get(0).getText());
        assertEquals(directorJourney.firstNameValidation, listOfSummaryErrors.get(1).getText());
        assertEquals(directorJourney.lastNameValidation, listOfSummaryErrors.get(2).getText());
        assertEquals(directorJourney.dateOfBirthEmptyFieldValidation, listOfSummaryErrors.get(3).getText());

        List<WebElement> listOfInlineErrors = findElements(directorJourney.listOfInlineErrors, SelectorType.XPATH);
        assertEquals(directorJourney.titleValidation, listOfInlineErrors.get(0).getText());
        assertEquals(directorJourney.firstNameValidation, listOfInlineErrors.get(1).getText());
        assertEquals(directorJourney.lastNameValidation, listOfInlineErrors.get(2).getText());
        assertEquals(directorJourney.dateOfBirthEmptyFieldValidation, listOfInlineErrors.get(3).getText());
    }

    @When("I wrongly fill in and submit the add a director page")
    public void whenIWronglyFillInAndSubmitTheAddADirectorPage() {
        clickByXPath(directorJourney.addButton);
        waitForTitleToBePresent(directorJourney.directorDetailsTitle);
        selectValueFromDropDown(directorJourney.directorTitleDropdown, SelectorType.XPATH, "Dr");

        String incorrectNameValue = "!@£$%^";
        enterText(directorJourney.firstNameField, incorrectNameValue, SelectorType.XPATH);
        enterText(directorJourney.lastNameField, incorrectNameValue, SelectorType.XPATH);

        HashMap<String, String> incorrectDateValues = new HashMap<String,String>();
        incorrectDateValues.put("day", "!@");
        incorrectDateValues.put("month", "£$");
        incorrectDateValues.put("year", "%^&*");
        replaceDateFieldsByPartialId("dob", incorrectDateValues);

        clickByXPath(directorJourney.saveAndContinue);
        waitForTextToBePresent(directorJourney.validationTitle);
    }

    @Then("the add a director page incorrect value validation should appear")
    public void theAddADirectorPageIncorrectValueValidationShouldAppear() {
        // Name fields do not currently contain any incorrect value validation

        List<WebElement> listOfSummaryErrors = findElements(directorJourney.listOfSummaryErrors, SelectorType.XPATH);
        assertEquals(directorJourney.dateOfBirthIncorrectValueValidation1, listOfSummaryErrors.get(0).getText());
        assertEquals(directorJourney.dateOfBirthIncorrectValueValidation2, listOfSummaryErrors.get(1).getText());
        assertEquals(directorJourney.dateOfBirthIncorrectValueValidation3, listOfSummaryErrors.get(2).getText());

        List<WebElement> listOfInlineErrors = findElements(directorJourney.listOfInlineErrors, SelectorType.XPATH);
        assertEquals(directorJourney.dateOfBirthIncorrectValueValidation1, listOfInlineErrors.get(0).getText());
        assertEquals(directorJourney.dateOfBirthIncorrectValueValidation2, listOfInlineErrors.get(1).getText());
        assertEquals(directorJourney.dateOfBirthIncorrectValueValidation3, listOfInlineErrors.get(2).getText());
    }

    @Then("the director financial history page empty field validation should appear")
    public void theDirectorFinancialHistoryPageEmptyFieldValidationShouldAppear() {
        List<WebElement> listOfSummaryErrors = findElements(directorJourney.listOfSummaryErrors, SelectorType.XPATH);
        assertEquals(directorJourney.bankruptcyValidation, listOfSummaryErrors.get(0).getText());
        assertEquals(directorJourney.liquidationValidation, listOfSummaryErrors.get(1).getText());
        assertEquals(directorJourney.receivershipValidation, listOfSummaryErrors.get(2).getText());
        assertEquals(directorJourney.administrationValidation, listOfSummaryErrors.get(3).getText());
        assertEquals(directorJourney.disqualifiedValidation, listOfSummaryErrors.get(4).getText());

        List<WebElement> listOfInlineErrors = findElements(directorJourney.listOfInlineErrors, SelectorType.XPATH);
        assertEquals(directorJourney.bankruptcyValidation, listOfInlineErrors.get(0).getText());
        assertEquals(directorJourney.liquidationValidation, listOfInlineErrors.get(1).getText());
        assertEquals(directorJourney.receivershipValidation, listOfInlineErrors.get(2).getText());
        assertEquals(directorJourney.administrationValidation, listOfInlineErrors.get(3).getText());
        assertEquals(directorJourney.disqualifiedValidation, listOfInlineErrors.get(4).getText());
    }

    @Then("the director convictions and penalties page empty field validation should appear")
    public void theDirectorConvictionsAndPenaltiesPageEmptyFieldValidationShouldAppear() {
        List<WebElement> listOfSummaryErrors = findElements(directorJourney.listOfSummaryErrors, SelectorType.XPATH);
        assertEquals(directorJourney.convictionsAndPenaltiesValidation, listOfSummaryErrors.get(0).getText());

        List<WebElement> listOfInlineErrors = findElements(directorJourney.listOfInlineErrors, SelectorType.XPATH);
        assertEquals(directorJourney.convictionsAndPenaltiesValidation, listOfInlineErrors.get(0).getText());
    }
}