package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;

import static org.dvsa.testing.framework.Journeys.UIJourney.refreshPageWithJavascript;

public class GoodVarDecreaseVehicle extends BasePage implements En {
    World world = new World();

    public GoodVarDecreaseVehicle(World world) {
        When("^A selfserve user decreases the vehicle authority count$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            world.UIJourney.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested() - 1));
            world.UIJourney.changeVehicleAuth(String.valueOf(world.createApplication.getNoOfVehiclesRequested() - 1));
        });
        When("^A selfserve user decreases the vehicle required count by invalid characters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            world.UIJourney.changeVehicleReq("-6");
        });
        When("^A selfserve user decreases the vehicle authority by invalid charecters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            world.UIJourney.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested()));
            world.UIJourney.changeVehicleAuth("-6");
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
