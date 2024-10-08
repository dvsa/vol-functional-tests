package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.aws.s3.SecretsManager;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class LoginPageJourney extends BasePage {
    World world;
    String userId;
    Initialisation initialisation;
    public LoginPageJourney(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @When("I attempt to login with a username {string} with special characters")
    public void iAttemptToLoginWithAUsernameWithSpecialCharacters(String username) {
        if (isElementPresent("declarationRead", SelectorType.ID)) {
            waitAndClick("declarationRead", SelectorType.ID);
        }
        world.globalMethods.signIn(username, SecretsManager.getSecretValue("adminPassword"));
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
        world.globalMethods.signIn(usernames, SecretsManager.getSecretValue("adminPassword"));
        this.userId = usernames;
    }

    @When("I attempt to login with {string} that share the same email address")
    public void iAttemptToLoginWithThatShareTheSameEmailAddress(String users) {
        world.globalMethods.signIn(users, SecretsManager.getSecretValue("adminPassword"));
        this.userId = users;
    }
}
