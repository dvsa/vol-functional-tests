package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage;

import static org.junit.Assert.assertEquals;

public class PermitUsagePageJourney extends BasePermitJourney {

    public static void hasPageHeading() {
        String heading = PermitUsagePage.getPageHeading();
        if ((isElementPresent("//input[@id='qaElement']", SelectorType.XPATH))) {
            assertEquals("What do you need to use your permits for?", heading);
        } else {
            assertEquals("Permit usage", heading);
        }
    }
}
