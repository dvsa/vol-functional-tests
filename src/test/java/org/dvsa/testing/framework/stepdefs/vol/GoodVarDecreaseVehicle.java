package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

public class GoodVarDecreaseVehicle extends BasePage implements En {
    World world = new World();

    public GoodVarDecreaseVehicle(World world) {
        When("^A selfserve user decreases the vehicle authority count$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested() - 1));
            world.UIJourneySteps.changeVehicleAuth(String.valueOf(world.createApplication.getNoOfVehiclesRequested() - 1));
        });
        When("^A selfserve user decreases the vehicle required count by invalid characters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq("-6");
        });
        When("^A selfserve user decreases the vehicle authority by invalid charecters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested()));
            world.UIJourneySteps.changeVehicleAuth("-6");
        });
        Then("^a status of update required should be shown next to Review and declarations$", () -> {
            untilExpectedTextInElement("//*[@id='overview-item__undertakings']",  SelectorType.XPATH,"REQUIRES ATTENTION", 10);
        });
        And("^removes a vehicle because of new vehicle cap", () -> {
            world.selfServeNavigation.navigateToPage("variation", "Vehicles");
            world.UIJourneySteps.removeFirstVehicleOnVehiclePage();
            javaScriptExecutor("location.reload(true)");
            waitAndClick("//*[@class='govuk-back-link']",SelectorType.XPATH);
        });
    }
}
