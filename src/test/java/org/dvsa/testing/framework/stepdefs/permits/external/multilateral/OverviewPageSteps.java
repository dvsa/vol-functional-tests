
package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import io.cucumber.java8.En;
import org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage;

public class OverviewPageSteps implements En {
    public OverviewPageSteps() {
        And("^I should be on the Annual Multilateral overview page$", () -> { OverviewPage.untilOnPage(); });

    }
}
