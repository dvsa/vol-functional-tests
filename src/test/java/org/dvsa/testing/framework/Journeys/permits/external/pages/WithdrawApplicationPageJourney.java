package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.external.pages.WithdrawApplicationPage;

import static org.junit.Assert.assertEquals;

public class WithdrawApplicationPageJourney extends BasePermitJourney {

    public static void hasPageHeading() {
        assertEquals("Withdraw Application", WithdrawApplicationPage.getPageHeading());
    }
}
