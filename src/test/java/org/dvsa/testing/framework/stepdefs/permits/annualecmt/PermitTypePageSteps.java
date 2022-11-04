package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitTypePage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermitTypePageSteps implements En {
    public PermitTypePageSteps(World world) {
        And("^I am on the permit type page$", () -> {
            clickToPermitTypePage(world);
        });
        When("^continue button is selected without confirming the permit type$", PermitTypePage::clickContinue);
        Then("^the permit type page error message is displayed$", () -> {
            String errorText = PermitTypePage.getErrorText();
            assertEquals("You must select an option to continue", errorText);
        });
        When("^I click cancel button$", PermitTypePage::clickCancel);

    }
}
