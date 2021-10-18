package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.UserType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WebDavAPIStep extends BasePage implements En {
    private final World world;
    private ValidatableResponse response;
    private String userId;
    private String pid;

    public WebDavAPIStep(World world) {this.world = world;}

    @Given("i have registered a new {string} user")
    public void iHaveRegisteredANewUser(String userRole) {
        this.userId = world.updateLicence.createInternalUser(userRole, UserType.INTERNAL.asString());
    }

    @When("i view their user details")
    public void iViewTheirUserDetails() {
        this.response = world.userDetails.getUserDetails(UserType.INTERNAL.asString(), userId);
        this.pid = response.extract().jsonPath().getString("pid");
    }

    @Then("the OS Type value should be null")
    public void theOSTypeValueShouldBeNull() {
        assertNull(response.extract().body().jsonPath().get("osType"));
    }

    @When("they attempt to update their OS version to {string}")
    public void theyAttemptToUpdateTheirOSVersionTo(String osVersion) {
        this.response = world.updateLicence.updateInternalUserDetails(this.userId, osVersion);
    }

    @When("i attempt to update their OS version to {string}")
    public void iAttemptToUpdateTheirOSVersionTo(String osVersion) {
        this.response = world.updateLicence.updateInternalUserDetails(this.userId, osVersion);
    }

    @Then("their OS Type value should be displaying {string}")
    public void theirOSTypeValueShouldBeDisplaying(String expectedOSVersion) {
        assertEquals(response.extract().body().jsonPath().get("osType.id"),expectedOSVersion);
    }

    @Then("their new OS Type should be {string}")
    public void theirNewOSTypeShouldBe(String expectedOSVersion) {
        this.response = world.userDetails.getUserDetails(UserType.INTERNAL.asString(), userId);
        assertEquals(response.extract().body().jsonPath().get("osType.id"),expectedOSVersion);
    }
}
