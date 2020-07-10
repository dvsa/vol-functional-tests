package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;;
import enums.UserRoles;
import io.restassured.response.ValidatableResponse;

public class CreateAdminUser implements En {

    private ValidatableResponse apiResponse;
    public CreateAdminUser(World world) {
        Given("^I create a new internal admin user$", () -> {
            world.updateLicence.createInternalUser(UserRoles.INTERNAL_ADMIN.getUserRoles(),UserRoles.INTERNAL.getUserRoles());
        });
        Then("^I should be able to login with my new credentials$", () -> {
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin,world.updateLicence.adminUserEmailAddress);
        });
    }
}