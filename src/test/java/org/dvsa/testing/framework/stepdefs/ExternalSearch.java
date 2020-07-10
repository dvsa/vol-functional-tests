package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.WebElement;

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
                    enterText("search", String.format("%s %s", world.createLicence.getForeName(), world.createLicence.getFamilyName()), SelectorType.NAME);
                    break;
            }
            clickByName("submit");
        });
        Then("^search results page addresses should only display address belonging to our post code$", () -> {
            String clippedCorrespondenceAddress = String.format("%s, %s, %s, %s",
                    world.createLicence.getAddressLine3(),
                    world.createLicence.getAddressLine4(),
                    world.createLicence.getTown(),
                    world.createLicence.getPostcode()
            );
            String clippedOperatingCentreAddress = String.format("%s, %s, %s, %s",
                    world.createLicence.getOperatingCentreAddressLine3(),
                    world.createLicence.getOperatingCentreAddressLine4(),
                    world.createLicence.getOperatingCentreTown(),
                    world.createLicence.getPostcode()
            );
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(clippedCorrespondenceAddress, 240, "KickOut reached. Correspondence and operating centre address external search failed.");
            WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", clippedCorrespondenceAddress), SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.createLicence.getOrganisationName()));
            assertTrue(tableRow.getText().contains(world.createLicence.getLicenceNumber()));
            tableRow = findElement(String.format("//tr[td[contains(text(),'%s')]]", clippedOperatingCentreAddress), SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.createLicence.getOrganisationName()));
            assertTrue(tableRow.getText().contains(world.createLicence.getLicenceNumber()));
        });
        Then("^search results page should display operator names containing our business name$", () -> {
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(world.createLicence.getOrganisationName(), 240, "KickOut reached. Operator name external search failed.");
            WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", world.createLicence.getOrganisationName()), SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.createLicence.getLicenceNumber()));
        });
        Then("^search results page should only display our licence number$", () -> {
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(world.createLicence.getLicenceNumber(), 240, "KickOut reached. Licence number external search failed.");
            WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", world.createLicence.getOrganisationName()), SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.createLicence.getLicenceNumber()));
        });
        Then("^search results page should display names containing our operator name$", () -> {
            String operatorName = String.format("%s %s", world.createLicence.getForeName(), world.createLicence.getFamilyName());
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(operatorName, 300, "KickOut reached. Operator name external search failed.");
            WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", operatorName), SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.createLicence.getOrganisationName()));
            assertTrue(tableRow.getText().contains(world.createLicence.getLicenceNumber()));
        });
    }
}