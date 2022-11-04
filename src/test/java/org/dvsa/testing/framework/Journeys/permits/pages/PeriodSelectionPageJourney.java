package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.PeriodSelectionPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeriodSelectionPageJourney extends BasePermitJourney {

    public static void hasPageHeading() {
        String heading = PeriodSelectionPage.getPageHeading();
        assertEquals(PeriodSelectionPage.PAGE_TITLE, heading);
    }

    public static void hasMoroccoPageHeading() {
        String heading = PeriodSelectionPage.getPageHeading();
        assertEquals(PeriodSelectionPage.MOROCCO_PAGE_TITLE, heading);
    }
}
