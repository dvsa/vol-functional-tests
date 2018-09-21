package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExternalSearch extends BasePage implements En {
    World world = new World();

    public ExternalSearch(World world) {
        Given("^I am on the external search page$", () -> {
            world.UIJourneySteps.navigateToExternalSearch();
        });
        When("^I search for a lorry and bus operator by \"([^\"]*)\"$", (String arg0) -> {
            findSelectAllRadioButtonsByValue(arg0);
            switch (arg0) {
                case "address":
                    enterText("search", world.createLicence.getPostcode(), SelectorType.NAME);
                    break;
                case "business":
                    enterText("search", world.createLicence.getBusinessName(), SelectorType.NAME);
                    break;
                case "licence":
                    enterText("search", world.createLicence.getLicenceNumber(), SelectorType.NAME);
                    break;
                case "person":
                    enterText("search", world.createLicence.getFamilyName(), SelectorType.NAME);
                    break;
            }
            clickByName("submit");
        });
        Then("^search results page addresses should only display address belonging to our post code$", () -> {
            assertTrue(checkForValuesInTable(world.createLicence.getPostcode().toUpperCase()));
        });

        Then("^search results page should display operator names containing our business name$", () -> {
            assertTrue(checkForValuesInTable(world.createLicence.getBusinessName().toUpperCase()));
        });
        Then("^search results page should only display our licence number$", () -> {
            do {
                clickByName("submit");
            } while (!isTextPresent(world.createLicence.getBusinessName(), 30));
            assertTrue(checkForValuesInTable(world.createLicence.getLicenceNumber()));
        });
        Then("^search results page should display names containing our operator name$", () -> {
            assertTrue(checkForValuesInTable(world.createLicence.getFamilyName().toUpperCase()));
        });
        Then("^search results page should not display addresses which were not searched for$", () -> {
            assertFalse(checkForValuesInTable("Swansea"));
        });
        Then("^search results page should only display operator names containing our business name$", () -> {
            assertFalse(checkForValuesInTable("Jones-Made-Up"));
        });
        Then("^search results page should not display any other licence number$", () -> {
            do {
                clickByName("submit");
            } while (!isTextPresent(world.createLicence.getBusinessName(), 30));
            assertFalse(checkForValuesInTable("OB0000123"));
        });
        Then("^search results page should only display names containing our operator name$", () -> {
            assertFalse(checkForValuesInTable("DVSA"));
        });
    }
}