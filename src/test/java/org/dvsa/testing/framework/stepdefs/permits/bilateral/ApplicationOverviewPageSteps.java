package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;

public class ApplicationOverviewPageSteps implements En {
    public ApplicationOverviewPageSteps() {

        When("^I click on read declaration on the application overview page$", () -> {
            OverviewPageJourney.clickBilateralOverviewSection(OverviewSection.BilateralDeclaration);
        });
    }
}

