package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
import activesupport.number.Int;
import activesupport.system.Properties;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.IRHPPageJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SubmitPermitApplicationSteps extends BasePage implements En {

    public SubmitPermitApplicationSteps(World world) {

        Given("^I am on the VOL internal site$", () -> {
            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.INTERNAL, Properties.get("env", true), "auth/login/").toString());
        });
        When("^I apply for an ECMT APGG Euro5 or Euro 6 application$", () -> {
            completeInternalECMTApplication(PermitType.ECMT_ANNUAL, true);
        });
        When("^I apply for an ECMT permit application without selecting Euro emissions checkbox$", () -> {
            completeInternalECMTApplication(PermitType.ECMT_ANNUAL, false);
        });

        When("^I apply for a short term APGG Euro5 or Euro 6 application$", () -> {
            completeInternalECMTApplication(PermitType.SHORT_TERM_ECMT, true);
        });

        When("^I am in application details page, I should see application is in UC status$",() -> {
            assertTrue(submitButtonNotExists());
            underConsiderationStatusExists();
        });
        When("^I Grant and pay issue fee on Internal$",() -> {
            waitAndClick("//tbody/tr/td/a", SelectorType.XPATH);
            grantApplication();
            continueButton();
            viewApplication();
            permitsSelectFeeTab();
            selectApplication();
            selectCardPayment();
            world.feeAndPaymentJourney.customerPaymentModule();

        });

        When("^I am in Docs and attachments page, I should see the snapshot generate successfully$",() -> {
            viewApplication();
            BasePage.waitAndClick("//a[@id='menu-licence_irhp_applications-document']", SelectorType.XPATH);
            refreshPageUntilElementAppears("//a[contains(text(),'Snapshot (app submitted)')]", SelectorType.XPATH);
        });
        //Submit button Exists
        Then("^In application details page, I should see Submit button$", IrhpPermitsApplyPage::submitButtonExists);

        //cancel button Exists
        Then("^I am on application details page, I should see cancel button$", IrhpPermitsApplyPage::cancelButtonExists);

        //withdraw button Exists
        Then("^I am in application details page, I should see withdraw button$", () -> {
            LicenceDetailsPageJourney.clickIRHPTab();
            viewApplication();
            isWithdrawButtonPresent();
        });
        // withdraw button Exists under Fee details section
        Then("^I am in fee details page, I should see withdraw button$", () -> {
            refreshPage();
            LicenceDetailsPageJourney.clickFeesTab();
            isWithdrawButtonPresent();
        });
        //cancel permit application
        Then("^I cancel Permit Application on the Application details page$", () -> {
            cancelPermitApplication();
            viewApplication();
        });

        //withdraw permit application
        Then("^In application details page, I withdraw Permit Application$", () -> {
            withdrawPermitApplication();
            viewApplication();
        });

        //cancel status Exists
        Then("^On the application details page, I should see application as cancelled$", IrhpPermitsApplyPage::cancelStatusExists);

        //withdrawn status Exists
        Then("^I am in application details page, I should see application as withdrawn", IrhpPermitsApplyPage::withdrawStatusExists);

        //Go To Fee tab
        When("^I am on the fee tab page$", ()->{
            LicenceDetailsPageJourney.clickFeesTab();
            submitButtonAPSGExists();
        });

        When("^I am on the fee details page$", () -> {
            //Go To Fee tab
            refreshPage();
            LicenceDetailsPageJourney.clickFeesTab();
        });
        //Submit Button Exists
        Then("^I should see Submit button$", IrhpPermitsApplyPage::submitButtonExists);

        //Submit Application
        When("^I click on submit button$", IrhpPermitsApplyPage::submitApplication);

        //Submit Application
        When("^I select application to pay$", IrhpPermitsApplyPage::selectApplication);
        When("^I pay fee for application$", () -> {
            //Pay Fee
            BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
            selectCardPayment();
            world.feeAndPaymentJourney.customerPaymentModule();
            FeesDetailsPage.untilFeePaidNotification();
        });

        //cancel Button not Exists
        Then("^I should not see cancel button on application details page$", IrhpPermitsApplyPage::cancelButtonNotExists);

        //Withdraw Button not Exists
        When("^I am in application details page, I should not see withdraw button$", IrhpPermitsApplyPage::withdrawButtonNotExists);

        And("^submit my ECMT permit application$", () -> {
            submitIRHP();

            BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);

            selectCardPayment();
            world.feeAndPaymentJourney.customerPaymentModule();
        });

        And("^I save my IRHP permit$", IrhpPermitsApplyPage::saveIRHP);

        Given("^I specify more than the maximum number of permits on internal form$", () -> {

            int numberOfPermits = Int.random(1, getNumberOfPermits());

            //Fill application
            permitsQuantityEcmtAPGGInternal(numberOfPermits+1);
        });

        Then("^I should get an error message in internal application$", () -> {
            assertTrue(IrhpPermitsApplyPage.isErrorTextPresent());
        });
        //checking cabotage validation
        When("^I have not declared not to undertake cabotage in internal", () -> {
            waitAndClick("//label[contains(text(),'I confirm that I will not undertake cabotage journ')]",SelectorType.XPATH);
        });

        //checking declaration page validation
        When("declaration checkbox is not selected in internal", () -> {
            completeInternalECMTApplicationUntilDeclaration(PermitType.ECMT_ANNUAL, true);
        });

        Then("^I should not see submit button on the application page$", () -> {
            assertTrue(IrhpPermitsApplyPage.submitButtonNotExists());
        });
    }

    public void completeInternalECMTApplication(PermitType ECMTType, boolean euro6Compliance) {
        completeInternalECMTApplicationUntilDeclaration(ECMTType, euro6Compliance);
        declare(true);

        //Save application
        saveIRHP();
    }

    public void completeInternalECMTApplicationUntilDeclaration(PermitType ECMTType, boolean euro6Compliance) {
        LicenceDetailsPageJourney.clickIRHPTab();

        //apply application
        untilOnPage();
        IRHPPageJourney.completeModal(ECMTType);
        int numberOfPermits = Int.random(1, 5);

        //Fill application
        waitForTextToBePresent("Edit");
        assertTrue(isPath("/licence/\\d+/irhp-application/edit/\\d+/"));
        emissionRadioSelectNew();
        needECMTPermit();
        cabotageEligibility();
        certificatesRequired();
        restrictedCountriesNo();
        isEuro6Compliant(euro6Compliance);

        if (ECMTType == PermitType.ECMT_ANNUAL) {
            emissionRadioSelect();
            permitsQuantityEcmtAPGGInternal(numberOfPermits);

        } else if (ECMTType == PermitType.SHORT_TERM_ECMT) {
            numberOfPermitsShortTermAPSG(numberOfPermits);
            datePermitNeededShortTermApgg();
        }

        NumberOfPermitsPageJourney.setNumberOfPermits(numberOfPermits);
    }
}
