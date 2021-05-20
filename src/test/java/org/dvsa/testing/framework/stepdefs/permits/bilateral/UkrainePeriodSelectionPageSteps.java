package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PeriodSelectionPage;
import org.junit.Assert;

public class UkrainePeriodSelectionPageSteps implements En {
    public UkrainePeriodSelectionPageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Ukraine Period Selection page with correct information and content$", () -> {
            PeriodSelectionPage.untilOnPage();

            // Checking Page heading
            String expectedPageHeading = "Validity period";
            String actualPageHeading = PeriodSelectionPage.pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);

           //Checking Country name displayed on the page is the one clicked on the overview page
            Assert.assertEquals(PeriodSelectionPage.getCountry(),operatorStore.getCountry());

            // Check that by default period should always be Ukraine related
            PeriodSelectionPage.defaultPeriodUkraine();
            operatorStore.setCurrentBilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsUkraine);
        });
        When("^I select continue button on the Bilateral Ukraine period selection page$", PeriodSelectionPage::continueButton);
    }
}

