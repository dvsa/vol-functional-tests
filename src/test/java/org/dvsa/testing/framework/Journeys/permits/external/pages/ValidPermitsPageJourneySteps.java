package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.permits.pages.ValidPermitsPage;

import static org.junit.Assert.assertEquals;

public class ValidPermitsPageJourneySteps extends BasePermitJourney {

    public static void hasBilateralHeading() {
        String heading = ValidPermitsPage.getHeading();
        assertEquals("Bilateral permits", heading);
    }

    public static void hasMultilateralHeading() {
        String heading = ValidPermitsPage.getHeading();
        assertEquals("Annual Multilateral (EU and EEA)", heading);
    }

    public static void hasAnnualECMTHeading() {
        String heading = ValidPermitsPage.getHeading();
        assertEquals("ECMT International Removal", heading);
    }

}
