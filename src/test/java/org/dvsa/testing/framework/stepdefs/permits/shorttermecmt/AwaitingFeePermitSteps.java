package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.DeclarationPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.ecmt.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.PermitStartDatePage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.dvsa.testing.lib.pages.internal.details.LicenceDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CabotagePage.cabotageConfirmation;
import static org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CertificatesRequiredPage.CertificatesRequiredConfirmation;
import static org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage.select;

public class AwaitingFeePermitSteps extends BasePermitPage implements En {

    public AwaitingFeePermitSteps(OperatorStore operatorStore, World world) {
        And("^I have a short term application in awaiting fee status$", () -> {
            CommonSteps.clickToPermitTypePage(world);
            ECMTShortTermJourney.getInstance()
                    .permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            PeriodSelectionPageOne.periodSelection();
                    PeriodSelectionPageOne.continueButton();
            ECMTShortTermJourney.getInstance()
                    .licencePage(operatorStore, world);
            select(OverviewPage.Section.CheckIfYouNeedPermits);
            saveAndContinue();
            CheckIfYouNeedECMTPermitsPage.hasErrorMessagePresent();
            CheckIfYouNeedECMTPermitsPage.checkboxSelection();
            CheckIfYouNeedECMTPermitsPage.saveAndContinue();
            cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.permitsValue();
            saveAndContinue();
            PermitStartDatePage.permitDate();
            BasePermitPage.saveAndContinue();
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
            saveAndContinue();
            DeclarationPage.declare(true);
            DeclarationPage.saveAndContinue();
            PermitFee.submitAndPay();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .passwordAuthorisation();
            SubmittedPage.untilOnPage();
            BilateralJourneySteps.clickFinishButton();


            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());

            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);

        LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
        operatorStore.withLicences(licenceStore);

            IrhpPermitsApplyPage.licence();
            String browser = String.valueOf(getURL());
            get(browser+"irhp-application/");
            IrhpPermitsApplyPage.viewApplication();
            IrhpPermitsApplyPage.grantApplication();
            IrhpPermitsApplyPage.continueButton();

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            String licence1 = operatorStore.getCurrentLicenceNumber().toString().substring(9, 18);
            HomePage.PermitsTab.selectOngoing(licence1);
        });
        And("^the user is taken to the awaiting fee page$", () -> {
            Assert.assertTrue(isPath("/permits/application/\\d+/awaiting-fee/"));
//            Browser.Wait.untilUrlMatches("/permits/application/\\d+/awaiting-fee/", 1000L, ChronoUnit.SECONDS);
        });
        And("^I select Decline Permits button$", PermitFee::declinePermits);
        And("^I should be on the short term decline awarded permits page$", DeclineAwardedPermitPage::untilOnPage);
        And("^I should see all the relevant advisory texts$", DeclineAwardedPermitPage::advisoryText);
        And("^I select the decline confirmation checkbox and confirm$", () -> {
            DeclineAwardedPermitPage.declineConfirmation();
            DeclineAwardedPermitPage.acceptAndContinue();
        });
        And("^I am taken to the permits declined page$", PermitsDeclinedPage::untilOnPage);
        And("^I should see all the relevant texts on permits declined page$", PermitsDeclinedPage::advisoryText);
        //Guidance link removed from the permits decline page
        //And("^the guidance on permits link opens in a new window$", () -> PermitsDeclinedPage.guidanceOnPermitsLink(1000L, ChronoUnit.MILLIS));
        And("^the declined permit application is not displayed on the permit dashboard$", () -> {
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            String permitLicence= String.valueOf(HomePage.PermitsTab.permitsWithStatus(HomePage.PermitsTab.Table.ongoing, PermitStatus.AWAITING_FEE));
            Assert.assertNotEquals(licence1,permitLicence);
        });
        And("^I select accept and continue button without confirming decline checkbox$", DeclineAwardedPermitPage::acceptAndContinue);
        And("^the error message is displayed$", DeclineAwardedPermitPage::errorText);
        And("^I click the view permit restriction link$", CandidatePermitsPage::viewPermitRestrictionLink);
        And("^the user is taken to the allocated candidate permit view page$", () -> {
            isPath("/application/\\d+/unpaid-permits/");
        });
        And("^the details are displayed as expected$", () -> {
             CandidatePermitsPage.pageHeading();
             CandidatePermitsPage.tableCheck();
        });
        And("^I select the return to fee overview link$", CandidatePermitsPage::returnToFeeOverview);

    }
}