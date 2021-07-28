package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.string.Str;
import io.cucumber.java8.En;;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;

public class ResettingPassword extends BasePage implements En {
    public ResettingPassword(World world) {
        Then("^i will receive a message to say my password has changed$", () -> {
            isTextPresent("We've sent you an email. Follow the link in the email to reset your password");
        });

        Then("^i will receive an error that username invalid$", () -> {
            isTextPresent("Failed to reset your password");
        });

        And("^i reset my password$", () -> {
            String env = System.getProperty("env");
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            clickByLinkText("Sign out");
            world.UIJourney.resettingExternalPassword();
            enterText(nameAttribute("input", "username"), SelectorType.CSS, world.registerUser.getUserName());
            isTextPresent("Failed");
            click(nameAttribute("input","submit"), SelectorType.CSS);
                while (isTextPresent("Failed")) {
                click(nameAttribute("input","submit"), SelectorType.CSS);
            }
        });
        And("^i try resetting my password$", () -> {
            world.UIJourney.resettingExternalPassword();
            enterText(nameAttribute("input", "username"), SelectorType.CSS, Str.randomWord(14));
            click(nameAttribute("input","submit"), SelectorType.CSS);
        });
        And("^i then try reset my password$", () -> {
            world.UIJourney.resettingExternalPassword();
            enterText(nameAttribute("input", "username"), SelectorType.CSS, world.registerUser.getUserName());
            isTextPresent("Failed");
            click(nameAttribute("input","submit"), SelectorType.CSS);
            isTextPresent("Failed");
            click(nameAttribute("input","submit"), SelectorType.CSS);
        });
        Then("^i will receive an error for inactive account$", () -> {
            isTextPresent("It looks like your account isn't active");
        });
    }
}