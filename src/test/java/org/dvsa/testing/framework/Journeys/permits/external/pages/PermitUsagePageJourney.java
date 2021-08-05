package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;

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

    public static void hasECMTPageHeading() {
        String heading = PermitUsagePage.getPageHeading();
        assertEquals("How will you use your permits?", heading);
    }

    public static void completePermitTypePage(JourneyType permitTypeChoice) {
        PermitUsagePage.untilOnPage();
        PermitUsagePage.journeyType(permitTypeChoice);
        PermitUsagePage.saveAndContinue();
    }

}
