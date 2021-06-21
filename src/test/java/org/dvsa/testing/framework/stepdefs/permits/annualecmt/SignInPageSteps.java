package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.external.SignInPage;
import org.junit.Assert;

public class SignInPageSteps implements En {
    public SignInPageSteps() {
        When("^I sign in without inserting any credentials$", LoginPage::submit);
        Then("^I should see the validation errors for sign in page$", () -> {
            Assert.assertTrue("Current page does not display the username error message", SignInPage.hasUsernameErrorMessage());
            Assert.assertTrue("Current page does not display the password error message", SignInPage.hasPasswordErrorMessage());
        });
    }
}
