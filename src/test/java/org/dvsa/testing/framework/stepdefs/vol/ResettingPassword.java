package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.string.Str;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ResettingPassword extends BasePage {
    private final World world;

    public ResettingPassword(World world) {
        this.world = world;
    }

    @And("i reset my password")
    public void iResetMyPassword() {
        clickByLinkText("Sign out");
        world.UIJourney.resettingExternalPassword();
        enterText(nameAttribute("input", "username"), SelectorType.CSS, world.registerUser.getUserName());
        waitAndClick("auth.forgot-password.button", SelectorType.ID);
        while (isTextPresent("Failed")) {click(nameAttribute("input","submit"), SelectorType.CSS);
        }
    }

    @Given("i try resetting my password")
    public void iTryResettingMyPassword() {
        world.UIJourney.resettingExternalPassword();
        enterText(nameAttribute("input", "username"), SelectorType.CSS, world.registerUser.getUserName().concat(Str.randomWord(4)));
        click(nameAttribute("input","submit"), SelectorType.CSS);
    }

    @Then("i will receive an error that username invalid")
    public void iWillReceiveAnErrorThatUsernameInvalid(){
        assertTrue(isTextPresent("Failed to reset your password"));
    }

    @And("I receive the reset password link via email")
    public void iReceiveTheResetPasswordLinkViaEmail() throws InterruptedException {
        world.genericUtils.getResetPasswordLink(world.configuration.getPasswordResetLink());
        world.UIJourney.resetSelfServePassword();
        assertTrue(isTextPresent(world.registerUser.getForeName()));
    }
}