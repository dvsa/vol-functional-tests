package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import static org.junit.Assert.*;

public class SelfServePayFees extends BasePage implements En {
    private World world;

    public SelfServePayFees(World world) {
        When("^I pay fees on self serve$", () -> {
            click("//*[@id='form-actions[pay]']", SelectorType.XPATH);
            world.feeAndPaymentJourneySteps.payFee(null, "card");
        });
        And("^an internal user has granted my application$", () -> {
            world.grantApplication.grant();
            world.grantApplication.payGrantFees();
        });
        Then("^my licence should valid$", () -> {
            clickByLinkText("Home");
            assertTrue(isTextPresent("Valid",80));
        });
    }
}
