package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitTypePage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;

public class PermitTypePageSteps implements En {
    public PermitTypePageSteps(OperatorStore operatorStore, World world) {
        And("^I am on the permit type page$", () -> {
            clickToPermitTypePage(world);
        });
        Then ("continue button is selected without confirming the permit type$", PermitTypePage::clickContinue);
        Then ("^the error message is displayed in the permit type page$", () -> {
            String errorText = PermitTypePage.getErrorText();
            assertEquals("You must select an option to continue", errorText);
        });
        Then ("^I click cancel button$", PermitTypePage::clickCancel);

    }
}
