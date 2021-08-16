package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.TurkeyThirdCountryPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TurkeyThirdCountrySteps implements En {
    public TurkeyThirdCountrySteps(World world) {

        Then("^I am on the Turkey third country page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            TurkeyThirdCountryPage.untilOnPage();

            //checking the Country name displayed on the page is Turkey
            assertEquals(TurkeyThirdCountryPage.getCountry(), AnnualBilateralJourney.getCountry());

            //checking the Page heading on the Turkey third country page is correct
            assertEquals("Are you transporting goods from Turkey to a third-country?", TurkeyThirdCountryPage.getPageHeading());

            //checking the  YES and NO radio buttons  with display of respective help texts on seletion
            assertTrue(TurkeyThirdCountryPage.areCorrectRadioButtonsPresent());
        });

        When("^I save and continue on the Turkey third country page$", TurkeyThirdCountryPage::saveAndContinue);
        Then("^I should get the validation error message to select one option$", () -> {
            assertEquals("Please select one option", TurkeyThirdCountryPage.getErrorText());
        });
        When("^I select Yes radio button on the Turkey third country page$", TurkeyThirdCountryPage::clickYesToRadioButton);
        When("^I select No radio button on the Turkey third country page$", TurkeyThirdCountryPage::clickNoToRadioButton);
        When("^I get advisory text that I don't need Turkey permits$", () -> {
            assertTrue(TurkeyThirdCountryPage.noSelectionAdvisoryText());
        });
        When("^I am navigated to the cancel application page$", () -> {
            CancellationPage.untilOnPage();
            String heading = CancellationPage.getPageHeading();
            assertEquals("Cancel application", heading);
        });
    }
}

