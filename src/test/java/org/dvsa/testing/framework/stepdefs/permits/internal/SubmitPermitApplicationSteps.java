package org.dvsa.testing.framework.stepdefs.permits.internal;

import activesupport.number.Int;
import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.FeePaymentConfirmationPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;
import org.dvsa.testing.lib.pages.external.permit.enums.ShortTermRestrictedCountry;
import org.dvsa.testing.lib.pages.internal.BaseModel;
import org.dvsa.testing.lib.pages.internal.details.BaseApplicationDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.LicenceDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitFeesPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.lib.pages.BasePage.*;
import static org.dvsa.testing.lib.pages.Driver.DriverUtils.get;
import static org.dvsa.testing.lib.pages.Driver.DriverUtils.*;
import static org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class SubmitPermitApplicationSteps implements En {

    public SubmitPermitApplicationSteps(World world, OperatorStore operatorStore) {

        Given("^I am on the VOL internal site$", () -> {
            if (world.get("browser.initialised") == null) {
                world.put("browser.initialised", true);
            }

            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.INTERNAL, Properties.get("env", true), "auth/login/").toString());
        });
        Then("^I'm  viewing my saved application in internal$", () -> {
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            LicenceDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);
            viewApplication();

        });
        Then("^save the application$", IrhpPermitsApplyPage::saveIRHP);
        Then("^I apply for an annual bilateral application in internal$", () -> {
            applyforPermit();
            IrhpPermitsPage.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
            IrhpPermitsPage.Model.permitType(PermitType.ANNUAL_BILATERAL);
            IrhpPermitsPage.Model.continueButton();
            IrhpPermitsPage.Model.untilModalIsGone(Duration.LONG, TimeUnit.SECONDS);
            LicenceStore licence = operatorStore.getLatestLicence().orElseThrow(IllegalStateException::new);
            AnnualBilateralJourney.getInstance().numberOfPermits(licence).save(licence);

        });

        When("^I apply for an ECMT permit application$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
            int numberOfTrips = Int.random(1, 1000);
            boolean cabotage = true;
            boolean euro6Compliant = true;

            //apply application
            untilOnPage();
            applyAnnualEcmtApplication();

            int numberOfPermits = Int.random(1, licenceStore.getNumberOfAuthorisedVehicles());

            //Fill application
            isPath("/licence/\\d+/irhp-application/edit/\\d+/");
            needECMTPermit();
            cabotageEligibility();
            certificatesRequired();
            restrictedCountriesNo();
            emissionRadioSelectNew();
            permitsQuantityEcmtAPGGInternal(numberOfPermits);
           // permitsQuantityInternal(numberOfPermits);
            licenceStore.getEcmt().setNumberOfPermits(numberOfPermits);
           // JourneyProportion journey = internationalJourneys();
            //selectTrips(numberOfTrips);
            //Sector sector = sector();
            isEuro6Compliant(true);
            declare(true);
            operatorStore.withLicences(licenceStore);

            //Save application
            saveIRHP();
            licenceStore.getEcmt().setSubmitDate(LocalDateTime.now());
           // licenceStore.getEcmt().setInternationalBusiness(journey);
            //licenceStore.getEcmt().setSector(sector);


        });
        When("^I apply for an ECMT APGG Euro5 or Euro 6 application$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
            boolean cabotage = true;
            boolean euro6Compliant = true;

            //apply application
            untilOnPage();
            applyAnnualEcmtAPGGApplication();

            int numberOfPermits = Int.random(1, licenceStore.getNumberOfAuthorisedVehicles());

            //Fill application
            isPath("/licence/\\d+/irhp-application/edit/\\d+/");
            emissionRadioSelectNew();
            needECMTPermit();
            cabotageEligibility();
            certificatesRequired();
            restrictedCountriesNo();
            isEuro6Compliant(true);
            emissionRadioSelect();
            permitsQuantityEcmtAPGGInternal(numberOfPermits);
            licenceStore.getEcmt().setNumberOfPermits(numberOfPermits);
            declare(true);
            operatorStore.withLicences(licenceStore);

            //Save application
            saveIRHP();
            licenceStore.getEcmt().setSubmitDate(LocalDateTime.now());
        });
        When("^I apply for a short term APGG Euro5 or Euro 6 application$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
            boolean cabotage = true;

            //apply application
            untilOnPage();

            applyShortTermAPGGApplication();

            int numberOfPermits = Int.random(1, licenceStore.getNumberOfAuthorisedVehicles());

            //Fill application
            isPath("/licence/\\d+/irhp-application/edit/\\d+/");
            emissionRadioSelectNew();
            needECMTPermit();
            cabotageEligibility();
            certificatesRequired();
            restrictedCountriesNo();
            isEuro6Compliant(true);
            numberOfPermitsShortTermAPSG(numberOfPermits);
            datePermitNeededShortTermApgg();
            licenceStore.getEcmt().setNumberOfPermits(numberOfPermits);
            declare(true);
            operatorStore.withLicences(licenceStore);

            //Save application
            saveIRHP();
            licenceStore.getEcmt().setSubmitDate(LocalDateTime.now());
        });
        When("^I apply for an ECMT permit application without selecting Euro emmissions checkbox$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
            boolean cabotage = true;

            //apply application
            untilOnPage();
            applyAnnualEcmtAPGGApplication();
            int numberOfPermits = Int.random(1, licenceStore.getNumberOfAuthorisedVehicles());

            //Fill application
            isPath("/licence/\\d+/irhp-application/edit/\\d+/");
            emissionRadioSelectNew();
            needECMTPermit();
            cabotageEligibility();
            certificatesRequired();
            restrictedCountriesNo();
            emissionRadioSelect();
            permitsQuantityEcmtAPGGInternal(numberOfPermits);
            licenceStore.getEcmt().setNumberOfPermits(numberOfPermits);
            declare(true);
            operatorStore.withLicences(licenceStore);
        });
            When("^I apply for an Short term APSG permit application$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);

            int numberOfTrips = Int.random(1, 1000);
            boolean cabotage = true;
            boolean euro6Compliant = true;

            //apply application
            untilOnPage();
            applyShortTermPermitAPSGWithoutSectors();

          int numberOfPermits = Int.random(1, licenceStore.getNumberOfAuthorisedVehicles());

            //Fill application
            permitsQuantityShortTermAPSG(numberOfPermits);
            checkBoxClickedSaveContinue();
            JourneyProportion journey = internationalJourneys();
            selectTripsAPSG(numberOfTrips);
             fillAPSG();
            countries(ShortTermRestrictedCountry.random());

            declare(true);
            operatorStore.withLicences(licenceStore);

            //Save application
            saveIRHP();
             untilOnPage();

        });

        When("^I am in application details page, I should see application is in UC status$",() -> {
            submitButtonNotExists();
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
            EcmtApplicationJourney.getInstance().cardDetailsPage().cardHolderDetailsPage();
            FeePaymentConfirmationPage.makeMayment();

        });
        And("^the ECMT APGG application goes to valid status$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);
            HomePage.PermitsTab.untilPermitHasStatus(
                    licenceStore.getLicenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES
            );

        });
        When("^I am in Docs and attachments page, I should see the snapshot generate successfully$",() -> {
            viewApplication();
            BasePage.waitAndClick("//a[@id='menu-licence_irhp_applications-document']",SelectorType.XPATH);
           untilPresentWithRefresh("//a[contains(text(),'Snapshot (app submitted)')]",SelectorType.XPATH,120L,TimeUnit.SECONDS);
        });
        //Submit button Exists
        Then("^In application details page, I should see Submit button$", IrhpPermitsApplyPage::submitButtonExists);


        //cancel button Exists
        Then("^I am on application details page, I should see cancel button$", IrhpPermitsApplyPage::cancelButtonExists);


        //withdraw button Exists
        Then("^I am in application details page, I should see withdraw button$", () -> {
            LicenceDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);
            viewApplication();
            isWithdrawButtonPresent();
        });
        // withdraw button Exists under Fee details section
        Then("^I am in fee details page, I should see withdraw button$", () -> {
            refreshPage();
            waitUntilElementIsEnabled("//a[@id='menu-licence_irhp_applications-fees']",SelectorType.XPATH,60L,TimeUnit.SECONDS);
            LicenceDetailsPage.Tab.select(DetailsTab.Fees);
            isWithdrawButtonPresent();
        });
        //cancel permit application
        Then("^I cancel Permit Application on the Application details page$", () -> {
            cancelPermitApplication();
            viewApplication();
        });

        //withdraw permit application
        Then("^In application details page, I withdraw Permit Application$", () -> {
          //  viewApplication();
            withdrawPermitApplication();
            viewApplication();
        });

        //cancel status Exists
        Then("^On the application details page, I should see application as cancelled$", IrhpPermitsApplyPage::cancelStatusExists);

        //withdrawn status Exists
        Then("^I am in application details page, I should see application as withdrawn", IrhpPermitsApplyPage::withdrawStatusExists);

        //Go To Fee tab
        When("^I am on the fee tab page$", ()->{
            BaseApplicationDetailsPage.Tab.select(BaseApplicationDetailsPage.DetailsTab.Fees);
            submitButtonAPSGExists();
        });
        //Go To first Fee tab

        When("^I am on the first fee tab page$", ()->{
            BaseDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.Fees);
        });
        When("^I click the application link on the fees page$", ()->{
          IrhpPermitFeesPage.feeDetailsLink();
        });
        Then ("^I should be in the edit fee page$", ()->{
            isPath("/licence/\\d+/fees/edit-fee/\\d+/");
        });
        Then ("^the fee gets refunded with the status updated to cancelled$", ()->{
           Assert.assertEquals(BasePage.getElementValueByText("//span[@class='status red']",SelectorType.XPATH),"CANCELLED");
        });
        When ("^I select application to refund$", () -> {
            BaseApplicationDetailsPage.Tab.select(BaseApplicationDetailsPage.DetailsTab.Processing);
            BaseApplicationDetailsPage.Tab.select(BaseApplicationDetailsPage.DetailsTab.Fees);
            String browser = String.valueOf(getURL());
            get(browser+"/?i=e&status=all&sort=id&order=DESC");
            FeesDetailsPage.select1stFee();
            isPath("/licence/\\d+/fees/edit-fee/");
            FeesDetailsPage.refundFeeButton();
            FeesDetailsPage.refundAccept();
        });
        Then("^I should see Fee Amount calculated correctly$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            String expectedFeeAmount = String.valueOf(licenceStore.getEcmt().getNumberOfPermits()*10);
            int expectedFee = Integer.parseInt(expectedFeeAmount);
            int actualFee = IrhpPermitFeesPage.getFeeAmount();
            assertThat(actualFee, is(expectedFee));
        });
        Then("^I should see Pay Fee Amount calculated correctly$", () -> {
            BaseModel.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
            int expectedFeeAmount = operatorStore.getLicences().get(0).getEcmt().getNumberOfPermits()*10;
            int actualFeeAmount = IrhpPermitFeesPage.getFeeAmountInFrame();
            assertThat(actualFeeAmount, is(expectedFeeAmount));
        });

        Then("^I should see Outstanding balance calculated correctly$", () ->{
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            String expectedOutstandingBalance = String.valueOf(licenceStore.getEcmt().getNumberOfPermits()*10);
            int expectedOutstandingFee = Integer.parseInt(expectedOutstandingBalance);
            int actualOutstandingBalance = IrhpPermitFeesPage.getFeeOutstanding();
            assertThat(actualOutstandingBalance, is(expectedOutstandingFee));
        });

        Then("^In Details page, I should see Outstanding balance calculated correctly$", () ->{
            int expectedOutstandingBalance = operatorStore.getLicences().get(0).getEcmt().getNumberOfPermits()*10;
            int actualOutstandingBalance = IrhpPermitFeesPage.getOutstandingFromDetails();
            assertThat(actualOutstandingBalance, is(expectedOutstandingBalance));
        });

        Then("^In Details page, I should see Fee Amount calculated correctly$", () ->{
            int expectedFeeAmount = operatorStore.getLicences().get(0).getEcmt().getNumberOfPermits()*10;
            int actualFeeAmount = IrhpPermitFeesPage.getFeeAmountFromDetails();
            assertThat(actualFeeAmount, is(expectedFeeAmount));
        });
        When("^I am on the fee details page$", () -> {
            //Go To Fee tab
            refreshPage();
            waitUntilElementIsEnabled("//a[@id='menu-licence_fees']",SelectorType.XPATH,60L,TimeUnit.SECONDS);
            LicenceDetailsPage.Tab.select(DetailsTab.Fees);
        });
        //Submit Button Exists
        Then("^I should see Submit button$", IrhpPermitsApplyPage::submitButtonExists);

        //Submit Application
        When("^I click on submit button$", IrhpPermitsApplyPage::submitApplication);

        //Submit Application
        When("^I select application to pay$", IrhpPermitsApplyPage::selectApplication);
        When("^(?:I pay fee for application|I pay for all outstanding fees)$", () -> {
            //Pay Fee
            BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
            selectCardPayment();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage();
            FeePaymentConfirmationPage.makeMayment();
            FeesDetailsPage.untilFeePaidNotification();
        });

        //Submit Button Exists
        When("^I am in application details page, I should not see submit button$", IrhpPermitsApplyPage::submitButtonNotExists);

        //cancel Button not Exists
        Then("^I should not see cancel button on application details page$", IrhpPermitsApplyPage::cancelButtonNotExists);

        //Withdraw Button not Exists
        When("^I am in application details page, I should not see withdraw button$", IrhpPermitsApplyPage::withdrawButtonNotExists);

        And("^submit my ECMT permit application$", () -> {
            submitIRHP();

            BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);

            selectCardPayment();
            EcmtApplicationJourney.getInstance().cardDetailsPage().cardHolderDetailsPage();
            FeePaymentConfirmationPage.makeMayment();
        });
        //apply application
        When("^I apply for a new permit application$", () -> {
            untilOnPage();
            applyPermit();
        });

        And("^I save my IRHP permit$", IrhpPermitsApplyPage::saveIRHP);

        Given("^I specify more than the maximum number of permits on internal form$", () -> {

            int numberOfPermits = Int.random(1, getNumberOfPermits());

            //Fill application
            permitsQuantityEcmtAPGGInternal(numberOfPermits+1);
        });

        Then("^I should get an error message in internal application$", () -> {
            Assert.assertTrue(hasErrorPresent());
        });
        //checking cabotage validation
        When("^I have not declared not to undertake cabotage in internal", () -> {
            waitAndClick("//label[contains(text(),'I confirm that I will not undertake cabotage journ')]",SelectorType.XPATH);
        });
        //checking Euro6 validation
        When("^I have not declared Euro 6 compliance in internal", () -> {
            waitAndClick("//label[contains(text(),'I confirm that I will only use my ECMT permits wit')]",SelectorType.XPATH);
        });
        When("^I should get the percentage of international journeys error message", () -> {
         Assert.assertEquals(BasePage.getElementValueByText("//p[contains(text(),'Select the percentage of international')]", SelectorType.XPATH),"Select the percentage of international journeys over the past 12 months");
        });
        When("^I should get the sector error message", () -> {
            Assert.assertEquals(BasePage.getElementValueByText("//p[contains(text(),'Select one main sector only')]", SelectorType.XPATH),"Select one main sector only");
        });
        When("^I should get the declaration error message", () -> {
            Assert.assertEquals(BasePage.getElementValueByText("//p[@class='error__text']", SelectorType.XPATH),"Select one main sector only");
        });
        When("^percentage of international journey checkbox is not selected", () -> {
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
            int numberOfTrips = Int.random(1, 1000);
            boolean cabotage = true;
            boolean euro6Compliant = true;

            //apply application
            int numberOfPermits = Int.random(1, operatorStore.getLatestLicence().get().getNumberOfAuthorisedVehicles());
            untilOnPage();
            applyAnnualEcmtApplication();

            //Fill application
            emissionRadioSelectNew();
            permitsQuantityInternal(numberOfPermits);
            checkBoxClickedSaveContinue();
            selectTrips(numberOfTrips);
            Sector sector = sector();
            restrictedCountriesNo();
            declare(true);
        });
//checking sector page validation
        When("sectors are not selected in internal", () -> {
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
            int numberOfTrips = Int.random(1, 1000);
            boolean cabotage = true;
            boolean euro6Compliant = true;

            //apply application
            int numberOfPermits = Int.random(1, operatorStore.getLatestLicence().get().getNumberOfAuthorisedVehicles());
            untilOnPage();
            applyAnnualEcmtApplication();

            //Fill application
            emissionRadioSelectNew();
            permitsQuantityInternal(numberOfPermits);
            checkBoxClickedSaveContinue();
            selectTrips(numberOfTrips);
            JourneyProportion journey = internationalJourneys();
            restrictedCountriesNo();
            declare(true);
        });
//checking declaration page validation
        When("declaration checkbox is not selected in internal", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
            boolean cabotage = true;
            boolean euro6Compliant = true;

            //apply application
            int numberOfPermits = Int.random(1,
                    operatorStore.getLatestLicence().get().getNumberOfAuthorisedVehicles());
            untilOnPage();
            applyAnnualEcmtApplication();

            //Fill application
            emissionRadioSelectNew();
            needECMTPermit();
            cabotageEligibility();
            certificatesRequired();
            restrictedCountriesNo();
            isEuro6Compliant(true);
            emissionRadioSelect();
            permitsQuantityEcmtAPGGInternal(numberOfPermits);
            licenceStore.getEcmt().setNumberOfPermits(numberOfPermits);

        });
        Then("^I should not see submit button on the application page$", IrhpPermitsApplyPage::submitButtonNotExists);
    }

}
