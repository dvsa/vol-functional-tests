package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.lib.newPages.external.pages.RestrictedCountriesPage;

import static org.junit.Assert.assertEquals;

public class RestrictedCountriesPageJourney {

    public static void hasPageHeading() {
        String heading = RestrictedCountriesPage.getPageHeading();
        assertEquals("Will you be transporting goods to Austria, Greece, Hungary, Italy or Russia?", heading);
    }
}
