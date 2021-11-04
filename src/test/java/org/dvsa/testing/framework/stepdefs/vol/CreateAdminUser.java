package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.UserRoles;
import apiCalls.enums.UserType;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class CreateAdminUser extends BasePage implements En {

    private final World world;

    public CreateAdminUser(World world) {this.world = world;}

    @When("I create a new internal admin user")
    public void iCreateANewInternalAdminUser() {
        world.updateLicence.createInternalUser(UserRoles.INTERNAL_ADMIN.asString(), UserType.INTERNAL.asString());
    }

    @Given("I create a new external user")
    public void iCreateANewExternalUser() {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
    }

    @Then("^I should be able to login with my new credentials$")
    public void iShouldBeAbleToLoginWithMyNewCredentials(){
        world.internalNavigation.logInAsAdmin();
    };


}
