package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;

public class LicenceDiscs extends BasePage implements En {

    public LicenceDiscs(World world) {
        And("^the licence discs should be present$", () -> {
            int psvDiscNumber = world.DBUtils.getFirstPsvDiscNumber(world.createApplication.getLicenceId(), world.configuration);
            for (int i = 0; i < 5; i++){
                Assert.assertTrue(isTextPresent(String.valueOf(psvDiscNumber + i)));
            }
        });
    }
}
