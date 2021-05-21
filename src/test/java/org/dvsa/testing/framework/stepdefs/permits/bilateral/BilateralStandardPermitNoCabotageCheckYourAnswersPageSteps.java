package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.EssentialInformationPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class BilateralStandardPermitNoCabotageCheckYourAnswersPageSteps implements En {
    public BilateralStandardPermitNoCabotageCheckYourAnswersPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        Then("^I am on the Bilateral Standard permits no Cabotage check your answers page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
            AnnualBilateralJourney.saveAndContinue();
        });

        Then("^I see three sections displayed on the table correctly$", () -> {
            BilateralJourneySteps.assertSectionsExist(false);
        });


        Then("^For bilateral standard permits no cabotage permit type,the value of how many permits you need, will be as per the ones saved on the number of permits page$", () -> {
            int count = NumberOfPermitsPage.getFieldCount();
            String permitlabel = org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.getLabel();
            String permitvalue = String.valueOf(NumberOfPermitsPage.getPermitValue());
            Assert.assertEquals(BilateralJourneySteps.getPermitValueForNonCabotage(), permitvalue + " " + permitlabel + "s");
        });
    }
}

