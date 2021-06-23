package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.permits.pages.CheckIfYouNeedECMTPermitsPage;

public class CheckIfYouNeedECMTPermitsPageJourneySteps extends BasePermitJourney {

    public static void completePage() {
        CheckIfYouNeedECMTPermitsPage.checkNeedECMTPermits();
        CheckIfYouNeedECMTPermitsPage.saveAndContinue();
    }
}
