package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckIfYouNeedECMTPermitsPage;

public class CheckIfYouNeedECMTPermitsPageJourney extends BasePermitJourney {

    public static void completePage() {
        CheckIfYouNeedECMTPermitsPage.checkNeedECMTPermits();
        CheckIfYouNeedECMTPermitsPage.saveAndContinue();
    }
}
