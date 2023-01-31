package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class BasePermitPageJourney extends BasePermitJourney {

    public static void hasReferenceOnPage() {
        assertFalse(BasePermitPage.getReferenceFromPage().isEmpty());
    }
}