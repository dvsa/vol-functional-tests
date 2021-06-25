package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.external.pages.ValidPermitsPage;

import static org.junit.Assert.assertEquals;

public class ValidPermitsPageJourney extends BasePermitJourney {

    public static void hasBilateralHeading() {
        String heading = ValidPermitsPage.getPageHeading();
        assertEquals("Bilateral permits", heading);
    }

    public static void hasMultilateralHeading() {
        String heading = ValidPermitsPage.getPageHeading();
        assertEquals("Annual Multilateral (EU and EEA)", heading);
    }

    public static void hasAnnualECMTHeading() {
        String heading = ValidPermitsPage.getPageHeading();
        assertEquals("ECMT International Removal", heading);
    }

}
