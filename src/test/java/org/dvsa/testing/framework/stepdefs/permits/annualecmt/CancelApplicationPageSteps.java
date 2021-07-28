package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.external.enums.JourneyType;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;

import static org.junit.Assert.assertEquals;


public class CancelApplicationPageSteps extends BasePermitPage implements En {

    public CancelApplicationPageSteps(World world, OperatorStore operatorStore) {
        Given("^I am on the cancel application page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPage.clickCancelApplication();
        });
        And("^I have not confirmed I would like to cancel$", () -> {
            // Here for readability and to stop cucumber from throwing an exception
        });
        Then("^I should get an error message on cancel application page$", () -> {
            assertEquals("Tick to confirm you want to withdraw your application", CancellationPage.getErrorText());
        });
        Then("^I should get an error message on Annual ECMT cancel application page$", () -> {
            assertEquals("You must select the checkbox to continue", CancellationPage.getErrorText());
        });
        When("^I cancel my ECMT application$", CancellationPage::saveAndContinue);
        Then("^I navigate to the Bilaterals cabotage page$", () -> {
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            PeriodSelectionPage.untilOnPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        Then("^I click save and continue on cabotage page$", CabotagePage::saveAndContinue);
        Then("^I am navigated to cabotage page$", () -> {
            CabotagePage.ECMTRemovalsUntilOnPage();
            String heading = CabotagePage.getPageHeading();
            assertEquals("ECMT permits do not allow you to carry out cabotage", heading);
        });
    }

}
