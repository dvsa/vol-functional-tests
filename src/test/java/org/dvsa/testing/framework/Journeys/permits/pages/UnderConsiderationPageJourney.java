package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UnderConsiderationPageJourney extends BasePermitJourney {

    public UnderConsiderationPageJourney(World world){
        super(world);
    }
    public static void hasPageHeading() {
        assertEquals("Application under consideration", BasePermitJourney.getPageHeading());
    }
}
