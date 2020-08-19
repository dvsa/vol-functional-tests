package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.string.Str;
import cucumber.api.java8.En;;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectorVariation extends BasePage implements En {

    private String firstName = String.format("Director%s", Str.randomWord(4));
    private String lastName = "LastName";
    private World world;

    public DirectorVariation(World world) {
        this.world = world;

        When("^i add a new person$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "directors");
            world.directorJourneySteps.addPerson(firstName, lastName);
        });
        Then("^a new director should be added to my licence$", () -> {
            waitForTextToBePresent("Directors                ");
            List<WebElement> director = listOfWebElements("//*/tbody/tr[*]/td[1]/input", SelectorType.XPATH);
            long directors = director.size();
            assertEquals(directors, 2);
            assertTrue(director.stream().anyMatch(d -> d.getAttribute("value").contains(firstName)));
        });
        And("^a non urgent task is created in internal$", () -> {
            world.internalNavigation.logInAndNavigateToTask();
            clickByLinkText("Add director(s)");
            waitForTextToBePresent("Linked to");
            String isSelected = findElement("//div[4]/label", SelectorType.XPATH, 30).getAttribute("class");
            assertEquals(isSelected, "");
        });
        When("^i enter \"([^\"]*)\" previous convictions details question$", (String arg0) -> {
            if (arg0.equals("No")) {
                arg0 = "N";
                findSelectAllRadioButtonsByValue(arg0);
            } else {
                arg0 = "Y";
                findSelectAllRadioButtonsByValue(arg0);
                click("add", SelectorType.ID);
                world.UIJourneySteps.addPreviousConviction();
            }
            clickByName("form-actions[saveAndContinue]");
        });
        And("^an urgent task is created in internal$", () -> {
            world.internalNavigation.logInAndNavigateToTask();
            clickByLinkText("Add director(s)");
            waitForTextToBePresent("Linked to");
            String isSelected = findElement("//div[4]/label", SelectorType.XPATH, 30).getAttribute("class");
            assertEquals(isSelected, "selected");
        });
        And("^i enter \"([^\"]*)\" to financial details question$", (String arg0) -> {
            if (arg0.equals("No")) {
                arg0 = "N";
                findSelectAllRadioButtonsByValue(arg0);
            } else {
                arg0 = "Y";
                findSelectAllRadioButtonsByValue(arg0);
                enterText("data[insolvencyDetails]", Str.randomWord(150), SelectorType.ID);
            }
            clickByName("form-actions[saveAndContinue]");
        });
        Then("^a snapshot should be created in internal$", () -> {
            world.internalNavigation.logInAndNavigateToDocsTable();
            List<WebElement> docsAttach = listOfWebElements("//tbody/tr[*]/td[2]", SelectorType.XPATH);
            assertTrue(docsAttach.stream().anyMatch(d -> d.getText().contains("Application")));
        });
        When("^a new director has been added$", () -> {
            world.directorJourneySteps.addDirectorWithoutConvictions(firstName, lastName);
        });
        Given("^i add a director$", () -> {
            world.selfServeNavigation.navigateToPage("licence","directors");
            world.directorJourneySteps.addDirector(firstName, lastName);
        });
        Then("^i should have multiple directors on my application$", () -> {
            waitForTextToBePresent("Directors                ");
            List<WebElement> director = listOfWebElements("//*/tbody/tr[*]/td[1]/input", SelectorType.XPATH);
            long directors = director.size();
            MatcherAssert.assertThat(directors, greaterThan(1L));
            assertTrue(director.stream().anyMatch(d -> d.getAttribute("value").contains(firstName)));
        });
        When("^i add a new director$", () -> {
            world.directorJourneySteps.addDirector(firstName, lastName);
        });
        When("^i remove a director$", () -> {
            world.directorJourneySteps.removeDirector();
        });
        Then("^a task should not be created in internal$", () -> {
            world.internalNavigation.logInAndNavigateToTask();
            List<WebElement> director = listOfWebElements("//tbody", SelectorType.XPATH);
            assertFalse(director.stream().anyMatch(d -> d.getText().contains("Last director removed")));
        });

        When("^i remove a the last director$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.selfServeNavigation.navigateToPage("licence", "directors");
            world.directorJourneySteps.removeDirector();
        });

        Then("^a task should be created in internal$", () -> {
           world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin,world.updateLicence.adminUserEmailAddress);
           world.internalSearch.searchAndViewApplication();
        });

        Then("^a task is created in internal$", () -> {
            world.internalNavigation.logInAndNavigateToTask();
            List<WebElement> director = listOfWebElements("//tbody", SelectorType.XPATH);
            assertTrue(director.stream().anyMatch(d -> d.getText().contains("Last director removed")));
        });
    }
}