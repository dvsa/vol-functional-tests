package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.external.pages.PeriodSelectionPage;

import static org.junit.Assert.assertEquals;

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
