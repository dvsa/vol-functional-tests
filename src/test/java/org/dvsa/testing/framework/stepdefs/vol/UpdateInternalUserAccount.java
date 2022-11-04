package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateInternalUserAccount extends BasePage {
    private World world;

    public UpdateInternalUserAccount(World world) {this.world = world;}

    @When("I am on the Your Account page")
    public void iAmOnTheYourAccountPage() {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.adminNavigation(AdminOption.YOUR_ACCOUNT);
    }

    @Given("I change my team")
    public void iChangeMyTeam() {
        world.UserAccountJourney.ChangeTeam();
    }

    @Then("my new team should be visible")
    public void myNewTeamShouldBeVisible() {
        String name = world.UserAccountJourney.teamName;
        assertTrue(isTextPresent(name));
    }

    @Given("I edit my personal details")
    public void iEditMyPersonalDetails() {
        world.UserAccountJourney.ChangeUserDetails();
    }


    @Then("my details should have updated")
    public void myDetailsShouldHaveUpdated() {
        assertTrue(isTextPresent(world.DataGenerator.getOperatorFamilyName()));
    }
}
