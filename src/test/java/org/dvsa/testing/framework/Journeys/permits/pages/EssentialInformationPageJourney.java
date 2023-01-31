package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.EssentialInformationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EssentialInformationPageJourney extends BasePermitJourney {

    public static void hasPageHeading() {
        String heading = EssentialInformationPage.getPageHeading();
        assertEquals("Essential information", heading);
    }

    public static void completePage() {
        EssentialInformationPage.untilOnPage();
        hasPageHeading();
        saveAndContinue();
    }
}
