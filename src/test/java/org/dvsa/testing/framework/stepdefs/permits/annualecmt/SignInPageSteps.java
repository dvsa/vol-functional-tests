package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignInPageSteps extends BasePage implements En {
    public SignInPageSteps() {
        When("^I sign in without inserting any credentials$", GlobalMethods::submit);
        Then("^I should see the validation errors for sign in page$", () -> {
            assertTrue(isTextPresent("Username: value is required"),"Current page does not display the username error message");
            assertTrue(isTextPresent("Password: value is required"),"Current page does not display the password error message");
        });
    }
}