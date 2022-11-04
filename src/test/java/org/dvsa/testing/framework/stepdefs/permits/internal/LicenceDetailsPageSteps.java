package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LicenceDetailsPageSteps implements En {

    public LicenceDetailsPageSteps(World world) {
        Then("^I should see the IRHP permits tab$", () -> {
            assertTrue(BaseDetailsPage.Tab.hasTab(DetailsTab.IrhpPermits),"The IRHP Permits tab is not displayed on the current page");
        });
        Then("^I should not see the IRHP permits tab$", () -> {
            assertFalse(BaseDetailsPage.Tab.hasTab(DetailsTab.IrhpPermits),"The IRHP Permits tab is displayed on the current page");
        });
    }

}
