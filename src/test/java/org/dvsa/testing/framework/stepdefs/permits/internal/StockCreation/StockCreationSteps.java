package org.dvsa.testing.framework.stepdefs.permits.internal.StockCreation;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.permits.pages.InternalBaseJourney;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.Permit;

public class StockCreationSteps {
    @Then("^I should be able to see Permits option")
    public void iShouldBeAbleToSeePermits() {
        InternalBaseJourney.navigateToAdminPermitsPage();
    }

    @Then("^I add a new ECMT APGG Euro 5 or Euro 6 stock")
    public void iAddANewECMTAPGG() {
        Permit.createStockNew();
    }
}