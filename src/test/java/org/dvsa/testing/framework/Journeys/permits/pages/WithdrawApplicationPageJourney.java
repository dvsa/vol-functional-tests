package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.WithdrawApplicationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WithdrawApplicationPageJourney extends BasePermitJourney {

    public WithdrawApplicationPageJourney(World world){
        super(world);
    }
    public static void hasPageHeading() {
        assertEquals("Withdraw application", WithdrawApplicationPage.getPageHeading());
    }
}
