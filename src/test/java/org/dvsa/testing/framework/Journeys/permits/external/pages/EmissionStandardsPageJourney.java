package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.external.pages.EmissionStandardsPage;

import static org.junit.Assert.assertEquals;

public class EmissionStandardsPageJourney extends BasePermitJourney {

    public static void hasPageHeading() {
        String heading = EmissionStandardsPage.getPageHeading();
        assertEquals("You must only use the Euro emission standard vehicles that the permit allows", heading);
    }

    public static void completePage() {
        EmissionStandardsPage.confirmCheckbox();
        EmissionStandardsPage.saveAndContinue();
    }
}
