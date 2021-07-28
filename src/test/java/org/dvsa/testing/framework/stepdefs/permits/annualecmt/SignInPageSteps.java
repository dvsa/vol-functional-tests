package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.lib.newPages.BasePage;
import org.junit.Assert;

public class SignInPageSteps extends BasePage implements En {
    public SignInPageSteps() {
        When("^I sign in without inserting any credentials$", GlobalMethods::submit);
        Then("^I should see the validation errors for sign in page$", () -> {
            Assert.assertTrue("Current page does not display the username error message", isTextPresent("Username: value is required"));
            Assert.assertTrue("Current page does not display the password error message", isTextPresent("Password: value is required"));
        });
    }
}
