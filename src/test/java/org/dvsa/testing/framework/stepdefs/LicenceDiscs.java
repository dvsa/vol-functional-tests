package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;;
import org.dvsa.testing.lib.pages.BasePage;
import org.junit.Assert;

public class LicenceDiscs extends BasePage implements En {

    public LicenceDiscs(World world) {
        And("^the licence discs should be present$", () -> {

//          Retrieving end print number and the number of discs going to be displayed on the licence.
            int endNumber = Integer.valueOf(world.updateLicence.getEndNumber());
            int numberOfDiscs = world.createLicence.getNoOfVehiclesRequired();

//          Getting the end number and counting backwards as this will be the most recent set of licence disc that get printed.
            for (int i = 1; i <= numberOfDiscs; i++){
                Assert.assertTrue(isTextPresent(String.valueOf(endNumber - i),5));
            }
        });
    }
}
