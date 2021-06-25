package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.EssentialInformationPage;
import org.dvsa.testing.lib.newPages.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitUsagePage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class BilateralStandardAndCabotagePermitsCheckYourAnswersPageSteps extends BasePermitPage implements En {
    public BilateralStandardAndCabotagePermitsCheckYourAnswersPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        Then("^I am on the Bilateral Standard and Cabotage permits check your answers page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickYesToCabotage();
            BilateralJourneySteps.yesAndCabotagePermitConfirmation();
            BilateralJourneySteps.saveAndContinue();
            NumberOfPermitsPageJourney.completePage();
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
            else
                {
                    Assert.assertEquals(BilateralJourneySteps.getPermitValueMultiple(), permitStandard + " " + permitstandardlabel + "s"+'\n'+permitCabotage + " " + permitcabotagelabel + "s");

                }
        });
    }
}

