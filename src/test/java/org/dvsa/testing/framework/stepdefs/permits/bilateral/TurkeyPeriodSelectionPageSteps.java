package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PeriodSelectionPage;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.untilOnPeriodSelectionPage;

public class TurkeyPeriodSelectionPageSteps implements En {
    public TurkeyPeriodSelectionPageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Turkey Period Selection page with correct information and content$", () -> {
            untilOnPeriodSelectionPage();

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

