package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.*;


public class SignInPageSteps extends BasePage {
    private final World world;

    public SignInPageSteps(World world) {
        this.world = world;
    }

    @When("I sign in without inserting any credentials")
    public void iSignInWithoutInsertingAnyCredentials() {
        world.globalMethods.submit();
    }

    @Then("I should see the validation errors for sign in page")
    public void iShouldSeeTheValidationErrorsForSignInPage() {
        assertTrue(isTextPresent("Username: value is required"),"Current page does not display the username error message");
        assertTrue(isTextPresent("Password: value is required"),"Current page does not display the password error message");
    }
}