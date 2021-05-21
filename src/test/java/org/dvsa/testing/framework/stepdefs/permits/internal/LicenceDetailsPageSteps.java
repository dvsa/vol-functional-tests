package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.internal.details.LicenceDetailsPage;
import org.junit.Assert;

public class LicenceDetailsPageSteps implements En {

    public LicenceDetailsPageSteps(World world) {
        Given("^I am viewing a good operating licence on internal$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.internalNavigation.urlSearchAndViewLicence();
            // May not be the code but theirs was commented out before so doesn't matter apparently.
        });
        Then("^I should see the IRHP permits tab$", () -> {
            Assert.assertTrue("The IRHP Permits tab is not displayed on the current page", LicenceDetailsPage.Tab.hasTab(LicenceDetailsPage.DetailsTab.IrhpPermits));
        });
        Then("^I should not see the IRHP permits tab$", () -> {
            Assert.assertTrue("The IRHP Permits tab is displayed on the current page", !LicenceDetailsPage.Tab.hasTab(LicenceDetailsPage.DetailsTab.IrhpPermits));
        });
    }

}
