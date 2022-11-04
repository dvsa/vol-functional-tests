package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.PeriodSelectionPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.PeriodSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PeriodSelectionPageSteps extends BasePermitPage implements En {
    public PeriodSelectionPageSteps(World world) {
        Then("^I am on the Bilateral (.+) Period Selection page with correct information and content$", (String country) -> {
            PeriodSelectionPage.untilOnPage();

            // Checking Page heading
            PeriodSelectionPageJourney.hasPageHeading();

           //Checking Country name displayed on the page is the one clicked on the overview page
            assertEquals(PeriodSelectionPage.getCountry(), AnnualBilateralJourney.getCountry());

            assertTrue(isElementPresent(String.format("//div[contains(text(),'%s')]", country), SelectorType.XPATH));
            if (country.equals("Turkey")) {
                AnnualBilateralJourney.setPeriodType(PeriodType.BilateralsTurkey);
            } else if (country.equals("Ukraine")) {
                AnnualBilateralJourney.setPeriodType(PeriodType.BilateralsUkraine);
            }
        });

        When("^I select continue button on the Bilateral period selection page$", PeriodSelectionPage::saveAndContinue);
    }
}

