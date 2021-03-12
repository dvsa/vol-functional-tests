package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.string.Str;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.Journeys.DirectorJourneySteps;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectorVariation extends BasePage {

    World world;
    private DirectorJourneySteps directorJourney;

    public DirectorVariation(World world){
        this.world = world;
        directorJourney = world.directorJourneySteps;
    }


    @When("^i begin adding a new director and their details$")
    public void iBeginAddingANewDirectorAndTheirDetails() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        clickByXPath(world.directorJourneySteps.addButton);
        world.directorJourneySteps.addDirectorDetails();
    }

    @Then("^a new director should be added to my licence$")
    public void aNewDirectorShouldBeAddedToMyLicence() throws MalformedURLException, IllegalBrowserException {
        world.directorJourneySteps.assertDirectorCount(2);
        List<WebElement> directors = listOfWebElements(world.directorJourneySteps.directorLinks, SelectorType.XPATH);
        assertTrue(world.directorJourneySteps.isDirectorPresentInDirectorTable(directors, world.directorJourneySteps.getDirectorName()));
    }

    @And("^a non urgent task is created in internal$")
    public void aNonUrgentTaskIsCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logIntoInternalAndClickOnTask(world.directorJourneySteps.internalDirectorTask);
        world.directorJourneySteps.assertTaskCheckBoxUnselected();
    }

    @When("^i enter \"([^\"]*)\" to previous convictions details question$")
    public void iEnterPreviousToConvictionDetailsQuestion (String answer) throws MalformedURLException, IllegalBrowserException {
        world.directorJourneySteps.answerConvictionsAndPenalties(answer);
        clickByXPath(world.directorJourneySteps.saveAndContinue);
    }

    @And("^an urgent task is created in internal$")
    public void anUrgentTaskIsCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logIntoInternalAndClickOnTask(world.directorJourneySteps.internalDirectorTask);
        world.directorJourneySteps.assertTaskCheckBoxSelected();
    }

    @And("^i enter \"([^\"]*)\" to financial details question$")
    public void iEnterToFinancialDetailsQuestion(String answer) throws MalformedURLException, IllegalBrowserException {
        world.directorJourneySteps.answerFinancialHistory(answer);
        clickByXPath(world.directorJourneySteps.saveAndContinue);
    }

    @Then("^a snapshot should be created in internal$")
    public void aSnapshotShouldBeCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logInAndNavigateToDocsTable();
        List<WebElement> docsAttach = listOfWebElements("//tbody/tr[*]/td[2]", SelectorType.XPATH);
        assertTrue(docsAttach.stream().anyMatch(d -> d.getText().contains("Application")));
        assertTrue(isElementPresent("//a[contains(text(),'PSV431')]", SelectorType.XPATH));
        //TODO: Look over this.
    }

    @Given("^i add a director$")
    public void iAddADirector() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        world.directorJourneySteps.addDirectorWithNoFinancialHistoryConvictionsOrPenalties();
    }

    @Then("^i should have multiple directors on my application$")
    public void iShouldHaveMultipleDirectorOnMyApplication() throws MalformedURLException, IllegalBrowserException {
        waitForTitleToBePresent(world.directorJourneySteps.directorsTitle);
        List<WebElement> director = listOfWebElements("//*/tbody/tr[*]/td[1]/input", SelectorType.XPATH);
        long directors = director.size();
        MatcherAssert.assertThat(directors, greaterThan(1L));
        assertTrue(director.stream().anyMatch(d -> d.getAttribute("value").contains(world.directorJourneySteps.getDirectorName())));
    }

    @When("^i add another new director$")
    public void iAddAnotherNewDirector() throws MalformedURLException, IllegalBrowserException {
        world.directorJourneySteps.addDirectorWithNoFinancialHistoryConvictionsOrPenalties();
    }

    @When("^i remove a director$")
    public void iRemoveADirector() throws MalformedURLException, IllegalBrowserException {
        world.directorJourneySteps.removeDirector();
    }

    @When("^i remove the last director$")
    public void iRemoveTheLastDirector() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        world.directorJourneySteps.removeDirector();
    }

    @Then("^a task should not be created in internal$")
    public void aTaskShouldNotBeCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logInAndNavigateToTask();
        world.directorJourneySteps.assertLastDirectorTaskNotCreated();
    }

    @Then("^a task should be created in internal$")
    public void aTaskShouldBeCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(),world.updateLicence.getInternalUserEmailAddress());
        world.internalSearch.searchAndViewApplication();
        //TODO this is not really done...
    }

    @Then("^a task is created in internal$")
    public void aTaskIsCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logInAndNavigateToTask();
        world.directorJourneySteps.assertLastDirectorTaskCreated();
    }
}