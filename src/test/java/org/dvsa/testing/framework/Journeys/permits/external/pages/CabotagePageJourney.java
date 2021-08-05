package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;

public class CabotagePageJourney extends BasePermitJourney {

    public static void completePage() {
        CabotagePage.confirmWontUndertakeCabotage();
        saveAndContinue();
    }
}
