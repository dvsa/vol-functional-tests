package org.dvsa.testing.framework.stepdefs.permits.internal.StockCreation;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.newPages.enums.Action;
import org.dvsa.testing.lib.newPages.enums.AdminOption;
import org.dvsa.testing.lib.newPages.internal.admin.permits.Permit;
import org.dvsa.testing.lib.pages.internal.NavigationBar;

public class StockCreationSteps implements En {
    public StockCreationSteps() {

        Then("^I should be able to see Permits option$", () -> {
            NavigationBar.adminPanel(Action.OPEN);
            NavigationBar.administratorList(AdminOption.PERMITS);
        });
        Then("^I add a new ECMT APGG Euro 5 or Euro 6 stock$", Permit::createStockNew);
    }
}