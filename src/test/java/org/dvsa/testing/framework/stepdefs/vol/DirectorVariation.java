package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.licence.DirectorJourney;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectorVariation extends BasePage {

    World world;
    private DirectorJourney directorJourney;

    public DirectorVariation(World world) {
        this.world = world;
        directorJourney = world.directorJourney;
    }

    @When("^I begin adding a new director and their details$")
    public void iBeginAddingANewDirectorAndTheirDetails() {
        clickByXPath(directorJourney.addButton);
        directorJourney.addDirectorDetails();
    }

    @Then("^a new director should be added to my licence$")
    public void aNewDirectorShouldBeAddedToMyLicence() {
        waitForTextToBePresent("Director has been added");
        directorJourney.assertDirectorCount(2);
        List<WebElement> directors = findElements(directorJourney.directorLinks, SelectorType.XPATH);
        assertTrue(directorJourney.isDirectorPresentInDirectorTable(directors, directorJourney.getDirectorName()));
    }

    @And("^a non urgent task is created in internal$")
    public void aNonUrgentTaskIsCreatedInInternal() throws HttpException {
        world.internalNavigation.logIntoInternalAndClickOnTask(directorJourney.internalDirectorTask);
        directorJourney.assertTaskCheckBoxUnselected();
    }

    @When("^i enter \"([^\"]*)\" to previous convictions details question$")
    public void iEnterPreviousToConvictionDetailsQuestion(String answer) {
       world.directorJourney.answerConvictionsAndPenalties(answer);
        UniversalActions.clickSaveAndContinue();
    }

    @And("^an urgent task is created in internal$")
    public void anUrgentTaskIsCreatedInInternal() throws HttpException {
        world.internalNavigation.logIntoInternalAndClickOnTask(directorJourney.internalDirectorTask);
        directorJourney.assertTaskCheckBoxSelected();
    }

    @And("^i enter \"([^\"]*)\" to financial details question$")
    public void iEnterToFinancialDetailsQuestion(String answer) {
        world.directorJourney.answerFinancialHistory(answer);
        UniversalActions.clickSaveAndContinue();
    }

    @And("^i enter \"([^\"]*)\" to licence history question$")
    public void iEnterToLicenceHistoryQuestion(String answer) {
        world.directorJourney.completeLicenceHistory(answer);
    }

    @Then("^a snapshot should be created in internal$")
    public void aSnapshotShouldBeCreatedInInternal() throws HttpException {
        world.internalNavigation.logInAndNavigateToApplicationDocsTable(false);
        directorJourney.assertDirectorChangeInTable();
    }

    @Given("^i add a director$")
    public void iAddADirector() throws IllegalBrowserException, IOException {
        world.directorJourney.addDirectorWithNoFinancialHistoryConvictionsOrPenalties(false);
    }

    @Then("^i should have multiple directors on my application$")
    public void iShouldHaveMultipleDirectorOnMyApplication() {
        waitForTitleToBePresent(directorJourney.directorsTitle);
        directorJourney.assertNewDirectorExistsAndMultiplePresent(directorJourney.getDirectorName());
    }

    @When("^i add another new director$")
    public void iAddAnotherNewDirector() throws IllegalBrowserException, IOException {
        directorJourney.addDirectorWithNoFinancialHistoryConvictionsOrPenalties(false);
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
    public void aTaskShouldNotBeCreatedInInternal() throws HttpException {
        world.internalNavigation.logInAndNavigateToApplicationProcessingPage(false);
        directorJourney.assertLastDirectorTaskNotCreated();
    }

    @Then("^the last director deleted task is created in internal$")
    public void aLastDirectorDeletedTaskIsCreatedInInternal() throws HttpException {
        world.internalNavigation.logInAndNavigateToApplicationProcessingPage(false);
        directorJourney.assertLastDirectorTaskCreated();
    }

    @When("I begin adding a director but submit empty fields")
    public void iBeginAddingADirectorButSubmitEmptyFields() {
        clickByXPath(directorJourney.addButton);
        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent(directorJourney.validationTitle);
    }

    @When("I submit the empty page")
    public void iSubmitAnEmptyPage() {
        UniversalActions.clickSaveAndContinue();
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
        assertEquals(directorJourney.titleValidation, listOfInlineErrors.get(0).getText().replace("Error:", "").trim());
        assertEquals(directorJourney.firstNameValidation, listOfInlineErrors.get(1).getText().replace("Error:", "").trim());
        assertEquals(directorJourney.lastNameValidation, listOfInlineErrors.get(2).getText().replace("Error:", "").trim());
        assertEquals(directorJourney.dateOfBirthEmptyFieldValidation, listOfInlineErrors.get(3).getText().replace("Error:", "").trim());
    }

    @When("I wrongly fill in and submit the add a director page")
    public void whenIWronglyFillInAndSubmitTheAddADirectorPage() {
        clickByXPath(directorJourney.addButton);
        waitForTitleToBePresent(directorJourney.directorVariationDetailsTitle);
        selectValueFromDropDown(directorJourney.directorTitleDropdown, SelectorType.XPATH, "Dr");

        String incorrectNameValue = "!@£$%^";
        enterText(directorJourney.firstNameField, SelectorType.XPATH, incorrectNameValue);
        enterText(directorJourney.lastNameField, SelectorType.XPATH, incorrectNameValue);

        HashMap<String, String> incorrectDateValues = new HashMap<String, String>();
        incorrectDateValues.put("day", "!@");
        incorrectDateValues.put("month", "£$");
        incorrectDateValues.put("year", "%^&*");
        enterDateFieldsByPartialId("dob", incorrectDateValues);

        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent(directorJourney.validationTitle);
    }

    @Then("the add a director page incorrect value validation should appear")
    public void theAddADirectorPageIncorrectValueValidationShouldAppear() {
        // Name fields do not currently contain any incorrect value validation

        List<WebElement> listOfSummaryErrors = findElements(directorJourney.listOfSummaryErrors, SelectorType.XPATH);
        assertEquals(directorJourney.dateOfBirthEmptyFieldValidation, listOfSummaryErrors.get(0).getText());
        assertEquals(directorJourney.dateOfBirthEmptyFieldValidation, listOfSummaryErrors.get(1).getText());
        assertEquals(directorJourney.dateOfBirthEmptyFieldValidation, listOfSummaryErrors.get(2).getText());


        assertTrue(isTextPresent(directorJourney.dateOfBirthEmptyFieldValidation));
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
        assertEquals(directorJourney.bankruptcyValidation, listOfInlineErrors.get(0).getText().replace("Error:", "").trim());
        assertEquals(directorJourney.liquidationValidation, listOfInlineErrors.get(1).getText().replace("Error:", "").trim());
        assertEquals(directorJourney.receivershipValidation, listOfInlineErrors.get(2).getText().replace("Error:", "").trim());
        assertEquals(directorJourney.administrationValidation, listOfInlineErrors.get(3).getText().replace("Error:", "").trim());
        assertEquals(directorJourney.disqualifiedValidation, listOfInlineErrors.get(4).getText().replace("Error:", "").trim());
    }

    @Then("the director convictions and penalties page empty field validation should appear")
    public void theDirectorConvictionsAndPenaltiesPageEmptyFieldValidationShouldAppear() {
        List<WebElement> listOfSummaryErrors = findElements(directorJourney.listOfSummaryErrors, SelectorType.XPATH);
        String actualMessage = listOfSummaryErrors.get(0).getText();

        assertTrue(
                actualMessage.equals("Value is required") || actualMessage.equals("Existing licence: choose an option"),
                "Validation message does not match any of the expected values."
        );

        assertTrue(
                isTextPresent("Value is required") || isTextPresent("Existing licence: choose an option"),
                "Expected validation message is not present on the page."
        );
    }
}