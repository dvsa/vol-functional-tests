package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;

public class TurkeyApplicationOverviewPageSteps implements En {
    public TurkeyApplicationOverviewPageSteps() {

        When("^I click on read declaration on the application overview page$", () -> {
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.BilateralDeclaration);
        });
    }
}

