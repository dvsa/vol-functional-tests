package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;

public class ResetCredsSelfServe extends BasePage implements En {
    private final World world;

    public ResetCredsSelfServe(World world)  {
        this.world = world;
    }

    @And("I have forgotten my password and want to reset")
    public void iHaveForgottenMyPasswordAndWantToReset() {
        world.forgottenCredsJourney.forgottenPassword();
    }

    @Then("I will be sent an email with my password")
    public void iWillBeSentAnEmailWithMyPassword() {
        Assert.assertTrue(isTextPresent("Check your email"));
    }

    @And("I have forgotten my username and want it to be sent")
    public void iHaveForgottenMyUsernameAndWantItToBeSent() {
        world.forgottenCredsJourney.forgottenUsername();
    }

    @Then("I will be sent an email with my username")
    public void iWillBeSentAnEmailWithMyUsername() {
        Assert.assertEquals(world.configuration.getUsernameResetLink(), world.registerUser.getUserName());
    }
}
