package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;

public class TurkeyApplicationOverviewPageSteps implements En {
    public TurkeyApplicationOverviewPageSteps() {

        When("^I click on read declaration on the application overview page$", () -> {
            OverviewPage.clickOverviewSection(OverviewSection.BilateralDeclaration);
        });
    }
}

