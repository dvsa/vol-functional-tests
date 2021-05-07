package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.CheckYourAnswersPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.NumberOfPermitsPage;
import org.junit.Assert;

public class UkraineCheckYourAnswersSteps implements En {
    public UkraineCheckYourAnswersSteps(OperatorStore operatorStore, World world) {
        And("^I am on the Annual Bilateral Ukraine check your answers page with correct information and content$", ()-> {

            //Wait until check your answers page is loaded
            CheckYourAnswersPage.untilOnCheckYourAnswersPage();

            /*Period should always be Ukraine related
            we have commented it out as sql script to create stock automatically doesn't populate this automatically and it may change in future*/
           // Assert.assertEquals(CheckYourAnswersPage.getPeriod(),operatorStore.getCurrentBilateralPeriodType().toString());

            //Permit usage should always be Single Journeys as that's the only Ukraine offers
            Assert.assertEquals(CheckYourAnswersPage.getJourney(),"Single journeys");

            //Euro emission standard should always be Euro 3 or Euro 4
            Assert.assertEquals(CheckYourAnswersPage.emissionStandard(),"Euro 3 or Euro 4");


            //Number of permits should be the same as entered on the number of permits page
            String permitlabel = operatorStore.getPermit();
            String permitvalue =String.valueOf(NumberOfPermitsPage.getPermitValue());
            Assert.assertEquals(CheckYourAnswersPage.getPermitValue(),permitvalue+" "+"Standard single journey permits");
        });


    }
}
