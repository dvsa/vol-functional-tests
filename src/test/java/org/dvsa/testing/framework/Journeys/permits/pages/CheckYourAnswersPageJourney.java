package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;

public class CheckYourAnswersPageJourney extends BasePermitJourney {

    public CheckYourAnswersPageJourney(World world){
        super(world);
    }
    public static void completePage() {
        CheckYourAnswerPage.untilOnPage();
        saveAndContinue();
    }
}
