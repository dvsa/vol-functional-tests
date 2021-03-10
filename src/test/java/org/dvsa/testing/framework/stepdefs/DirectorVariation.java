package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.string.Str;
import cucumber.api.java.en.*;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.DirectorJourneySteps;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectorVariation extends BasePage implements En {

    private World world;
    private DirectorJourneySteps directorJourney = world.directorJourneySteps;


    @When("^i begin adding a new director and their details$")
    public void iBeginAddingANewDirectorAndTheirDetails() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        clickByXPath(directorJourney.addButton);
        world.directorJourneySteps.addDirectorDetails();
    }

    @Then("^a new director should be added to my licence$")
    public void aNewDirectorShouldBeAddedToMyLicence() throws MalformedURLException, IllegalBrowserException {
        waitForTitleToBePresent(directorJourney.directorsTitle);
        List<WebElement> directors = listOfWebElements(directorJourney.directorLinks, SelectorType.XPATH);
        long directorCount = directors.size();
        assertEquals(directorCount, 2);
        assertTrue(directorJourney.isDirectorPresentInDirectorTable(directors, directorJourney.getDirectorName()));
    }

    @And("^a non urgent task is created in internal$")
    public void aNonUrgentTaskIsCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logInAndNavigateToTask();
        clickByLinkText("Add director(s)");
        waitForTextToBePresent("Linked to");
        String isSelected = findElement("//div[4]/label", SelectorType.XPATH, 30).getAttribute("class");
        assertEquals(isSelected, "");
    }

    @When("^i enter \"([^\"]*)\" to previous convictions details question$")
    public void iEnterPreviousToConvictionDetailsQuestion (String convictionsAndPenaltiesAnswer) throws MalformedURLException, IllegalBrowserException {
        if (convictionsAndPenaltiesAnswer.equals("No")) {
            findSelectAllRadioButtonsByValue("N");
        } else {
            findSelectAllRadioButtonsByValue("Y");
            click("add", SelectorType.ID);
            world.UIJourneySteps.addPreviousConviction();
        }
        clickByXPath(directorJourney.saveAndContinue);
    }

    @And("^an urgent task is created in internal$")
    public void anUrgentTaskIsCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logInAndNavigateToTask();
        clickByLinkText("Add director(s)");
        waitForTextToBePresent("Linked to");
        String isSelected = findElement("//div[4]/label", SelectorType.XPATH, 30).getAttribute("class");
        assertEquals(isSelected, "selected");
    }

    @And("^i enter \"([^\"]*)\" to financial details question$")
    public void iEnterToFinancialDetailsQuestion(String answerToFinancialQuestions) throws MalformedURLException, IllegalBrowserException {
        if (answerToFinancialQuestions.equals("No")) {
            findSelectAllRadioButtonsByValue("N");
        } else {
            findSelectAllRadioButtonsByValue("Y");
            enterText("data[insolvencyDetails]", Str.randomWord(150), SelectorType.ID);
        }
        clickByName("form-actions[saveAndContinue]");
    }

    @Then("^a snapshot should be created in internal$")
    public void aSnapshotShouldBeCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logInAndNavigateToDocsTable();
        List<WebElement> docsAttach = listOfWebElements("//tbody/tr[*]/td[2]", SelectorType.XPATH);
        assertTrue(docsAttach.stream().anyMatch(d -> d.getText().contains("Application")));
    }

    @Given("^i add a director$")
    public void iAddADirector() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        world.directorJourneySteps.addDirectorWithNoFinancialHistoryConvictionsOrPenalties();
    }

    @Then("^i should have multiple directors on my application$")
    public void iShouldHaveMultipleDirectorOnMyApplication() throws MalformedURLException, IllegalBrowserException {
        waitForTitleToBePresent("Directors");
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
        List<WebElement> director = listOfWebElements("//tbody", SelectorType.XPATH);
        assertFalse(director.stream().anyMatch(d -> d.getText().contains("Last director removed")));
    }

    @Then("^a task should be created in internal$")
    public void aTaskShouldBeCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(),world.updateLicence.getInternalUserEmailAddress());
        world.internalSearch.searchAndViewApplication();
        //TODO
    }

    @Then("^a task is created in internal$")
    public void aTaskIsCreatedInInternal() throws MalformedURLException, IllegalBrowserException {
        world.internalNavigation.logInAndNavigateToTask();
        List<WebElement> director = listOfWebElements("//tbody", SelectorType.XPATH);
        assertTrue(director.stream().anyMatch(d -> d.getText().contains("Last director removed")));
    }
}