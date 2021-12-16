package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRegistrationJourney extends BasePage {
    private World world;

    public UserRegistrationJourney(World world){
        this.world = world;
    }

    public void registerUserWithNoLicence() {
        world.selfServeNavigation.navigateToLoginPage();
        world.selfServeNavigation.navigateToCreateAnAccount();
        world.UIJourney.addNewOperator(null, false);
        assertTrue(isTextPresent(world.DataGenerator.getOperatorUserEmail()));
    }
}