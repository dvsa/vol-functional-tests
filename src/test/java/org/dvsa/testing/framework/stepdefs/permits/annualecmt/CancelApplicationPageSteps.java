package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.pages.external.permit.CancelApplicationPage;
import org.dvsa.testing.lib.pages.external.permit.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.CabotagePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PeriodSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.*;

public class CancelApplicationPageSteps implements En {

    public CancelApplicationPageSteps(World world, OperatorStore operatorStore) {
        Given("^I am on the cancel application page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPage.Application.cancel();
        });
        And("^I have not confirmed I would like to cancel$", () -> {
            // Here for readability and to stop cucumber from throwing an exception
        });
        Then("^I should see the validation error message for the cancel application page$", () -> {
            Assert.assertTrue(CancelApplicationPage.hasErrorMessagePresent());
        });
        When("^I cancel my ECMT application$", CancelApplicationPage::cancel);
        Then("^I navigate to the Bilaterals cabotage page$", () -> {
            org.dvsa.testing.lib.pages.external.permit.bilateral.OverviewPage.untilOnOverviewPage();
            org.dvsa.testing.lib.pages.external.permit.bilateral.OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            untilOnPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        Then("^I click save and continue on cabotage page$", () -> {
           CabotagePage.saveAndContinue();
        });
        Then("^I am navigated to cabotage page$", () -> {
            CabotagePage.untilOnCabotage();
        });
    }

}
