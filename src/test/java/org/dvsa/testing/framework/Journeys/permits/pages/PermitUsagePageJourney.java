package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermitUsagePageJourney extends BasePermitJourney {

    public static void hasPageHeading() {
        String heading = PermitUsagePage.getPageHeading();
        if ((isElementPresent("//input[@id='qaElement']", SelectorType.XPATH))) {
            assertEquals("What do you need to use your permits for?", heading);
        } else {
            assertEquals("Permit usage", heading);
        }
    }

    public static void completePermitTypePage(JourneyType permitTypeChoice) {
        PermitUsagePage.untilOnPage();
        PermitUsagePage.journeyType(permitTypeChoice);
        PermitUsagePage.saveAndContinue();
    }
}