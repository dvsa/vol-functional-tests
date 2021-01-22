package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import apiCalls.enums.UserType;
import cucumber.api.java8.En;
import io.restassured.response.ValidatableResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WebDavAPIStep implements En {
    private ValidatableResponse response;
    private String userId;
    private String pid;

    public WebDavAPIStep(World world) {
        Given("^i have registered a new \"([^\"]*)\" user$", (String userRole) -> {
            this.userId = world.updateLicence.createInternalUser(userRole, UserType.INTERNAL.asString());
        });
        When("^i view their user details$", () -> {
            this.response = world.userDetails.getUserDetails(UserType.INTERNAL.asString(), userId);
            this.pid = response.extract().jsonPath().getString("pid");
        });
        Then("^the OS Type value should be null$", () -> {
            assertNull(response.extract().body().jsonPath().get("osType"));
        });
        When("^i attempt to update their OS version to \"([^\"]*)\"$", (String osVersion) -> {
            this.response = world.updateLicence.updateInternalUserDetails(this.userId, osVersion);
        });
        Then("^their OS Type value should be displaying \"([^\"]*)\"$", (String expectedOSVersion) -> {
            assertEquals(response.extract().body().jsonPath().get("osType.id"),expectedOSVersion);
        });
        When("^they attempt to update their OS version to \"([^\"]*)\"$", (String osVersion) -> {
            this.response = world.updateLicence.updateInternalUserDetails(this.userId, osVersion);
        });
        Then("^their new OS Type should be \"([^\"]*)\"$", (String expectedOSVersion) -> {
            this.response = world.userDetails.getUserDetails(UserType.INTERNAL.asString(), userId);
            assertEquals(response.extract().body().jsonPath().get("osType.id"),expectedOSVersion);
        });
    }
}