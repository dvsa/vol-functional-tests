package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.string.Str;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class ResettingPassword extends BasePage implements En {
    private final World world;

    public ResettingPassword (World world) {this.world = world;}

    @And("i reset my password")
    public void iResetMyPassword() {
        String env = System.getProperty("env");
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
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
