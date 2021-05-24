package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.junit.Assert;

public class UkraineCheckYourAnswersSteps extends BasePage implements En {
    public UkraineCheckYourAnswersSteps(OperatorStore operatorStore, World world) {
        And("^I am on the Annual Bilateral Ukraine check your answers page with correct information and content$", ()-> {

            //Wait until check your answers page is loaded
            CheckYourAnswerPage.untilOnPage();

            /*Period should always be Ukraine related
            we have commented it out as sql script to create stock automatically doesn't populate this automatically and it may change in future*/
           // Assert.assertEquals(CheckYourAnswersPage.getPeriod(),operatorStore.getCurrentBilateralPeriodType().toString());

            //Permit usage should always be Single Journeys as that's the only Ukraine offers
            Assert.assertEquals(BilateralJourneySteps.getJourney(),"Single journeys");

            //Euro emission standard should always be Euro 3 or Euro 4
            String emissionStandard = getText("//dd[contains(text(),'Euro 3 or Euro 4')]", SelectorType.XPATH);
            Assert.assertEquals(emissionStandard,"Euro 3 or Euro 4");

            //Number of permits should be the same as entered on the number of permits page
            String permitValue = String.valueOf(NumberOfPermitsPage.getPermitValue());
            Assert.assertEquals(BilateralJourneySteps.getPermitValue(),permitValue + " Standard single journey permits");
        });


    }
}
