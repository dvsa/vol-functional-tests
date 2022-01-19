package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import com.fasterxml.jackson.databind.ser.Serializers;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrantApplication extends BasePage implements En {
    private final World world;
    private ValidatableResponse apiResponse;

    public GrantApplication(World world) {
        this.world = world;
    }

    @When("I grant licence")
    public void iGrantLicence() {
        apiResponse = world.grantApplication.grantLicence();
    }

    @Then("the licence should be granted")
    public void theLicenceShouldBeGranted() {
        if (world.licenceCreation.isGoodsLicence()) {
            apiResponse = world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
        }
        assertTrue(apiResponse.extract().response().asString().contains("documents\\/Licensing\\/Other_Documents"));
    }
}
