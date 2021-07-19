package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.PeriodSelectionPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.PeriodSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class PeriodSelectionPageSteps extends BasePermitPage implements En {
    public PeriodSelectionPageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral (.+) Period Selection page with correct information and content$", (String country) -> {
            PeriodSelectionPage.untilOnPage();

            // Checking Page heading
            PeriodSelectionPageJourney.hasPageHeading();

           //Checking Country name displayed on the page is the one clicked on the overview page
            Assert.assertEquals(PeriodSelectionPage.getCountry(), operatorStore.getCountry());

            assertTrue(isElementPresent(String.format("//div[contains(text(),'%s')]", country), SelectorType.XPATH));
            if (country.equals("Turkey")) {
                operatorStore.setCurrentBilateralPeriodType(PeriodType.BilateralsTurkey);
            } else if (country.equals("Ukraine")) {
                operatorStore.setCurrentBilateralPeriodType(PeriodType.BilateralsUkraine);
            }
        });

        When("^I select continue button on the Bilateral period selection page$", PeriodSelectionPage::saveAndContinue);
    }
}

