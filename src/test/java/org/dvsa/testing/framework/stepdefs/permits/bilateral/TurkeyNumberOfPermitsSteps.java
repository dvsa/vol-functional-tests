package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.TurkeyThirdCountryPage;
import org.junit.Assert;

public class TurkeyNumberOfPermitsSteps implements En {
    public TurkeyNumberOfPermitsSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the annual bilateral Turkey number of permit page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            NumberOfPermitsPage.untilOnPage();

            //checking the Country name displayed on the page is Turkey
            Assert.assertEquals(TurkeyThirdCountryPage.getCountry(),operatorStore.getCountry());

            //checking the Page heading on the Turkey number of permits page is correct
            NumberOfPermitsPageJourneySteps.hasPageHeading();

            //checking the Number of permits label
            NumberOfPermitsPage.hasTurkeyAndUkraineBilateralStandardSingleInformation();

        });
        Then("^I should get the validation error message on the number of permits page$", NumberOfPermitsPage::hasBilateralErrorMessage);
        When("^I enter the number of bilateral permits required", NumberOfPermitsPageJourneySteps::completePage);
    }
}

