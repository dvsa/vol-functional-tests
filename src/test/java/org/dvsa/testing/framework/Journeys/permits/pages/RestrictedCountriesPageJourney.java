package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.pageObjects.external.pages.RestrictedCountriesPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestrictedCountriesPageJourney {

    public static void hasPageHeading() {
        String heading = RestrictedCountriesPage.getPageHeading();
        assertEquals("Will you be transporting goods to Austria, Greece, Hungary, Italy or Russia?", heading);
    }
}
