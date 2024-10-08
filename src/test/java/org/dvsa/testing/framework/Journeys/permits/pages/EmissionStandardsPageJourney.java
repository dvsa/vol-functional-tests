package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.EmissionStandardsPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmissionStandardsPageJourney extends BasePermitJourney {

    public EmissionStandardsPageJourney(World world){
        super(world);
    }
    public static void hasPageHeading() {
        String heading = EmissionStandardsPage.getPageHeading();
        assertEquals("You must only use the Euro emission standard vehicles that the permit allows", heading);
    }

    public static void completePage() {
        EmissionStandardsPage.confirmCheckbox();
        EmissionStandardsPage.saveAndContinue();
    }
}