package org.dvsa.testing.framework.stepdefs.vol;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginJourney {
    @When("I attempt to login with a username with special characters")
    public void iAttemptToLoginWithAUsernameWithSpecialCharacters() {
    }

    @Then("I should be authenticated")
    public void iShouldBeAuthenticated() {
    }

    @And("redirected to the dashboard")
    public void redirectedToTheDashboard() {
    }

    @When("I attempt to login with a username with an email format")
    public void iAttemptToLoginWithAUsernameWithAnEmailFormat() {

    }

    @When("I attempt to login with users that share the same email address")
    public void iAttemptToLoginWithUsersThatShareTheSameEmailAddress() {
    }
}