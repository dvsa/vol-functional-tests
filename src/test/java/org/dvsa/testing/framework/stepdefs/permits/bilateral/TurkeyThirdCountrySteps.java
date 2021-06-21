package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.CancelApplicationPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.TurkeyThirdCountryPage;
import org.junit.Assert;

public class TurkeyThirdCountrySteps implements En {
    public TurkeyThirdCountrySteps(OperatorStore operatorStore, World world) {

        Then("^I am on the Turkey third country page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            TurkeyThirdCountryPage.untilOnThirdCountryPage();

            //checking the Country name displayed on the page is Turkey
            Assert.assertEquals(TurkeyThirdCountryPage.getCountry(),operatorStore.getCountry());

            //checking the Page heading on the Turkey third country page is correct
            String expectedPageHeading = "Are you transporting goods from Turkey to a third-country?";
            String actualPageHeading = TurkeyThirdCountryPage.pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);

            //checking the  YES and NO radio buttons  with display of respective help texts on seletion
            TurkeyThirdCountryPage.radiobuttons();
        });

        When("^I save and continue on the Turkey third country page$", () -> {
            TurkeyThirdCountryPage.turkeyThirdCountrySaveAndContinue();
        });
        Then("^I should get the validation error message to select one option$", () -> {
           TurkeyThirdCountryPage.turkeyThirdCountryErrorText();
        });
        When("^I select Yes radio button on the Turkey third country page$", () -> {
           TurkeyThirdCountryPage.turkeyThirdCountryYes();
        });
        When("^I select No radio button on the Turkey third country page$", () -> {
            TurkeyThirdCountryPage.turkeyThirdCountryNo();
        });
        When("^I get advisory text that I don't need Turkey permits$", () -> {
            TurkeyThirdCountryPage.noAdvisoryText();
        });
        When("^I am navigated to the cancel application page$", () -> {
            CancelApplicationPage.untilOnPage();
        });
    }
}

