package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.newPages.permits.pages.CancellationPage;

public class WithdrawApplicationPageSteps implements En {
    public WithdrawApplicationPageSteps() {
        Then("^I should see the validation error message for the withdraw application page$", CancellationPage::hasErrorMessage);
    }
}
