package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import io.cucumber.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.By;

public class GoodVarDecreaseVehicle extends BasePage implements En {
    World world = new World();

    public GoodVarDecreaseVehicle(World world) {
        When("^A selfserve user decreases the vehicle authority count$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createLicence.getNoOfVehiclesRequired() - 1));
            world.UIJourneySteps.changeVehicleAuth(String.valueOf(world.createLicence.getNoOfVehiclesRequired() - 1));
        });
        When("^A selfserve user decreases the vehicle required count by invalid characters$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq("-6");
        });
        When("^A selfserve user decreases the vehicle authority by invalid charecters$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createLicence.getNoOfVehiclesRequired()));
            world.UIJourneySteps.changeVehicleAuth("-6");
        });
        Then("^a status of update required should be shown next to Review and declarations$", () -> {
            untilExpectedTextInElement("//*[@id='overview-item__undertakings']",  SelectorType.XPATH,"REQUIRES ATTENTION", 10);
        });
        And("^removes a vehicle because of new vehicle cap", () -> {
            world.UIJourneySteps.navigateToVehiclesPage();
            world.UIJourneySteps.removeFirstVehicleOnVehiclePage();
            javaScriptExecutor("location.reload(true)");
            waitAndClick("//*[@class='back-link']",SelectorType.XPATH);
        });
    }
}
