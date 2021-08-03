package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.TurkeyThirdCountryPage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.junit.Assert;

public class CheckYourAnswersSteps extends BasePage implements En {
    public CheckYourAnswersSteps(World world) {
        And("^I am on the Annual Bilateral (.+) check your answers page with correct information and content$", (String country)-> {

            //Wait until check your answers page is loaded
            CheckYourAnswerPage.untilOnPage();

            /*Period should always be Turkey related -
            commented it out as sql script to create stock automatically doesn't populate this automatically and it may change in future */
          // Assert.assertEquals(CheckYourAnswersPage.getPeriod(),operatorStore.getCurrentBilateralPeriodType().toString());

            //Permit usage should always be Single Journeys as that's the only Turkey offers
            Assert.assertEquals(BilateralJourneySteps.getJourney(),"Single journeys");

            if (country.equals("Turkey")) {
                //Third-country continuation should always be YES
                Assert.assertEquals(TurkeyThirdCountryPage.getOverviewThirdCountryContinuationText(), "Yes");
            } else if (country.equals("Ukraine")) {
                //Euro emission standard should always be Euro 3 or Euro 4
                String emissionStandard = getText("//dd[contains(text(),'Euro 3 or Euro 4')]", SelectorType.XPATH);
                Assert.assertEquals(emissionStandard,"Euro 3 or Euro 4");
            }

            //Number of permits should be the same as entered on the number of permits page
            String permitValue = String.valueOf(NumberOfPermitsPageJourney.getPermitValue());
            Assert.assertEquals(BilateralJourneySteps.getPermitValue(),permitValue + " Standard single journey permits");
        });
    }
}
