package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import apiCalls.enums.OperatorType;
import cucumber.api.java8.En;
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
            if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString())) {
                apiResponse = world.grantApplication.payGrantFees();
                assertTrue(apiResponse.extract().response().asString().contains("documents\\/Licensing\\/Other_Documents"));
            } else {
                assertTrue(apiResponse.extract().response().asString().contains("documents\\/Licensing\\/Other_Documents"));
            }
        });
    }
}