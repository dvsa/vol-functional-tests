package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PeriodSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PermitUsagePage;
import org.junit.Assert;

public class TurkeyPermitsUsagePageSteps implements En {
    public TurkeyPermitsUsagePageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Turkey Permit usage page with correct information and content$", () -> {

            PermitUsagePage.untilOnPermitUsagePage();

            //Country name displayed on the Permits Usage page is the one clicked on the overview page
            Assert.assertEquals(PermitUsagePage.getCountry(),operatorStore.getCountry());

            // the page heading on bilateral Turkey permits usage  page is correct
            String expectedPageHeading = "Permit usage";
            String actualPageHeading = PermitUsagePage.turkeyPageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);

            // I have only one option displayed by default i.e. Single Journey permits
            PermitUsagePage.TurkeyDefaultPeriodOption();
        });

        When("^I select continue button on the Bilateral Turkey permit usage page$", () -> {
            PeriodSelectionPage.continueButton();
        });
    }
}
