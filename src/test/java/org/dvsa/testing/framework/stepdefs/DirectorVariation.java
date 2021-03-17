package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.java.sl.In;
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


    @When("^i begin adding a new director and their details$")
    public void iBeginAddingANewDirectorAndTheirDetails() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        clickByXPath(directorJourney.addButton);
        directorJourney.addDirectorDetails();
    }

    @Then("^a new director should be added to my licence$")
    public void aNewDirectorShouldBeAddedToMyLicence() throws MalformedURLException, IllegalBrowserException {
        directorJourney.assertDirectorCount(2);
        List<WebElement> directors = listOfWebElements(directorJourney.directorLinks, SelectorType.XPATH);
        assertTrue(directorJourney.isDirectorPresentInDirectorTable(directors, directorJourney.getDirectorName()));
    }

    @And("^a non urgent task is created in internal$")
    public void aNonUrgentTaskIsCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logIntoInternalAndClickOnTask(directorJourney.internalDirectorTask);
        directorJourney.assertTaskCheckBoxUnselected();
    }

    @When("^i enter \"([^\"]*)\" to previous convictions details question$")
    public void iEnterPreviousToConvictionDetailsQuestion (String answer) throws MalformedURLException, IllegalBrowserException {
        directorJourney.answerConvictionsAndPenalties(answer);
        clickByXPath(directorJourney.saveAndContinue);
    }

    @And("^an urgent task is created in internal$")
    public void anUrgentTaskIsCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logIntoInternalAndClickOnTask(directorJourney.internalDirectorTask);
        directorJourney.assertTaskCheckBoxSelected();
    }

    @And("^i enter \"([^\"]*)\" to financial details question$")
    public void iEnterToFinancialDetailsQuestion(String answer) throws MalformedURLException, IllegalBrowserException {
        directorJourney.answerFinancialHistory(answer);
        clickByXPath(directorJourney.saveAndContinue);
    }

    @Then("^a snapshot should be created in internal$")
    public void aSnapshotShouldBeCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logInAndNavigateToDocsTable();
        directorJourney.assertDirectorChangeInTable(world.createApplication.getOperatorType());
    }

    @Given("^i add a director$")
    public void iAddADirector() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        directorJourney.addDirectorWithNoFinancialHistoryConvictionsOrPenalties();
    }

    @Then("^i should have multiple directors on my application$")
    public void iShouldHaveMultipleDirectorOnMyApplication() throws MalformedURLException, IllegalBrowserException {
        waitForTitleToBePresent(directorJourney.directorsTitle);
        directorJourney.assertNewDirectorExistsAndMultiplePresent(directorJourney.getDirectorName());
    }

    @When("^i add another new director$")
    public void iAddAnotherNewDirector() throws MalformedURLException, IllegalBrowserException {
        directorJourney.addDirectorWithNoFinancialHistoryConvictionsOrPenalties();
    }

    @When("^i remove a director$")
    public void iRemoveADirector() throws MalformedURLException, IllegalBrowserException {
        directorJourney.removeDirector();
    }

    @When("^i remove the last director$")
    public void iRemoveTheLastDirector() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        directorJourney.removeDirector();
    }

    @Then("^a task should not be created in internal$")
    public void aTaskShouldNotBeCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logInAndNavigateToTask();
        directorJourney.assertLastDirectorTaskNotCreated();
    }

    @Then("^the last director deleted task is created in internal$")
    public void aLastDirectorDeletedTaskIsCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logInAndNavigateToTask();
        directorJourney.assertLastDirectorTaskCreated();
    }

    @When("when I submit the add a director page")
    public void whenISubmitTheAddADirectorPage() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        clickByName(directorJourney.saveAndContinue);
        waitForTextToBePresent(directorJourney.validationTitle);
    }

    @Then("the add a director page empty field validation should appear")
    public void theAddADirectorPageEmptyFieldValidationShouldAppear() throws MalformedURLException, IllegalBrowserException {
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

    @When("when I wrongly fill in and submit the add a director page")
    public void whenIWronglyFillInAndSubmitTheAddADirectorPage() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        clickByXPath(directorJourney.addButton);
        waitForTitleToBePresent(directorJourney.directorDetailsTitle);
        selectValueFromDropDown(directorJourney.directorTitleDropdown, SelectorType.XPATH, "Dr");

        String incorrectNameValue = "!@£$%^";
        enterText(directorJourney.firstNameField, incorrectNameValue, SelectorType.XPATH);
        enterText(directorJourney.lastNameField, incorrectNameValue, SelectorType.XPATH);

        HashMap<String, String> incorrectDateValues = new HashMap();
        incorrectDateValues.put("day", "!@");
        incorrectDateValues.put("month", "£$");
        incorrectDateValues.put("year", "%^&*");
        replaceDateFieldsByPartialId("dob", incorrectDateValues);

        clickByXPath(directorJourney.saveAndContinue);
        waitForTextToBePresent(directorJourney.validationTitle);
    }

    @Then("the add a director page incorrect value validation should appear")
    public void theAddADirectorPageIncorrectValueValidationShouldAppear() throws MalformedURLException, IllegalBrowserException {
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

}