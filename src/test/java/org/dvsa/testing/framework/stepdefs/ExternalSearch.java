package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExternalSearch extends BasePage implements En {
    private World world;

    public ExternalSearch(World world) {
        this.world = world;

        Given("^I am on the external search page$", () -> {
            world.selfServeNavigation.navigateToSearch();
        });
        When("^I search for a lorry and bus operator by \"([^\"]*)\"$", (String arg0) -> {
            findSelectAllRadioButtonsByValue(arg0);
            switch (arg0) {
                case "address":
                    enterText("search", world.createLicence.getPostcode(), SelectorType.NAME);
                    break;
                case "business":
                    enterText("search", world.createLicence.getOrganisationName(), SelectorType.NAME);
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
            boolean correspondenceAddressNotFound = true;
            long kickOut = System.currentTimeMillis() + 240000;
            String fullCorrespondenceAddress = String.format("%s, %s, %s, %s",
                    world.createLicence.getAddressLine3(),
                    world.createLicence.getAddressLine4(),
                    world.createLicence.getTown(),
                    world.createLicence.getPostcode()
            );
            String fullOperatingCentreAddress = String.format("%s, %s, %s, %s",
                    world.createLicence.getOperatingCentreAddressLine3(),
                    world.createLicence.getOperatingCentreAddressLine4(),
                    world.createLicence.getOperatingCentreTown(),
                    world.createLicence.getPostcode()
            );
            while (correspondenceAddressNotFound && System.currentTimeMillis() < kickOut) {
                correspondenceAddressNotFound = !isTextPresent(fullCorrespondenceAddress, 10);
                click("submit", SelectorType.ID);
                waitForPageLoad();
            }
//            if (kickOut > System.currentTimeMillis()){
//                throw new Exception("KickOut reached. Postcode external search failed.");
//            }
            WebElement tableRow = findElement(String.format("//tr[td[contains(text(),'%s')]]", fullCorrespondenceAddress) , SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.createLicence.getOrganisationName()));
            assertTrue(tableRow.getText().contains(world.createLicence.getLicenceNumber()));
            tableRow = findElement(String.format("//tr[td[contains(text(),'%s')]]", fullOperatingCentreAddress) , SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.createLicence.getOrganisationName()));
            assertTrue(tableRow.getText().contains(world.createLicence.getLicenceNumber()));
        });

        Then("^search results page should display operator names containing our business name$", () -> {
            assertTrue(checkForFullMatch(world.createLicence.getOrganisationName()));
        });
        Then("^search results page should only display our licence number$", () -> {
            do {
                clickByName("submit");
            } while (!isTextPresent(world.createLicence.getOrganisationName(), 30));
            assertTrue(checkForFullMatch(world.createLicence.getLicenceNumber()));
        });
        Then("^search results page should display names containing our operator name$", () -> {
            assertTrue(checkForPartialMatch(world.createLicence.getFamilyName().toUpperCase()));
        });
        Then("^search results page should not display addresses which were not searched for$", () -> {
            assertFalse(checkForFullMatch("Swansea"));
        });
        Then("^search results page should only display operator names containing our business name$", () -> {
            assertFalse(checkForFullMatch("Jones-Made-Up"));
        });
        Then("^search results page should not display any other licence number$", () -> {
            do {
                clickByName("submit");
            } while (!isTextPresent(world.createLicence.getOrganisationName(), 30));
            assertFalse(checkForFullMatch("OB0000123"));
        });
        Then("^search results page should only display names containing our operator name$", () -> {
            assertFalse(checkForFullMatch("DVSA"));
        });
    }
}