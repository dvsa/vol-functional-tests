package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.PermitUsagePageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.external.pages.PeriodSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitUsagePage;

import static org.junit.Assert.assertEquals;

public class TurkeyPermitsUsagePageSteps implements En {
    public TurkeyPermitsUsagePageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Turkey Permit usage page with correct information and content$", () -> {

            PermitUsagePage.untilOnPage();

            //Country name displayed on the Permits Usage page is the one clicked on the overview page
            assertEquals(PermitUsagePage.getCountry(), operatorStore.getCountry());

            // the page heading on bilateral Turkey permits usage  page is correct
            PermitUsagePageJourney.hasPageHeading();

            // I have only one option displayed by default i.e. Single Journey permits
            assertEquals("Only single journey permits are available for this country.", PermitUsagePage.getDefaultPeriodOption());
        });

        When("^I select continue button on the Bilateral Turkey permit usage page$", PeriodSelectionPage::saveAndContinue);
    }
}
