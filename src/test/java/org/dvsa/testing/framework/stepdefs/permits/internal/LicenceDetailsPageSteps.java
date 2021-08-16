package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.lib.newPages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.newPages.internal.details.enums.DetailsTab;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import org.junit.Assert;

public class LicenceDetailsPageSteps implements En {

    public LicenceDetailsPageSteps(World world) {
        Then("^I should see the IRHP permits tab$", () -> {
            Assert.assertTrue("The IRHP Permits tab is not displayed on the current page", BaseDetailsPage.Tab.hasTab(DetailsTab.IrhpPermits));
        });
        Then("^I should not see the IRHP permits tab$", () -> {
            Assert.assertFalse("The IRHP Permits tab is displayed on the current page", BaseDetailsPage.Tab.hasTab(DetailsTab.IrhpPermits));
        });
    }

}
