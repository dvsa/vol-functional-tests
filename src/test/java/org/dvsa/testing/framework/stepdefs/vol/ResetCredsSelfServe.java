package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResetCredsSelfServe extends BasePage{
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
        assertTrue(isTextPresent("Check your email"));
    }

    @And("I have forgotten my username and want it to be sent")
    public void iHaveForgottenMyUsernameAndWantItToBeSent() {
        world.forgottenCredsJourney.forgottenUsername();
    }

    @Then("I will be sent an email with my username")
    public void iWillBeSentAnEmailWithMyUsername() {
        assertEquals(world.configuration.getUsernameResetLink(), world.registerUser.getUserName());
    }
}
