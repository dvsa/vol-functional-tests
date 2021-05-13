package org.dvsa.testing.framework.stepdefs.permits.internal.multilateral;

import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitFeesPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;


public class AnnualMultilateralInternalApplicationSteps extends BasePage implements En {
    public AnnualMultilateralInternalApplicationSteps(OperatorStore operatorStore) {

        When("^I'm  viewing my saved application in internal and Granting Permit$", () -> {
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsApplyPage.licence();
            String browser = String.valueOf(getURL());
            System.out.println(getURL());
            get(browser+"/permits/");
        });
        When("^I pay less fee for application by cash$", () -> {
            //Pay Fee
            IrhpPermitsApplyPage.selectCashPayment();
            IrhpPermitFeesPage.underPayment();
        });
        When("^I make cheque payment less than or equal to double of pending fees$", () -> {
            //Pay Fee
            IrhpPermitsApplyPage.selectChequePayment();
            IrhpPermitFeesPage.overPayment();
        });
        When("^I make cheque payment more than double of pending fees$", () -> {
            //Pay Fee
            IrhpPermitsApplyPage.selectChequePayment();
            IrhpPermitFeesPage.moreThanDoublePayment();
        });
        When("^I should be informed with the overpayment message$", IrhpPermitFeesPage::overPaymentWarningMessage);
        When("^I should be informed with the correct error message$", IrhpPermitFeesPage::moreThanDoublePaymentWarningMessage);
        When("^I select cancel button$", IrhpPermitFeesPage::cancelButton);
        Then("^I should get a warning message$", IrhpPermitFeesPage::underPaymentWarningMessage);
        When("^I make payment$", IrhpPermitFeesPage::makePaymentButton);
        And("^I should be taken back to the fees page$", () -> {
          isPath("//licence/\\d+/fees/");
        });
        And("^I pay all the fees$", () -> {
           IrhpPermitsApplyPage.selectCardPayment();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
        });
        When ("^I select continue$", IrhpPermitFeesPage::makePaymentButton);
        And("^I pay the balance by cash$", () -> {
             IrhpPermitsApplyPage.selectApplication();
             IrhpPermitsApplyPage.selectCashPayment();
             IrhpPermitFeesPage.secondPayment();
        });
        And("^there should be no outstanding fees on the fee table$", IrhpPermitFeesPage::noOutstandingFees);
        Then ("^For Short term APSG, I see only Withdraw application button against on my submitted application details page$", IrhpPermitsApplyPage::isWithdrawButtonPresent);
    }
}

