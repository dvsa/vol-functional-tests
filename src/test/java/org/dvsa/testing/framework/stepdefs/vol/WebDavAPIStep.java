package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.UserType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WebDavAPIStep extends BasePage {
    private final World world;
    Initialisation initialisation;
    private ValidatableResponse response;
    private String userId;
    public WebDavAPIStep(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("i have registered a new {string} user")
    public void iHaveRegisteredANewUser(String userRole) throws HttpException {
        this.userId = world.updateLicence.createInternalUser(userRole, UserType.INTERNAL.asString());
    }

    @When("i view their user details")
    public void iViewTheirUserDetails() throws HttpException {
        this.response = world.userDetails.getUserDetails(UserType.INTERNAL.asString(), userId, world.registerUser
                .getUserName(), world.registerUser.getEmailAddress());
    }

    @Then("the OS Type value should be null")
    public void theOSTypeValueShouldBeNull() {
        assertNull(response.extract().body().jsonPath().get("osType"));
    }

    @When("they attempt to update their OS version to {string}")
    public void theyAttemptToUpdateTheirOSVersionTo(String osVersion) throws HttpException {
        this.response = world.updateLicence.updateInternalUserDetails(this.userId, osVersion);
    }

    @When("i attempt to update their OS version to {string}")
    public void iAttemptToUpdateTheirOSVersionTo(String osVersion) throws HttpException {
        this.response = world.updateLicence.updateInternalUserDetails(this.userId, osVersion);
    }

    @Then("their OS Type value should be displaying {string}")
    public void theirOSTypeValueShouldBeDisplaying(String expectedOSVersion) {
        assertEquals(response.extract().body().jsonPath().get("osType.id"), expectedOSVersion);
    }

    @Then("their new OS Type should be {string}")
    public void theirNewOSTypeShouldBe(String expectedOSVersion) throws HttpException {
        this.response = world.userDetails.getUserDetails(UserType.INTERNAL.asString(), userId, world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        assertEquals(response.extract().body().jsonPath().get("osType.id"), expectedOSVersion);
    }
}