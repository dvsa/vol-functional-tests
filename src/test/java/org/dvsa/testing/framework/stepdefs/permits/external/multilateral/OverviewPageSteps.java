
package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;

public class OverviewPageSteps implements En {
    public OverviewPageSteps() {
        And("^I should be on the Annual Multilateral overview page$", () -> { OverviewPage.untilOnPage(); });

    }
}
