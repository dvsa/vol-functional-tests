package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;

public class CheckYourAnswersPageJourney extends BasePermitJourney {

    public static void completePage() {
        CheckYourAnswerPage.untilOnPage();
        saveAndContinue();
    }
}
