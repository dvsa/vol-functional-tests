package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.util.List;

public class LoginJourney extends BasePage {
    World world = new World();
    String userId;

    @When("I attempt to login with a username with special characters")
    public void iAttemptToLoginWithAUsernameWithSpecialCharacters(DataTable userTable) {
        List<String> specialCharacterUser = userTable.asList();
        for (String users : specialCharacterUser) {
            world.globalMethods.signIn(users, world.configuration.config.getString("internalNewPassword"));
            this.userId = users;
        }
    }

    @Then("I should be authenticated")
    public void iShouldBeAuthenticated() {
        isTextPresent(this.userId);
    }

    @And("redirected to the dashboard")
    public void redirectedToTheDashboard() {
        isTextPresent("logout");
    }

    @When("I attempt to login with a username with an email format")
    public void iAttemptToLoginWithAUsernameWithAnEmailFormat(DataTable userTable) {
        List<String> usersWithEmailFormatUsername = userTable.asList();
        for (String users : usersWithEmailFormatUsername) {
            world.globalMethods.signIn(users, world.configuration.config.getString("internalNewPassword"));
            this.userId = users;
        }
    }

    @When("I attempt to login with users that share the same email address")
    public void iAttemptToLoginWithUsersThatShareTheSameEmailAddress(DataTable userTable) {
        List<String> usersThatShareEmailAddresses = userTable.asList();
        for(String users : usersThatShareEmailAddresses){
            world.globalMethods.signIn(users, world.configuration.config.getString("internalNewPassword"));
            this.userId = users;
        }
    }
}