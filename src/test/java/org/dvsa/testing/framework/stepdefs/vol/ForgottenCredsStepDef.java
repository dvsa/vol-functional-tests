package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class ForgottenCredsStepDef extends BasePage {
    private World world;

    public ForgottenCredsStepDef(World world) {
        this.world = world;
    }

    @And("I have forgotten my password and want to reset")
    public void iHaveForgottenMyPasswordAndWantToReset() {
        world.selfServeForgottenCredentialsJourney.forgottenPassword();
    }

    @Then("I will be sent an email")
    public void iWillBeSentAnEmail() {

    }
}
