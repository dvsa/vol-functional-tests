package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.lib.newPages.BasePage;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
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
