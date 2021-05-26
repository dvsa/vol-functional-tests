package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.BilateralPeriodType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.pages.PeriodSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class UkrainePeriodSelectionPageSteps extends BasePermitPage implements En {
    public UkrainePeriodSelectionPageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Ukraine Period Selection page with correct information and content$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.PeriodSelectionPage.untilOnPage();

            // Checking Page heading
            org.dvsa.testing.lib.newPages.permits.pages.PeriodSelectionPage.hasPageHeading();

           //Checking Country name displayed on the page is the one clicked on the overview page
            Assert.assertEquals(PeriodSelectionPage.getCountry(),operatorStore.getCountry());

            // Check that by default period should always be Ukraine related
            assertTrue(isElementPresent("//div[contains(text(),'Ukraine')]", SelectorType.XPATH));
            operatorStore.setCurrentBilateralPeriodType(BilateralPeriodType.BilateralsUkraine);
        });
        When("^I select continue button on the Bilateral Ukraine period selection page$", PeriodSelectionPage::saveAndContinue);
    }
}
// TODO Entire class is near identical to TurkerPeriodSelectionpageSteps.

