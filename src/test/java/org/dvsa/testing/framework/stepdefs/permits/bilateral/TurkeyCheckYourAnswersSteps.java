package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.TurkeyThirdCountryPage;
import org.junit.Assert;

public class TurkeyCheckYourAnswersSteps implements En {
    public TurkeyCheckYourAnswersSteps(OperatorStore operatorStore, World world) {
        And("^I am on the Annual Bilateral Turkey check your answers page with correct information and content$", ()-> {

            //Wait until check your answers page is loaded
            CheckYourAnswerPage.untilOnPage();

            /*Period should always be Turkey related -
            commented it out as sql script to create stock automatically doesn't populate this automatically and it may change in future */
          //  Assert.assertEquals(CheckYourAnswersPage.getPeriod(),operatorStore.getCurrentBilateralPeriodType().toString());

            //Permit usage should always be Single Journeys as that's the only Turkey offers
            Assert.assertEquals(BilateralJourneySteps.getJourney(),"Single journeys");

            //Third-country continuation should always be YES
            Assert.assertEquals(TurkeyThirdCountryPage.getOverviewThirdCountryContinuationText(), "Yes");

            //Number of permits should be the same as entered on the number of permits page
            String permitValue = String.valueOf(NumberOfPermitsPage.getPermitValue());
            Assert.assertEquals(BilateralJourneySteps.getPermitValue(),permitValue+" "+"Standard single journey permits");
        });


    }
}
