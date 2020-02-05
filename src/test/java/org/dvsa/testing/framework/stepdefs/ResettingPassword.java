package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.string.Str;
import io.cucumber.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

public class ResettingPassword extends BasePage implements En {
    public ResettingPassword(World world) {
        Then("^i will receive a message to say my password has changed$", () -> {
          isTextPresent("We've sent you an email. Follow the link in the email to reset your password", 30);
        });

        Then("^i will receive an error that username invalid$", () -> {
            isTextPresent("Failed to reset your password", 30);
        });

        And("^i reset my password$", () -> {
            String env = System.getProperty("env");
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            clickByLinkText("Sign out");
            world.UIJourneySteps.resettingExternalPassword();
            enterField(nameAttribute("input", "username"), world.createLicence.getLoginId());
            isTextPresent("Failed", 30);
            click(nameAttribute("input","submit"), SelectorType.CSS);
                while (isTextPresent("Failed", 30)) {
                click(nameAttribute("input","submit"), SelectorType.CSS);
            }
        });
        And("^i try resetting my password$", () -> {
            world.UIJourneySteps.resettingExternalPassword();
            enterField(nameAttribute("input", "username"), Str.randomWord(14));
            click(nameAttribute("input","submit"), SelectorType.CSS);
        });
        And("^i then try reset my password$", () -> {
            world.UIJourneySteps.resettingExternalPassword();
            enterField(nameAttribute("input", "username"), world.createLicence.getLoginId());
            isTextPresent("Failed", 30);
            click(nameAttribute("input","submit"), SelectorType.CSS);
            isTextPresent("Failed", 30);
            click(nameAttribute("input","submit"), SelectorType.CSS);
        });
        Then("^i will receive an error for inactive account$", () -> {
            isTextPresent("It looks like your account isn't active", 30);
        });
    }
}
