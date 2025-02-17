package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRegistrationJourney extends BasePage {
    private final World world;

    public UserRegistrationJourney(World world) {
        this.world = world;
    }

    public void registerUserWithNoLicence() {
        world.selfServeNavigation.navigateToLoginPage();
        world.selfServeNavigation.navigateToCreateAnAccount();
        world.selfServeUIJourney.addNewOperator();
        assertTrue(isTextPresent(world.DataGenerator.getOperatorUserEmail()));
    }

    public void navigateAndLogIntoSelfServiceWithExistingUser() {
        world.userRegistrationJourney.registerUserWithNoLicence();
        world.globalMethods.navigateToLoginWithoutCookies(world.DataGenerator.getOperatorUser(), world.DataGenerator.getOperatorUserEmail(), ApplicationType.EXTERNAL);
    }
}