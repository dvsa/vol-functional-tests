package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.EssentialInformationPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.EssentialInformationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EssentialInformationSteps implements En {
    public EssentialInformationSteps(World world) {

        Then("^I am navigated to (.+) essential information page with correct information and content$", (String country) -> {
            // Make sure the page has loaded before any further checks
            EssentialInformationPage.untilOnPage();

            //checking the Country name displayed on the page is Turkey
            assertEquals(BasePermitPage.getCountry(), AnnualBilateralJourney.getCountry());

            //checking the Page heading on the essential information page is correct
            EssentialInformationPageJourney.hasPageHeading();

            if (country.equals("Turkey")) {
                //checking the page content on Bilateral essential information  page is correct
                assertTrue(EssentialInformationPage.isTurkeyPageContentPresent());
            } else if (country.equals("Ukraine")) {
                //checking the page content on Bilateral essential information  page is correct
                assertTrue(EssentialInformationPage.isUkrainePageContentPresent());
            }

        });
        When("^I select continue button on the Bilateral (.+) essential information page$", (String) -> {
            EssentialInformationPage.saveAndContinue();
        });
    }
}

