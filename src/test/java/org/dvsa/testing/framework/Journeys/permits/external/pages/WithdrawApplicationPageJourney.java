package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.WithdrawApplicationPage;

import static org.junit.Assert.assertEquals;

public class WithdrawApplicationPageJourney extends BasePermitJourney {

    public static void hasPageHeading() {
        assertEquals("Withdraw application", WithdrawApplicationPage.getPageHeading());
    }
}
