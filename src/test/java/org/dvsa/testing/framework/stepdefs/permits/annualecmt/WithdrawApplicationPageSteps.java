package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.external.permit.WithdrawApplicationPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.junit.Assert;

public class WithdrawApplicationPageSteps implements En {
    public WithdrawApplicationPageSteps() {
        Then("^I should see the validation error message for the withdraw application page$", () -> Assert.assertTrue(WithdrawApplicationPage.hasErrorMessagePresent()));
        And("^I'm viewing my saved ECMT application$", IrhpPermitsApplyPage::viewApplication);
    }
}
