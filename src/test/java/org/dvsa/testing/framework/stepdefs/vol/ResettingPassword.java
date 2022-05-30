package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.string.Str;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class ResettingPassword extends BasePage {
    private final World world;

    public ResettingPassword (World world) {this.world = world;}

    @And("i reset my password")
    public void iResetMyPassword() {
        clickByLinkText("Sign out");
        world.UIJourney.resettingExternalPassword();
        enterText(nameAttribute("input", "username"), SelectorType.CSS, world.registerUser.getUserName());
        isTextPresent("Failed");
        click(nameAttribute("input","submit"), SelectorType.CSS);
        while (isTextPresent("Failed")) {click(nameAttribute("input","submit"), SelectorType.CSS);
        }
    }

    @Then("i will receive a message to say my password has changed")
    public void iWillReceiveAMessageToSayMyPasswordHasChanged() {
        isTextPresent("We've sent you an email. Follow the link in the email to reset your password");
    }

    @Given("i try resetting my password")
    public void iTryResettingMyPassword() {
        world.UIJourney.resettingExternalPassword();
        enterText(nameAttribute("input", "username"), SelectorType.CSS, Str.randomWord(14));
        click(nameAttribute("input","submit"), SelectorType.CSS);
    }

    @Then("i will receive an error that username invalid")
    public void iWillReceiveAnErrorThatUsernameInvalid() {
        isTextPresent("Failed to reset your password");
    }

    @And("i then try reset my password")
    public void iThenTryResetMyPassword() {
        world.UIJourney.resettingExternalPassword();
        enterText(nameAttribute("input", "username"), SelectorType.CSS, Str.randomWord(14));
        click(nameAttribute("input","submit"), SelectorType.CSS);
    }

    @Then("i will receive an error for inactive account")
    public void iWillReceiveAnErrorForInactiveAccount() {
        isTextPresent("It looks like your account isn't active");
    }
}
