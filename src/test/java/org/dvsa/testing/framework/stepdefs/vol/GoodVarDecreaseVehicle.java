package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class GoodVarDecreaseVehicle extends BasePage implements En {
    private final World world;

    public GoodVarDecreaseVehicle (World world) {this.world = world;}

    @When("A selfserve user decreases the vehicle authority count")
    public void aSelfserveUserDecreasesTheVehicleAuthorityCount() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        world.UIJourney.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested() - 1));
        world.UIJourney.changeVehicleAuth(String.valueOf(world.createApplication.getNoOfVehiclesRequested() - 1));
    }

    @And("removes a vehicle because of new vehicle cap")
    public void removesAVehicleBecauseOfNewVehicleCap() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        world.UIJourney.changeVehicleReq("-6");
    }

    @Then("a status of update required should be shown next to Review and declarations")
    public void aStatusOfUpdateRequiredShouldBeShownNextToReviewAndDeclarations() {
        waitForElementToBePresent("//*[@id='overview-item__undertakings']");
    }

    @When("A selfserve user decreases the vehicle required count by invalid characters")
    public void aSelfserveUserDecreasesTheVehicleRequiredCountByInvalidCharacters() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        world.UIJourney.changeVehicleReq("-6");

    }

    @When("A selfserve user decreases the vehicle authority by invalid charecters")
    public void aSelfserveUserDecreasesTheVehicleAuthorityByInvalidCharecters() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        world.UIJourney.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested()));
        world.UIJourney.changeVehicleAuth("-6");
    }
}
