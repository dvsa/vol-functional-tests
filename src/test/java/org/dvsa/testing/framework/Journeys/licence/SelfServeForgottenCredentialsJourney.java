package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.driver.Browser;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebDriver;

public class SelfServeForgottenCredentialsJourney extends BasePage {

    public World world;

    public SelfServeForgottenCredentialsJourney(World world) {
        this.world = world;
    }

    public void forgottenPassword()
    {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        waitAndClick("Sign out", SelectorType.LINKTEXT);
        world.selfServeNavigation.navigateToLoginPage();
        waitAndClick("Forgotten your password?", SelectorType.LINKTEXT);
        waitAndEnterText("username", SelectorType.NAME, world.registerUser.getUserName());
        waitAndClick("submit", SelectorType.NAME);
        waitForPageLoad();
    }

}
