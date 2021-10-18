package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;

public class GoodVarDecreaseVehicle extends BasePage implements En {
    World world = new World();

    public GoodVarDecreaseVehicle(World world) {
        When("^A selfserve user decreases the vehicle authority count$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
            world.UIJourney.changeLicenceForVariation();
            world.operatingCentreJourney.updateOperatingCentreAuthorisation(String.valueOf(world.createApplication.getNoOfAddedHgvVehicles() - 1));
            String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
            world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(String.valueOf(world.createApplication.getNoOfAddedHgvVehicles() - 1), null, currentTrailerTotalAuthority);
        });
        When("^A selfserve user decreases the vehicle required count by invalid characters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
            world.UIJourney.changeLicenceForVariation();
            world.operatingCentreJourney.updateOperatingCentreAuthorisation("-6");
        });
        When("^A selfserve user changes the vehicle authority by invalid characters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
            world.UIJourney.changeLicenceForVariation();
            String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
            world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority("-6", null, currentTrailerTotalAuthority);
        });
        Then("^a status of update required should be shown next to Review and declarations$", () -> {
            waitForElementToBePresent("//*[@id='overview-item__undertakings']");
        });
        And("^removes a vehicle because of new vehicle cap", () -> {
            world.selfServeNavigation.navigateToPage("variation", "Vehicles");
            world.UIJourney.removeFirstVehicleOnVehiclePage();
            refreshPageWithJavascript();
            waitAndClick("//*[@class='govuk-back-link']",SelectorType.XPATH);
        });
    }
}
