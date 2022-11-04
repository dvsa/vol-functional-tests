package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumberOfPermitsSteps implements En {
    public NumberOfPermitsSteps(World world) {
        Then("^I should get the validation error message on the number of permits page$", NumberOfPermitsPageJourney::hasBilateralErrorMessage);
        When("^I enter the number of bilateral permits required", NumberOfPermitsPageJourney::completePage);
        Then("^I am on the annual bilateral number of permits page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            NumberOfPermitsPage.untilOnPage();

            //checking the Country name displayed on the page is Ukraine
            assertEquals(BasePermitPage.getCountry(), AnnualBilateralJourney.getCountry());

            //checking the Page heading on the Turkey number of permits page is correct
            NumberOfPermitsPageJourney.hasPageHeading();

            //checking the Number of permits label
            assertTrue(NumberOfPermitsPage.isTurkeyAndUkraineBilateralStandardSingleInformationPresent());
        });
    }

}

