package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.Assert.assertFalse;

public class BasePermitPageJourney extends BasePermitJourney {

    public static void hasReferenceOnPage() {
        assertFalse(BasePermitPage.getReferenceFromPage().isEmpty());
    }

}
