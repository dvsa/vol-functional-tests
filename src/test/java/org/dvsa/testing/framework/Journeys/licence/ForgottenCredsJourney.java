package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class ForgottenCredsJourney extends BasePage {
    private World world;

    public ForgottenCredsJourney(World world) {
        this.world = world;
    }

    public void createAccountSignOut()
    {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        waitAndClick("Sign out", SelectorType.LINKTEXT);
        world.selfServeNavigation.navigateToLoginPage();
    }

    public void forgottenPassword()
    {
       createAccountSignOut();
        waitAndClick("Forgotten your password?", SelectorType.LINKTEXT);
        waitAndEnterText("auth.forgot-password.username", SelectorType.ID, world.registerUser.getUserName());
        waitAndClick("auth.forgot-password.button", SelectorType.ID);
        waitForPageLoad();
        System.out.println(world.configuration.getPasswordResetLink());
    }

    public void forgottenUsername()
    {
        createAccountSignOut();
        waitAndClick("Forgotten your username?", SelectorType.LINKTEXT);
        waitAndEnterText("fields[licenceNumber]", SelectorType.ID, world.applicationDetails.getLicenceNumber());
        waitAndEnterText("fields[emailAddress]", SelectorType.ID, world.registerUser.getEmailAddress());
        waitAndClick("form-actions[submit]", SelectorType.ID);
        System.out.println(world.configuration.getUsernameResetLink());





    }


}
