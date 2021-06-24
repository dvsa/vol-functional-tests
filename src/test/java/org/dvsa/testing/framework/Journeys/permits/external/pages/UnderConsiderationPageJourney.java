package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.permits.pages.UnderConsiderationPage;

import static org.junit.Assert.assertEquals;

public class UnderConsiderationPageJourney extends BasePermitJourney {

    public static void hasPageHeading() {
        assertEquals("Application details", UnderConsiderationPage.getPageHeading());
    }
}
