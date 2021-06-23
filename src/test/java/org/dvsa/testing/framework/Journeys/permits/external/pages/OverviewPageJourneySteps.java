package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;

public class OverviewPageJourneySteps extends BasePermitJourney {

    public static void clickOverviewSection(OverviewSection section) {
        OverviewPage.untilOnPage();
        OverviewPage.clickOverviewSection(section);
    }
}
