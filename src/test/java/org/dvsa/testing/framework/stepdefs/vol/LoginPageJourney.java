package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class LoginPageJourney extends BasePage {
    World world;
    String userId;

    public LoginPageJourney(World world) {
        this.world = world;
    }

    @When("I attempt to login with a username {string} with special characters")
    public void iAttemptToLoginWithAUsernameWithSpecialCharacters(String username) {
        world.globalMethods.signIn(username, world.configuration.config.getString("adminPassword"));
        this.userId = username;
    }

    @Then("I should be authenticated")
    public void iShouldBeAuthenticated() {
        isTextPresent(this.userId);
    }

    @And("redirected to the dashboard")
    public void redirectedToTheDashboard() {
        isTextPresent("Sign out");
        waitAndClick("Sign out", SelectorType.LINKTEXT);
    }

    @When("I attempt to login with a {string} with an email format")
    public void iAttemptToLoginWithAWithAnEmailFormat(String usernames) {
        world.globalMethods.signIn(usernames, world.configuration.config.getString("adminPassword"));
        this.userId = usernames;
    }

    @When("I attempt to login with {string} that share the same email address")
    public void iAttemptToLoginWithThatShareTheSameEmailAddress(String users) {
        world.globalMethods.signIn(users, world.configuration.config.getString("adminPassword"));
        this.userId = users;
    }
}
