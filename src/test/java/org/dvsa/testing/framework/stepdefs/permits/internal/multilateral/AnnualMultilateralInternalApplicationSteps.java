package org.dvsa.testing.framework.stepdefs.permits.internal.multilateral;

import Injectors.World;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitFeesPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.newPages.BasePage;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AnnualMultilateralInternalApplicationSteps extends BasePage implements En {

    private World world;

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
            IrhpPermitFeesPage.underPayFee();
        });
        When("^I make cheque payment less than or equal to double of pending fees$", () -> {
            //Pay Fee
            IrhpPermitsApplyPage.selectChequePayment();
            IrhpPermitFeesPage.overPayFee();
        });
        When("^I make cheque payment more than double of pending fees$", () -> {
            //Pay Fee
            IrhpPermitsApplyPage.selectChequePayment();
            IrhpPermitFeesPage.moreThanDoublePayment();
        });
        When("^I should be informed with the overpayment message$", () -> {
            String message = IrhpPermitFeesPage.getPaymentWarningMessage();
            assertEquals("The payment is more than the total outstanding. A refund will be created for the overpayment." +
                    " Would you like to continue?", message);
        });
        When("^I should be informed with the correct error message$", () -> {
            String message = IrhpPermitFeesPage.getPaymentWarningMessage();
            assertEquals("The payment is more than double the total outstanding!! A refund will be created for the overpayment." +
                    " Would you like to continue?", message);
        });
        When("^I select cancel button$", IrhpPermitFeesPage::clickCancel);
        Then("^I should get a warning message$", () -> {
            String message = IrhpPermitFeesPage.getPaymentWarningMessage();
            assertEquals("The payment amount is less than the total outstanding amount. Would you like to continue with the payment? " +
                    "The invoices will be paid in date order starting with the earliest first", message);
        });
        And("^I should be taken back to the fees page$", () -> {
          isPath("//licence/\\d+/fees/");
        });
        And("^I pay all the fees$", () -> {
            IrhpPermitsApplyPage.selectCardPayment();
            world.feeAndPaymentJourneySteps.customerPaymentModule();
        });
        When ("^I select continue$", IrhpPermitFeesPage::clickConfirmPaymentButton);
        And("^I pay the balance by cash$", () -> {
             IrhpPermitsApplyPage.selectApplication();
             IrhpPermitsApplyPage.selectCashPayment();
             IrhpPermitFeesPage.fillInCashDetailsAndPay();
        });
        And("^there should be no outstanding fees on the fee table$", () -> {
            assertTrue(IrhpPermitFeesPage.areNoOutstandingFeesPresent());
        });
        Then ("^For Short term APSG, I see only Withdraw application button against on my submitted application details page$", IrhpPermitsApplyPage::isWithdrawButtonPresent);
    }
}

