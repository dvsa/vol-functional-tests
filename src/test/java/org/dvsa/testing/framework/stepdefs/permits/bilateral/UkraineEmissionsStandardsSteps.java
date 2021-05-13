package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.UkraineEmissionsStandardsPage;
import org.junit.Assert;

public class UkraineEmissionsStandardsSteps implements En {
    public UkraineEmissionsStandardsSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the Ukraine emissions standards page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            UkraineEmissionsStandardsPage.untilOnEmissionsPage();

            //checking the Country name displayed on the page is Ukraine
            Assert.assertEquals(UkraineEmissionsStandardsPage.getCountry(),operatorStore.getCountry());

            //checking the Page heading on the Ukraine emissions standards page is correct
            String expectedPageHeading = "Which vehicle euro emission standard will you use?";
            String actualPageHeading = UkraineEmissionsStandardsPage.pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);

            //checking the  YES and NO radio buttons  with display of respective help texts on selection
            UkraineEmissionsStandardsPage.radiobuttons();
        });

        When("^I save and continue on the Ukraine emissions standards page$", () -> {
            UkraineEmissionsStandardsPage.ukraineEmissionsSaveAndContinue();

        });

        When("^I select Euro 3 or Euro 4 radio button on the Ukraine emissions standards page$", () -> {
           UkraineEmissionsStandardsPage.emissionsYes();

        });
        When("^I select Euro 5, Euro 6 or higher emission standard radio button on the Ukraine emissions standards page$", () -> {
            UkraineEmissionsStandardsPage.emissionsNo();

        });
        When("^I select 'Euro5, Euro 6 or higher emission standard' radio button on the Ukraine emissions standards page$", () -> {
            UkraineEmissionsStandardsPage.emissionsNo();
        });
        When("^I get advisory text that I don't need Ukraine permits$", () -> {
            UkraineEmissionsStandardsPage.ukraineNoAdvisoryText();
        });
    }
}

