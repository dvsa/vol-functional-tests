package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.OperatorType;
import io.cucumber.java8.En;
import io.restassured.response.ValidatableResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrantApplication implements En {

    private World world;
    private ValidatableResponse apiResponse;

    public GrantApplication(World world) {
        this.world = world;

        When("^I grant licence$", () -> {
            apiResponse = world.grantApplication.grantLicence();
        });
        Then("^the licence should be granted$", () -> {
            if (world.licenceCreation.isGoodsLicence()) {
                apiResponse = world.grantApplication.payGrantFees();
            }
            assertTrue(apiResponse.extract().response().asString().contains("documents\\/Licensing\\/Other_Documents"));
        });
    }
}