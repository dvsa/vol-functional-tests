package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.RemovalsEligibilityPage;

public class RemovalsEligibilityPageJourney extends BasePermitJourney {

    public RemovalsEligibilityPageJourney(World world){
        super(world);
    }
    public static void completePage() {
        RemovalsEligibilityPage.confirmCheckbox();
        saveAndContinue();
    }
}
