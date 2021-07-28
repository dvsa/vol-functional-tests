package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.external.pages.EmissionStandardsPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UkraineEmissionsStandardsSteps implements En {
    public UkraineEmissionsStandardsSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the Ukraine emissions standards page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            EmissionStandardsPage.untilOnPage();

            //checking the Country name displayed on the page is Ukraine
            assertEquals(EmissionStandardsPage.getCountry(), operatorStore.getCountry());

            //checking the Page heading on the Ukraine emissions standards page is correct
            String heading = EmissionStandardsPage.getPageHeading();
            assertEquals("Which vehicle euro emission standard will you use?", heading);

            //checking the  YES and NO radio buttons  with display of respective help texts on selection
            assertTrue(EmissionStandardsPage.isEuro3To6RadioButtonsChoices());
        });

        When("^I save and continue on the Ukraine emissions standards page$", () -> {
            EmissionStandardsPage.saveAndContinue();

        });

        When("^I select Euro 3 or Euro 4 radio button on the Ukraine emissions standards page$", () -> {
            EmissionStandardsPage.clickYes();
        });

        When("^I select Euro 5, Euro 6 or higher emission standard radio button on the Ukraine emissions standards page$", () -> {
            EmissionStandardsPage.clickNo();

        });

        When("^I get advisory text that I don't need Ukraine permits$", () -> {
            EmissionStandardsPage.ukraineSelectNoAdvisoryText();
        });
    }
}

