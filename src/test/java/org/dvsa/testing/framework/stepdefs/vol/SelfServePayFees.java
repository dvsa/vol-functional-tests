package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.Assert.*;

public class SelfServePayFees extends BasePage implements En {
    private World world;

    public SelfServePayFees(World world) {
        this.world = world;
    }

        @When("^I pay fees on self serve")
        public void iPayFeesOnSelfServe() {
        world.UIJourney.clickPay();
        world.feeAndPaymentJourney.payFee(null, "card");
    }

        @And("an internal user has granted my application")
        public void anInternalUserHasGrantedMyApplication() {
            world.grantApplication.grant();
            world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
        }

        @Then("^my licence should valid")
        public void myLicenceShouldValid() {
            clickByLinkText("Home");
            assertTrue(isTextPresent("Valid"));
        }
}
