package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.TurkeyThirdCountryPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.UkraineEmissionsStandardsPage;
import org.junit.Assert;

public class UkraineNumberOfPermitsSteps implements En {
    public UkraineNumberOfPermitsSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the annual bilateral Ukraine number of permit page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            NumberOfPermitsPage.untilOnNumberofPermitsPage();

            //checking the Country name displayed on the page is Ukraine
            Assert.assertEquals(UkraineEmissionsStandardsPage.getCountry(),operatorStore.getCountry());

            //checking the Page heading on the Turkey number of permits page is correct
            NumberOfPermitsPage.pageHeading();

            //checking the Number of permits label
            NumberOfPermitsPage.turkeyStandardSingle();

        });

        When("^I save and continue on the Ukraine number of permits page$", () -> {
            TurkeyThirdCountryPage.turkeyThirdCountrySaveAndContinue();
        });

        Then("^I enter the valid number of permits required for Ukraine permit$", () -> {
            NumberOfPermitsPage.ukraineNumberOfPermits();
        });
    }
}

