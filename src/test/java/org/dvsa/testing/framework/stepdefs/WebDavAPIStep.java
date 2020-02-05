package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import io.cucumber.java8.En;
import enums.UserRoles;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.Journeys.APIJourneySteps;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WebDavAPIStep implements En {
    private ValidatableResponse response;
    private String userId;
    private String pid;

    public WebDavAPIStep(World world) {
        Given("^i have registered a new \"([^\"]*)\" user$", (String userRole) -> {
            this.userId = world.updateLicence.createInternalUser(userRole, UserRoles.INTERNAL.getUserRoles());
        });
        When("^i view their user details$", () -> {
            this.response = world.createLicence.getUserDetails(UserRoles.INTERNAL.getUserRoles(), userId, APIJourneySteps.adminApiHeader());
            this.pid = response.extract().jsonPath().getString("pid");
        });
        Then("^the OS Type value should be null$", () -> {
            assertNull(response.extract().body().jsonPath().get("osType"));
        });
        When("^i attempt to update their OS version to \"([^\"]*)\"$", (String osVersion) -> {
            this.response = world.updateLicence.updateInternalUserODetails(this.userId, osVersion,APIJourneySteps.adminApiHeader());
        });
        Then("^their OS Type value should be displaying \"([^\"]*)\"$", (String expectedOSVersion) -> {
            assertEquals(response.extract().body().jsonPath().get("osType.id"),expectedOSVersion);
        });
        When("^they attempt to update their OS version to \"([^\"]*)\"$", (String osVersion) -> {
            this.response = world.updateLicence.updateInternalUserODetails(this.userId, osVersion,this.pid);
        });
        Then("^their new OS Type should be \"([^\"]*)\"$", (String expectedOSVersion) -> {
            this.response = world.createLicence.getUserDetails(UserRoles.INTERNAL.getUserRoles(), userId, APIJourneySteps.adminApiHeader());
            assertEquals(response.extract().body().jsonPath().get("osType.id"),expectedOSVersion);
        });
    }
}