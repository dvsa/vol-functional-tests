package org.dvsa.testing.framework.stepdefs.permits.internal.StockCreation;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.enums.Action;
import org.dvsa.testing.lib.pages.enums.AdminOption;
import org.dvsa.testing.lib.pages.internal.NavigationBar;
import org.dvsa.testing.lib.pages.internal.admin.permits.Permit;

public class StockCreationSteps implements En {
    public StockCreationSteps() {

        Then("^I should be able to see Permits option$", () -> {
            NavigationBar.adminPanel(Action.OPEN);
            NavigationBar.administratorList(AdminOption.PERMITS);
        });
        Then("^I add a new ECMT APGG Euro 5 or Euro 6 stock$", () -> {
            Permit.createStockNew();
        });
    }
}