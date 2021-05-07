package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.untilOnPage;

public class BilateralStandardPermitNoCabotageCheckYourAnswersPageSteps implements En {
    public BilateralStandardPermitNoCabotageCheckYourAnswersPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        Then("^I am on the Bilateral Standard permits no Cabotage check your answers page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.continueButton();
            NumberOfPermitsPage.numberOfPermitsNew();

            NumberOfPermitsPage.setStandardValue(NumberOfPermitsPage.getStandardValue());
            NumberOfPermitsPage.setFieldCount(NumberOfPermitsPage.getFieldCount());
            NumberOfPermitsPage.setLabel(NumberOfPermitsPage.permitLabel());
            AnnualBilateralJourney.getInstance().permit(operatorStore);
            BasePermitPage.bilateralSaveAndContinue();
        });

        Then("^I see three sections displayed on the table correctly$", CheckYourAnswersPage::newSectionNoCabotage);


        Then("^For bilateral standard permits no cabotage permit type,the value of how many permits you need, will be as per the ones saved on the number of permits page$", () -> {
            int count = NumberOfPermitsPage.getFieldCount();
            String permitlabel = operatorStore.getPermit();
            String permitvalue = String.valueOf(NumberOfPermitsPage.getPermitValue());
            Assert.assertEquals(CheckYourAnswersPage.getpermitValueforNonCabotage(), permitvalue + " " + permitlabel + "s");
        });
    }
}

