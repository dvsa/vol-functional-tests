package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;


public class GoodVarIncreaseVehicle extends BasePage implements En  {
    World world = new World();

    public GoodVarIncreaseVehicle(World world) {

        When("^A selfserve user increases the vehicle authority count$", () -> {
         world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
         clickByLinkText(world.createLicence.getLicenceNumber());
         world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createLicence.getNoOfVehiclesRequired() + 1 ));
         world.UIJourneySteps.changeVehicleAuth(String.valueOf(world.createLicence.getNoOfVehiclesRequired() + 1 ));
        });

        Then("^a status of update required should be shown next to financial evidence$", () -> {
        untilExpectedTextInElement("//*[@id=\"overview-item__financial_evidence\"]",  SelectorType.XPATH,"REQUIRES ATTENTION", 10);

        });

        When("^A selfserve user increases the vehicle required count by invalid characters$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq("+6");

        });
        Then("^An error message should appear$", () -> {
            isTextPresent("//*[@id=\"OperatingCentre\"]/fieldset[2]/div[1]/div/p",  20);
        });

        When("^A selfserve user increases the vehicle authority by invalid charecters$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createLicence.getNoOfVehiclesRequired()));
            world.UIJourneySteps.changeVehicleAuth("+6");
        });
        Then("^An error should appear$", () -> {
            isTextPresent("//*[@id=\"OperatingCentres\"]/fieldset[3]/div[1]/div/p",  20);
        });
    }
}