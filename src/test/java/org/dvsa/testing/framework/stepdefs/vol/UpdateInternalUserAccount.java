package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javafx.concurrent.Worker;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.junit.Assert;

public class UpdateInternalUserAccount extends BasePage {
    private World world;

    public UpdateInternalUserAccount(World world) {this.world = world;}

    @When("I am on the Your Account page")
    public void iAmOnTheYourAccountPage() {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.AdminNavigation(AdminOption.YOUR_ACCOUNT);
    }

    @Given("I change my team")
    public void iChangeMyTeam() {
        world.UserAccountJourney.ChangeTeam();
    }

    @Then("my new team should be visible")
    public void myNewTeamShouldBeVisible() {
        String name = world.UserAccountJourney.teamName;
        Assert.assertTrue(isTextPresent(name));
    }

    @Given("I edit my personal details")
    public void iEditMyPersonalDetails() {
        world.UserAccountJourney.ChangeUserDetails();
    }

    @Then("my details should have updated")
    public void myDetailsShouldHaveUpdated() {
        Assert.assertTrue(isTextPresent(world.UserAccountJourney.getUserName()));
    }
}
