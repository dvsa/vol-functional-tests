package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;

public class CreateNewInternalUser extends BasePage{
    private  World world;

    public CreateNewInternalUser (World world) { this.world=world; }

    @And("I have logged into the internal application")
    public void iHaveLoggedIntoTheInternalApplication() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
    }

    @Then("I add a new User")
    public void iAddANewUser() {
       world.UIJourney.addNewInternalUser();
    }

    @Then("User should be created")
    public void userShouldBeCreated()
    {
        world.internalSearchJourney.searchUser();
        Assert.assertTrue(isTextPresent(world.DataGenerator.getOperatorUserEmail()));
    }
}