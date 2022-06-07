package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import static org.junit.Assert.assertTrue;


import org.openqa.selenium.WebElement;

public class ExternalSearch extends BasePage implements En {
    private final World world;

    public ExternalSearch(World world) {this.world = world;}

    @And("i login as a partner user")
    public void iLoginAsAPartnerUser() {
        String user = world.configuration.config.getString("partnerUser");
        String password = world.configuration.config.getString("partnerUserPassword");
        if(getDriver().getCurrentUrl().contains("dashboard")){
            clickByLinkText("Sign out");
        }
        String externalURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
        get(externalURL);
        world.globalMethods.signIn(user,password);
        waitAndClick("Lorry and bus operators",SelectorType.PARTIALLINKTEXT);
    }

    @And("I am on the external search page")
    public void iAmOnTheExternalSearchPage() {
        world.selfServeNavigation.navigateToFindLorryAndBusOperatorsSearch();
    }

    @When("I search for a lorry and bus operator by {string}")
    public void iSearchForALorryAndBusOperatorBy(String arg0) {
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
    }

    @Then("search results page addresses should only display address belonging to our post code")
    public void searchResultsPageAddressesShouldOnlyDisplayAddressBelongingToOurPostCode() {
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
        }

        @Then("search results page should display operator names containing our business name")
    public void searchResultsPageShouldDisplayOperatorNamesContainingOurBusinessName() {
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(world.createApplication.getOrganisationName(), 240, "KickOut reached. Operator name external search failed.");
    }

    @And("I am able to view the applications license number")
    public void iAmAbleToViewTheApplicationsLicenseNumber() {
        WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", world.createApplication.getOrganisationName()), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
    }

    @Then("search results page should only display our licence number")
    public void searchResultsPageShouldOnlyDisplayOurLicenceNumber() {
        world.selfServeNavigation.clickSearchWhileCheckingTextPresent(world.applicationDetails.getLicenceNumber(), 240, "KickOut reached. Licence number external search failed.");
    }

    @And("I am able to view the license number")
    public void iAmAbleToViewTheLicenseNumber() {
        WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", world.createApplication.getOrganisationName()), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
    }


    @Then("search results page should display names containing our operator name")
    public void searchResultsPageShouldDisplayNamesContainingOurOperatorName() {
        String operatorName = String.format("%s %s", world.createApplication.getDirectorForeName(), world.createApplication.getDirectorFamilyName());
        world.selfServeNavigation.clickSearchWhileCheckingTextPresent(operatorName, 300, "KickOut reached. Operator name external search failed.");
        WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", operatorName), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(world.createApplication.getOrganisationName()));
        assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
    }
}
