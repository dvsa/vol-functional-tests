package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PeriodSelectionPage;
import org.junit.Assert;

public class TurkeyPeriodSelectionPageSteps implements En {
    public TurkeyPeriodSelectionPageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Turkey Period Selection page with correct information and content$", () -> {
            PeriodSelectionPage.untilOnPage();

            // Checking Page heading
            String expectedPageHeading = "Validity period";
            String actualPageHeading = PeriodSelectionPage.pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);

           //Checking Country name displayed on the page is the one clicked on the overview page
            Assert.assertEquals(PeriodSelectionPage.getCountry(),operatorStore.getCountry());

            // Check that by default period should always be Turkey related
            PeriodSelectionPage.defaultPeriodTurkey();
            operatorStore.setCurrentBilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsTurkey);
        });
        When("^I select continue button on the Bilateral Turkey period selection page$", PeriodSelectionPage::continueButton);
    }
}

