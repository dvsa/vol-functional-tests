package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.permits.pages.EmissionStandardsPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.junit.Assert;

public class UkraineNumberOfPermitsSteps implements En {
    public UkraineNumberOfPermitsSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the annual bilateral Ukraine number of permit page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            NumberOfPermitsPage.untilOnPage();

            //checking the Country name displayed on the page is Ukraine
            Assert.assertEquals(EmissionStandardsPage.getCountry(),operatorStore.getCountry());

            //checking the Page heading on the Turkey number of permits page is correct
            NumberOfPermitsPage.hasPageHeading();

            //checking the Number of permits label
            NumberOfPermitsPage.hasTurkeyAndUkraineBilateralStandardSingleInformation();
        });

        When("^I save and continue on the Ukraine number of permits page$", NumberOfPermitsPage::saveAndContinue);

        Then("^I enter the valid number of permits required for Ukraine permit$", org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage::setNumberOfPermitsAndSetRespectiveValues);
    }
}

