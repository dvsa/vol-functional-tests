package org.dvsa.testing.framework.stepdefs.permits.internal;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.number.Int;
import activesupport.system.Properties;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
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


public class SubmitPermitApplicationSteps extends BasePage{
    World world;
    public SubmitPermitApplicationSteps(World world) {
        this.world = world;
    }
    @Given("I am on the VOL internal site")
    public void iamOnTheVolInternalSite() {
        deleteCookies();
        refreshPage();
        get(URL.build(ApplicationType.INTERNAL, Properties.get("env", true), "auth/login/").toString());
    }
    @When("I apply for an ECMT APGG Euro5 or Euro 6 application")
    public void iApplyForAnECMTAPGGEuro5OrEuro6Application() {
        completeInternalECMTApplication(PermitType.ECMT_ANNUAL, true);
    }
    @When("I apply for an ECMT permit application without selecting Euro emissions checkbox")
    public void iApplyForAnECMTPermitApplicationWithoutSelectingEuroEmissionsCheckbox() {
        completeInternalECMTApplication(PermitType.ECMT_ANNUAL, false);
    }
    @When("I apply for a short term APGG Euro5 or Euro 6 application")
    public void iApplyForAShortTermAPGGEuro5OrEuro6Application() {
        completeInternalECMTApplication(PermitType.SHORT_TERM_ECMT, true);
    }
    @When("I am in application details page, I should see application is in UC status")
    public void iAmInApplicationDetailsPageIShouldSeeApplicationIsInUCStatus() {
        assertTrue(submitButtonNotExists());
        underConsiderationStatusExists();
    }
    @When("I Grant and pay issue fee on Internal")
    public void iGrantAndPayIssueFeeOnInternal() {
        waitAndClick("//tbody/tr/td/a", SelectorType.XPATH);
        grantApplication();
        continueButton();
        viewApplication();
        permitsSelectFeeTab();
        selectApplication();
        selectCardPayment();
        world.feeAndPaymentJourney.customerPaymentModule();
    }
    @When("I am in Docs and attachments page, I should see the snapshot generate successfully")
    public void iAmInDocsAndAttachmentsPageIShouldSeeTheSnapshotGenerateSuccessfully() {
        viewApplication();
        BasePage.waitAndClick("//a[@id='menu-licence_irhp_applications-document']", SelectorType.XPATH);
        refreshPageUntilElementAppears("//a[contains(text(),'Snapshot (app submitted)')]", SelectorType.XPATH);
    }

    //Submit button Exists
    @Then("In application details page, I should see Submit button")
    public void inApplicationDetailsPageIShouldSeeSubmitButton() {
        IrhpPermitsApplyPage.submitButtonExists();
    }

    //cancel button Exists
    @Then("I am on application details page, I should see cancel button")
    public void iAmOnApplicationDetailsPageIShouldSeeCancelButton() {
        IrhpPermitsApplyPage.cancelButtonExists();
    }

    //withdraw button Exists
    @Then("I am in application details page, I should see withdraw button")
    public void iAmInApplicationDetailsPageIShouldSeeWithdrawButton() {
        LicenceDetailsPageJourney.clickIRHPTab();
        viewApplication();
        isWithdrawButtonPresent();
    }

    // withdraw button Exists under Fee details section
    @Then("I am in fee details page, I should see withdraw button")
    public void iAmInFeeDetailsPageIShouldSeeWithdrawButton() {
        refreshPage();
        LicenceDetailsPageJourney.clickFeesTab();
        isWithdrawButtonPresent();
    }

    //cancel permit application
    @Then("I cancel Permit Application on the Application details page")
    public void iCancelPermitApplicationOnTheApplicationDetailsPage() {
        cancelPermitApplication();
        viewApplication();
    }

    //withdraw permit application
    @Then("In application details page, I withdraw Permit Application")
    public void inApplicationDetailsPageIWithdrawPermitApplication() {
        withdrawPermitApplication();
        viewApplication();
    }

    //cancel status Exists
    @Then("On the application details page, I should see application as cancelled")
    public void onTheApplicationDetailsPageIShouldSeeApplicationAsCancelled() {
        IrhpPermitsApplyPage.cancelStatusExists();
    }

    //withdrawn status Exists
    @Then("I am in application details page, I should see application as withdrawn")
    public void iAmInApplicationDetailsPageIShouldSeeApplicationAsWithdraw() {
        IrhpPermitsApplyPage.withdrawStatusExists();
    }

    //Go To Fee tab
    @When("I am on the fee tab page")
    public void iAmOnTheFeeTabPage() {
        LicenceDetailsPageJourney.clickFeesTab();
        submitButtonAPSGExists();
    }
    @When("I am on the fee details page")
    public void iAmOnTheFeeDetailsPage() {
        //Go To Fee tab
        refreshPage();
        LicenceDetailsPageJourney.clickFeesTab();
    }
    //Submit Button Exists
    @Then("I should see Submit button")
    public void iShouldSeeSubmitButton() {
        IrhpPermitsApplyPage.submitButtonExists();
    }

    //Submit Application
    @When("I click on submit button")
    public void iClickOnSubmitButton() {
        IrhpPermitsApplyPage.submitApplication();
    }

    //Submit Application
    @When("I select application to pay")
    public void iSelectApplicationToPay() {
        IrhpPermitsApplyPage.selectApplication();
    }

    @When("I pay fee for application")
    public void iPayFeeForApplication() {
        //Pay Fee
        BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
        selectCardPayment();
        world.feeAndPaymentJourney.customerPaymentModule();
        FeesDetailsPage.untilFeePaidNotification();
    }

    //cancel Button not Exists
    @Then("I should not see cancel button on application details page")
    public void iShouldNotSeeCancelButtonOnApplicationDetailsPage() {
        IrhpPermitsApplyPage.cancelButtonNotExists();
    }

    //Withdraw Button not Exists
    @When("I am in application details page, I should not see withdraw button")
    public void iAmInApplicationDetailsPageIShouldNotSeeWithdrawButton() {
        IrhpPermitsApplyPage.withdrawButtonNotExists();
    }

    @And("submit my ECMT permit application")
    public void submitMyECMTPermitApplication() {
        submitIRHP();
        BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
        selectCardPayment();
        world.feeAndPaymentJourney.customerPaymentModule();
    }

    @And("I save my IRHP permit")
    public void iSaveMyIRHPPermit() {
        IrhpPermitsApplyPage.saveIRHP();
    }

    @Given("I specify more than the maximum number of permits on internal form")
    public void iSpecifyMoreThanTheMaximumNumberOfPermitsOnInternalForm() {
        int numberOfPermits = Int.random(1, getNumberOfPermits());
        //Fill application
        permitsQuantityEcmtAPGGInternal(numberOfPermits + 1);
    }

    @Then("I should get an error message in internal application")
    public void iShouldGetAnErrorMessageInInternalApplication() {
        assertTrue(IrhpPermitsApplyPage.isErrorTextPresent());
    }

    //checking cabotage validation
    @When("I have not declared not to undertake cabotage in internal")
    public void iHaveNotDeclaredNotToUndertakeCabotageInInternal() {
        waitAndClick("//label[contains(text(),'I confirm that I will not undertake cabotage journ')]", SelectorType.XPATH);
    }

    //checking declaration page validation
    @When("declaration checkbox is not selected in internal")
    public void declarationCheckboxIsNotSelectedInInternal() {
        completeInternalECMTApplicationUntilDeclaration(PermitType.ECMT_ANNUAL, true);
    }

    @Then("I should not see submit button on the application page")
    public void iShouldNotSeeSubmitButtonOnTheApplicationPage() {
        assertTrue(IrhpPermitsApplyPage.submitButtonNotExists());
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
        world.irhpPageJourney.completeModal(ECMTType);
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