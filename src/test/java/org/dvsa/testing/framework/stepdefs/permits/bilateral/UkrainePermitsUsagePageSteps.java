package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.permits.pages.PeriodSelectionPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage;
import org.junit.Assert;

public class UkrainePermitsUsagePageSteps implements En {
    public UkrainePermitsUsagePageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Ukraine Permit usage page with correct information and content$", () -> {

            org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage.untilOnPage();

            //Country name displayed on the Permits Usage page is the one clicked on the overview page
            Assert.assertEquals(PermitUsagePage.getCountry(), operatorStore.getCountry());

            // the page heading on bilateral Turkey permits usage  page is correct
            org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage.hasPageHeading();

            // I have only one option displayed by default i.e. Single Journey permits
            org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage.hasDefaultPeriodOption();
        });

        When("^I select continue button on the Bilateral Ukraine permit usage page$", PeriodSelectionPage::saveAndContinue);
    }
}
