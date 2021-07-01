package org.dvsa.testing.framework.stepdefs.permits.internal.StockCreation;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.InternalBaseJourney;
import org.dvsa.testing.lib.newPages.internal.admin.permits.Permit;

public class StockCreationSteps implements En {
    public StockCreationSteps() {

        Then("^I should be able to see Permits option$", InternalBaseJourney::navigateToAdminPermitsPage);
        Then("^I add a new ECMT APGG Euro 5 or Euro 6 stock$", Permit::createStockNew);
    }
}