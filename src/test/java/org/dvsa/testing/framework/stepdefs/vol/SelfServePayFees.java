package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelfServePayFees extends BasePage {
    private World world;
    Initialisation initialisation;
    public SelfServePayFees(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

        @When("^I pay fees on self serve")
        public void iPayFeesOnSelfServe() {
        world.UIJourney.clickPay();
        world.feeAndPaymentJourney.payFee(null, "card");
    }

        @And("an internal user has granted my application")
        public void anInternalUserHasGrantedMyApplication() throws HttpException {
            world.grantApplication.grant();
            world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
        }

        @Then("^my licence should valid")
        public void myLicenceShouldValid() {
            clickByLinkText("Home");
            assertTrue(isTextPresent("Valid"));
        }
}