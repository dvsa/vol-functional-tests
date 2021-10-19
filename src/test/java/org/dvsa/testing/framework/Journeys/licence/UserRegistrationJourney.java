package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class UserRegistrationJourney extends BasePage {
    private World world;

    public UserRegistrationJourney(World world){
        this.world = world;
    }

    public void registerUserWithNoLicence() {
        world.selfServeNavigation.navigateToLoginPage();
        world.selfServeNavigation.navigateToCreateAnAccount();
        world.UIJourney.addNewOperator(null, false);
        isTextPresent(world.UIJourney.getEmail());
    }
}