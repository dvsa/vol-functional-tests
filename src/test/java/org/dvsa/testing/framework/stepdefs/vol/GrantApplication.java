package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrantApplication extends BasePage{
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
