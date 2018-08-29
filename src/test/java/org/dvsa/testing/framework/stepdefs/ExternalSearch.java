package org.dvsa.testing.framework.stepdefs;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExternalSearch extends BasePage implements En {
    World world = new World();

    public ExternalSearch(World world) {
        Given("^I am on the external search page$", () -> {
            world.journeySteps.externalSearch();
        });
        When("^I search for a lorry and bus operator by \"([^\"]*)\"$", (String arg0) -> {
            world.genericUtils.findAllRadioButtons(arg0);
            switch (arg0) {
                case "address":
                    enterText("search", world.createLicence.getTown(), SelectorType.NAME);
                    break;
                case "business":
                    enterText("search", world.createLicence.getBusinessName(), SelectorType.NAME);
                    break;
                case "licence":
                    enterText("search", world.createLicence.getLicenceNumber(), SelectorType.NAME);
                    break;
                case "person":
                    enterText("search", world.createLicence.getForeName(), SelectorType.NAME);
                    break;
            }
            clickByName("submit");
        });
        Then("^search results page addresses should only contain the correct address$", () -> {
            assertTrue(world.genericUtils.checkForValuesInTable(world.createLicence.getTown().toUpperCase()));
        });
        Then("^search results page should display operator names containing our business name$", () -> {
            assertTrue(world.genericUtils.checkForValuesInTable(world.createLicence.getBusinessName().toUpperCase()));
        });
        Then("^search results page should only display our licence number$", () -> {
            do {
                clickByName("submit");
            } while (!isTextPresent(world.createLicence.getBusinessName(), 30));
            assertTrue(world.genericUtils.checkForValuesInTable(world.createLicence.getLicenceNumber()));
        });
        Then("^search results page should display names containing our operator name$", () -> {
            assertTrue(world.genericUtils.checkForValuesInTable(world.createLicence.getFamilyName()));
        });
    }
}