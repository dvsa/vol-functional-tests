package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
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
                    enterText("search", SelectorType.NAME, world.createApplication.getPostCodeByTrafficArea());
                    break;
                case "business":
                    enterText("search", SelectorType.NAME, world.createApplication.getOrganisationName());
                    break;
                case "licence":
                    enterText("search", SelectorType.NAME, world.applicationDetails.getLicenceNumber());
                    break;
                case "person":
                    enterText("search", SelectorType.NAME, String.format("%s %s", world.createApplication.getDirectorForeName(), world.createApplication.getDirectorFamilyName()));
                    break;
            }
            clickByName("submit");
        });
        Then("^search results page addresses should only display address belonging to our post code$", () -> {
            String clippedCorrespondenceAddress = String.format("%s, %s, %s, %s",
                    world.createApplication.getCorrespondenceAddressLine3(),
                    world.createApplication.getCorrespondenceAddressLine4(),
                    world.createApplication.getCorrespondenceTown(),
                    world.createApplication.getCorrespondencePostCode()
            );
            String clippedOperatingCentreAddress = String.format("%s, %s, %s, %s",
                    world.createApplication.getOperatingCentreAddressLine3(),
                    world.createApplication.getOperatingCentreAddressLine4(),
                    world.createApplication.getOperatingCentreTown(),
                    world.createApplication.getOperatingCentrePostCode()
            );
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(clippedCorrespondenceAddress, 240, "KickOut reached. Correspondence and operating centre address external search failed.");
            WebElement tableRow = findElement(String.format("//tr[td[contains(text(),'%s')]]", clippedCorrespondenceAddress), SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.createApplication.getOrganisationName()));
            assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
            tableRow = findElement(String.format("//tr[td[contains(text(),'%s')]]", clippedOperatingCentreAddress), SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.createApplication.getOrganisationName()));
            assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
        });
        Then("^search results page should display operator names containing our business name$", () -> {
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(world.createApplication.getOrganisationName(), 240, "KickOut reached. Operator name external search failed.");
            WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", world.createApplication.getOrganisationName()), SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
        });
        Then("^search results page should only display our licence number$", () -> {
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(world.applicationDetails.getLicenceNumber(), 240, "KickOut reached. Licence number external search failed.");
            WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", world.createApplication.getOrganisationName()), SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
        });
        Then("^search results page should display names containing our operator name$", () -> {
            String operatorName = String.format("%s %s", world.createApplication.getDirectorForeName(), world.createApplication.getDirectorFamilyName());
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(operatorName, 300, "KickOut reached. Operator name external search failed.");
            WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", operatorName), SelectorType.XPATH);
            assertTrue(tableRow.getText().contains(world.createApplication.getOrganisationName()));
            assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
        });
        And("^i login as a partner user$", () -> {
            String user = world.configuration.config.getString("partnerUser");
            String password = world.configuration.config.getString("partnerUserPassword");
            String externalURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
            get(externalURL);
            world.globalMethods.signIn(user,password);
        });
    }
}