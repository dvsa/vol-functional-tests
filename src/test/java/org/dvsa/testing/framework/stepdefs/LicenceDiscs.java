package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.junit.Assert;

public class LicenceDiscs extends BasePage implements En {

    public LicenceDiscs(World world) {
        And("^the licence discs should be present$", () -> {

//          Retrieving starting print number and the number of discs going to be displayed on the licence.
            int startNumber = Integer.valueOf(world.updateLicence.getStartNumber());
            int numberOfDiscs = world.createLicence.getNoOfVehiclesRequired();

//          Looping over the discs numbers that should display on the licence, incrementing the disc number each time
//          since they were created simultaneously and then asserting their presence.
            for (int i = 0; i < numberOfDiscs; i++){
                Assert.assertTrue(isTextPresent(String.valueOf(startNumber + i),5));
            }
        });
    }
}
