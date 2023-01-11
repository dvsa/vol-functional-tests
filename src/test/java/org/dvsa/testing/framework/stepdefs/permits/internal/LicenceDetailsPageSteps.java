package org.dvsa.testing.framework.stepdefs.permits.internal;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;

import static org.junit.jupiter.api.Assertions.*;

public class LicenceDetailsPageSteps {

    @Then("I should see the IRHP permits tab")
    public void iShouldSeeTheIRHPPermitsTab() {
        assertTrue(BaseDetailsPage.Tab.hasTab(DetailsTab.IrhpPermits),"The IRHP Permits tab is not displayed on the current page");
    }

    @Then("I should not see the IRHP permits tab")
    public void iShouldNotSeeTheIRHPTab() {
        assertFalse(BaseDetailsPage.Tab.hasTab(DetailsTab.IrhpPermits),"The IRHP Permits tab is displayed on the current page");
    }
}
