package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.CancellationPage;
import org.dvsa.testing.lib.newPages.permits.pages.EssentialInformationPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.CancelApplicationPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PeriodSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.junit.Assert;


public class CancelApplicationPageSteps implements En {

    public CancelApplicationPageSteps(World world, OperatorStore operatorStore) {
        Given("^I am on the cancel application page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPage.clickCancelApplication();
        });
        And("^I have not confirmed I would like to cancel$", () -> {
            // Here for readability and to stop cucumber from throwing an exception
        });
        Then("^I should see the validation error message for the cancel application page$", () -> {
            Assert.assertTrue(CancelApplicationPage.hasErrorMessagePresent());
        });
        When("^I cancel my ECMT application$", CancellationPage::clickCancelCheckbox);
        Then("^I navigate to the Bilaterals cabotage page$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            PeriodSelectionPage.untilOnPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        Then("^I click save and continue on cabotage page$", () -> {
           BilateralJourneySteps.saveAndContinue();
        });
        Then("^I am navigated to cabotage page$", () -> {
            CabotagePage.untilOnPage();
        });
    }

}
