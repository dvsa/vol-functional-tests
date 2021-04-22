package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.external.permit.bilateral.OverviewPage;

public class TurkeyApplicationOverviewPageSteps implements En {
    public TurkeyApplicationOverviewPageSteps() {

        When("^I click on read declaration on the application overview page$", () -> {
            OverviewPage.selectDeclaration();
        });
    }
}

