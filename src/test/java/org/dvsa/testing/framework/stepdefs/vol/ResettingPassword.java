package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ResettingPassword extends BasePage implements En {
    private final World world;

    public ResettingPassword (World world) {this.world = world;}

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
        enterText(nameAttribute("input", "username"), SelectorType.CSS, world.registerUser.getUserName());
        click(nameAttribute("input","submit"), SelectorType.CSS);
    }

    @Then("i will receive an error that username invalid")
    public void iWillReceiveAnErrorThatUsernameInvalid() {
        Assert.assertTrue(isTextPresent("Failed to reset your password"));
    }

    @And("I receive the reset password link via email")
    public void iReceiveTheResetPasswordLinkViaEmail() throws Exception {
        world.genericUtils.getResetPasswordLink();
        world.UIJourney.resetSelfServePassword();
        Assert.assertTrue(isTextPresent(world.registerUser.getForeName()));
    }
}
