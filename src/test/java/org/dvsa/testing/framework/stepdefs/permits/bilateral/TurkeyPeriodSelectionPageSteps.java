package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.pages.PeriodSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class TurkeyPeriodSelectionPageSteps extends BasePermitPage implements En {
    public TurkeyPeriodSelectionPageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Turkey Period Selection page with correct information and content$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.PeriodSelectionPage.untilOnPage();

            // Checking Page heading
            org.dvsa.testing.lib.newPages.permits.pages.PeriodSelectionPage.hasPageHeading();

           //Checking Country name displayed on the page is the one clicked on the overview page
            Assert.assertEquals(PeriodSelectionPage.getCountry(), operatorStore.getCountry());

            // Check that by default period should always be Turkey related
            assertTrue(isElementPresent("//div[contains(text(),'Turkey')]", SelectorType.XPATH));
            operatorStore.setCurrentBilateralPeriodType(PeriodType.BilateralsTurkey);
        });
        When("^I select continue button on the Bilateral Turkey period selection page$", PeriodSelectionPage::saveAndContinue);
    }
}

