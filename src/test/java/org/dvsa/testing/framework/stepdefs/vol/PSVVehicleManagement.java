package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;

public class PSVVehicleManagement {
    private final World world;

    public PSVVehicleManagement(World world) {
        this.world = world;
    }

    @When("I add a PSV vehicle with make\\/model {string}")
    public void addPsvVehicle(String makeModel) {
        world.psvVehicleManagementJourney.addVehicle(makeModel);
    }

    @Then("the added PSV vehicle should persist after reloading the licence")
    public void addedPsvVehicleShouldPersistAfterReloadingTheLicence() {
        world.psvVehicleManagementJourney.verifyLastAddedVehiclePersistsAfterReload();
    }

    @When("I add two PSV vehicles with make\\/models {string} and {string}")
    public void addTwoPsvVehicles(String firstMakeModel, String secondMakeModel) {
        world.psvVehicleManagementJourney.addTwoVehicles(firstMakeModel, secondMakeModel);
    }

    @And("I remove the first PSV vehicle I added")
    public void removeFirstPsvVehicleIAdded() {
        world.psvVehicleManagementJourney.removeFirstAddedVehicle();
    }

    @Then("the removed PSV vehicle should be absent and the other added vehicle should remain")
    public void removedPsvVehicleShouldBeAbsentAndOtherVehicleShouldRemain() {
        world.psvVehicleManagementJourney.verifyRemovedVehicleIsAbsentAndOtherVehicleRemains();
    }

    @When("I export the PSV vehicle list")
    public void exportPsvVehicleList() throws Exception {
        world.psvVehicleManagementJourney.exportVehicleList();
    }

    @Then("the PSV export should contain the added vehicle's registration and make\\/model")
    public void psvExportShouldContainAddedVehicleRegistrationAndMakeModel() {
        world.psvVehicleManagementJourney.verifyExportContainsLastAddedVehicle();
    }
}
