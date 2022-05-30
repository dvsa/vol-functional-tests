package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.UserRoles;
import apiCalls.enums.UserType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class CreateAdminUser extends BasePage {

    private final World world;

    public CreateAdminUser(World world) {this.world = world;}

    @When("I create a new internal admin user")
    public void iCreateANewInternalAdminUser() {
        world.updateLicence.createInternalUser(UserRoles.INTERNAL_ADMIN.asString(), UserType.INTERNAL.asString());
    }

    @When("I create a new system admin user")
    public void iCreateANewSystemAdminUser() {
        world.updateLicence.createInternalUser(UserRoles.SYSTEM_ADMIN.asString(), UserType.INTERNAL.asString());
    }

    @Given("I create a new external user")
    public void iCreateANewExternalUser() {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
    }

    @Given("I create a new NI external user")
    public void iCreateANewNIExternalUser() {
        world.createApplication.setNiFlag("Y");
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
    }

    @Then("^I should be able to login with my new credentials$")
    public void iShouldBeAbleToLoginWithMyNewCredentials(){
        world.internalNavigation.logInAsAdmin();
    }
}