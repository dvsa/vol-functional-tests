package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.external.pages.CheckIfYouNeedECMTPermitsPage;

public class CheckIfYouNeedECMTPermitsPageJourney extends BasePermitJourney {

    public static void completePage() {
        CheckIfYouNeedECMTPermitsPage.checkNeedECMTPermits();
        CheckIfYouNeedECMTPermitsPage.saveAndContinue();
    }
}
