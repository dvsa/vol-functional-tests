package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;

public class CabotagePageJourney extends BasePermitJourney {


    public CabotagePageJourney(World world){
       super(world);
    }

    public static void completePage() {
        CabotagePage.confirmWontUndertakeCabotage();
        saveAndContinue();
    }
}
