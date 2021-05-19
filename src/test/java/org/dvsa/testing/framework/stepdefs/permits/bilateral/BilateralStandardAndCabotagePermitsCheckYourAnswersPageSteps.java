package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.untilOnPage;

public class BilateralStandardAndCabotagePermitsCheckYourAnswersPageSteps extends BasePermitPage implements En {
    public BilateralStandardAndCabotagePermitsCheckYourAnswersPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        Then("^I am on the Bilateral Standard and Cabotage permits check your answers page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickYesToCabotage();
            BilateralJourneySteps.yesAndCabotagePermitConfirmation();
            BilateralJourneySteps.saveAndContinue();
            NumberOfPermitsPage.numberOfPermitsNew();
            NumberOfPermitsPage.setCabotageValue(NumberOfPermitsPage.getCabotageValue());
            NumberOfPermitsPage.setStandardValue(NumberOfPermitsPage.getStandardValue());
            NumberOfPermitsPage.setFieldCount(NumberOfPermitsPage.getFieldCount());
            NumberOfPermitsPage.setLabel(NumberOfPermitsPage.permitLabel());
            AnnualBilateralJourney.getInstance().permit(operatorStore);
            AnnualBilateralJourney.saveAndContinue();
        });

        Then("^Value of do you need to carry out cabotage, will always be as per the value selected on the cabotage page$", () -> {
            int count = NumberOfPermitsPage.getFieldCount();
            String value = getText("//*[@id='main-content']//dl/div[3]/dd[1]", SelectorType.XPATH);
            if(count == 1) {
               Assert.assertEquals("Yes\nI only need permits for cabotage", value);
            }
            else {
                Assert.assertEquals("Yes\nI need standard and cabotage permits", value);
            }
        });

        Then("^For Bilateral Standard and cabotage permits ,the Value of How many permits you need, will be as per the ones saved on the number of permits page$", () -> {
            int count = NumberOfPermitsPage.getFieldCount();
            String permitlabel = operatorStore.getPermit();
            String permitstandardlabel = String.valueOf(NumberOfPermitsPage.getStandardLabel());
            String permitcabotagelabel = String.valueOf(NumberOfPermitsPage.getCabotageLabel());
            String permitCabotage = String.valueOf(NumberOfPermitsPage.getCabotageValue());
            String permitStandard = String.valueOf(NumberOfPermitsPage.getStandardValue());
            String permitvalue = String.valueOf(NumberOfPermitsPage.getPermitValue());

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

