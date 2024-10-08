package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.EssentialInformationPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.EssentialInformationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EssentialInformationSteps {
    public EssentialInformationSteps(World world) {
    }

    @Then("I am navigated to {string} essential information page with correct information and content")
    public void iAmNavigatedToEssentialInformationPage(String country) {
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
    }

    @When("I select continue button on the Bilateral {string} essential information page")
    public void iSelectContinueButtonOnTheBil(String x) {
        EssentialInformationPage.saveAndContinue();
    }
}

