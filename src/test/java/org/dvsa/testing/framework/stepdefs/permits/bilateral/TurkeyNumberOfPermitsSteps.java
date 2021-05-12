package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.TurkeyThirdCountryPage;
import org.junit.Assert;

public class TurkeyNumberOfPermitsSteps implements En {
    public TurkeyNumberOfPermitsSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the annual bilateral Turkey number of permit page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            NumberOfPermitsPage.untilOnNumberofPermitsPage();

            //checking the Country name displayed on the page is Turkey
            Assert.assertEquals(TurkeyThirdCountryPage.getCountry(),operatorStore.getCountry());

            //checking the Page heading on the Turkey number of permits page is correct
            NumberOfPermitsPage.pageHeading();

            //checking the Number of permits label
            NumberOfPermitsPage.turkeyStandardSingle();

        });

        When("^I save and continue on the Turkey number of permits page$", () -> {
            TurkeyThirdCountryPage.turkeyThirdCountrySaveAndContinue();
        });

        Then("^I should get the validation error message on the number of permits page$", () -> {
        NumberOfPermitsPage.turkeyNumberofPermitsValidation();
        });
        Then("^I enter the valid number of permits required for Turkey permit$", () -> {
            NumberOfPermitsPage.turkeyNumberOfPermits();
        });
    }
}

