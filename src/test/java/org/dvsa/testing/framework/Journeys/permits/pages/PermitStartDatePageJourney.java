package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.PermitStartDatePage;

public class PermitStartDatePageJourney extends BasePermitJourney {

    public PermitStartDatePageJourney(World world){
        super(world);
    }
    public static void completePage() {
        PermitStartDatePage.permitDate();
        saveAndContinue();
    }

}
