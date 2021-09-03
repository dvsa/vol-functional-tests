package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.junit.Assert;

public class BilateralStandardAndCabotagePermitsCheckYourAnswersPageSteps extends BasePermitPage implements En {
    public BilateralStandardAndCabotagePermitsCheckYourAnswersPageSteps(World world) {
        Then("^I am on the Bilateral Standard and Cabotage permits check your answers page$", () -> {
            AnnualBilateralJourney.startBilateralJourneyTypeAndSelectCabotageUntilCheckYourAnswersPage(world, PeriodType.BilateralsStandardAndCabotagePermits, Country.Norway, true);
        });

        Then("^Value of do you need to carry out cabotage, will always be as per the value selected on the cabotage page$", () -> {
            int count = NumberOfPermitsPageJourney.getFieldCount();
            String value = getText("//*[@id='main-content']//dl/div[3]/dd[1]", SelectorType.XPATH);
            if(count == 1) {
               Assert.assertEquals("Yes\nI only need permits for cabotage", value);
            }
            else {
                Assert.assertEquals("Yes\nI need standard and cabotage permits", value);
            }
        });

        Then("^For Bilateral Standard and cabotage permits ,the Value of How many permits you need, will be as per the ones saved on the number of permits page$", () -> {
            int count = NumberOfPermitsPageJourney.getFieldCount();
            String permitlabel = NumberOfPermitsPageJourney.getLabel();
            String permitstandardlabel = String.valueOf(NumberOfPermitsPage.getStandardLabel());
            String permitcabotagelabel = String.valueOf(NumberOfPermitsPage.getCabotageLabel());
            String permitCabotage = String.valueOf(NumberOfPermitsPageJourney.getCabotageValue());
            String permitStandard = String.valueOf(NumberOfPermitsPageJourney.getStandardValue());
            String permitvalue = String.valueOf(NumberOfPermitsPageJourney.getPermitValue());

            if(count == 1) {
                Assert.assertEquals(BilateralJourneySteps.getPermitValueMultiple(), permitvalue + " " + permitlabel + "s");
            }
            else {
                Assert.assertEquals(BilateralJourneySteps.getPermitValueMultiple(), permitStandard + " " + permitstandardlabel + "s" + '\n' + permitCabotage + " " + permitcabotagelabel + "s");
            }
        });
    }
}

