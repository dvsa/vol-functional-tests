package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.UserRoles;
import apiCalls.enums.UserType;
import cucumber.api.java8.En;
import io.restassured.response.ValidatableResponse;

public class CreateAdminUser implements En {

    private ValidatableResponse apiResponse;
    public CreateAdminUser(World world) {
        Given("^I create a new internal admin user$", () -> {
            world.updateLicence.createInternalUser(UserRoles.INTERNAL_ADMIN.asString(), UserType.INTERNAL.asString());
        });
        Then("^I should be able to login with my new credentials$", () -> {
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(),world.updateLicence.getInternalUserEmailAddress());
        });
    }
}