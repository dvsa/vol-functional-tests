package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.lib.newPages.external.pages.RestrictedCountriesPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;

import static org.junit.Assert.assertEquals;

public class RestrictedCountriesPageJourney extends BasePermitPage {

    public static void hasPageHeading() {
        String heading = RestrictedCountriesPage.getECMTPageHeading();
        assertEquals("Will you be transporting goods to Austria, Greece, Hungary, Italy or Russia?", heading);
    }
}
