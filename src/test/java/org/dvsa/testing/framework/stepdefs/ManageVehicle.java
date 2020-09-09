package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import enums.OperatorType;
import enums.UserRoles;

import java.net.MalformedURLException;

public class ManageVehicle {
    World world;

    public ManageVehicle(World world){
         this.world = world;
    }

    @Given("I have a new {string} application")
    public void iHaveANewApplication(String operatorType) {
        world.createLicence.setOperatorType(OperatorType.getEnum(operatorType).getName());
        world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
        world.APIJourneySteps.createPartialApplication();
    }

    @When("I navigate to manage vehicle page")
    public void iNavigateToManageVehiclePage() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToLogin( world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
        world.selfServeNavigation.navigateToPage("application", "vehicles");
    }

    @Then("the add vehicle page should display licence number")
    public void theAddVehiclePageShouldDisplayLicenceNumber() {

    }

    @And("choose to add a vehicle")
    public void chooseToAddAVehicle() {
        System.out.println("EXAMPLE");

    }

    @And("{string} heading")
    public void heading(String arg0) {
    }
}
