package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.apache.commons.lang3.StringUtils;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.PermitApplication;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.newPages.permits.pages.SubmittedPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.ecmt.UnderConsiderationPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.EuroEmissioStandardsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;
import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;

public class ECMTPermitApplicationSteps extends BasePermitPage implements En {

    public static Map<String, String> applicationReference;

    public ECMTPermitApplicationSteps(World world, OperatorStore operatorStore) {
        When("^I fill in the permits form$", () -> {
            completeEcmtApplication(operatorStore, world);
        });
        Then("^the permits tab should (not )?be displayed$", (String hidden) -> {
            if (hidden != null && StringUtils.deleteWhitespace(hidden).toLowerCase().equals("not")) {
                HomePage.tabIsNotPresent(Tab.PERMITS);
            } else {
                HomePage.tabIsPresent(Tab.PERMITS);
            }
        });
        Then("^There should be no selected licences$", () -> Assert.assertFalse(LicencePage.hasSelectedLicence()));
        And ("^I save and continue$", () -> BasePermitPage.saveAndContinue());
        Then("^I should be taken to the permits dashboard$", () -> Assert.assertTrue(isPath(HomePage.PermitsTab.RESOURCE)));

        And("^I have completed (an|all) ECMT application$", (String arg) -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
        });
        When("^I try applying for an annual ECMT again$", () -> {
            HomePage.applyForLicenceButton();
            AnnualBilateralJourney.getInstance().permitType();
        });
        When("^I withdraw without confirming$", () -> {
            HomePage.PermitsTab.selectOngoing(ECMTPermitApplicationSteps.applicationReference.get("application.reference"));
            ApplicationDetailsPage.withdraw();
            WithdrawApplicationPage.withdraw();
        });
        Then("^issued permits should be sorted by reference number in descending order$", () -> {
            List<PermitApplication> actualApplications = HomePage.PermitsTab.issuedPermitApplications();
            IntStream.range(0, actualApplications.size() - 1).forEach((i) -> {
                String ref2 = actualApplications.get(i).getReferenceNumber();
                String ref1 = actualApplications.get(i + 1).getReferenceNumber();

                Assert.assertThat(ref1, Matchers.greaterThanOrEqualTo(ref2));
            });
        });
        When("^I have a partial completed ECMT application$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
          HomePage.applyForLicenceButton();
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operatorStore);
            YearSelectionPage.EcmtValidityPeriod();
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            BasePermitPage.back();
        });
        When("^I view the application from ongoing permit application table$", () -> {
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
        });
        Then ("^I am on the annual ECMT application overview page$", org.dvsa.testing.lib.newPages.permits.pages.OverviewPage::hasPageHeading);
        Then ("^I have an annual ECMT application in awaiting fee status$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());

            IrhpPermitsApplyPage.licence();
            // TODO: Need to fix this. This selects a licence near on random and goes to internal and navigates to the
            //  wrong licence and can't find the permit half the time.
            String browser = String.valueOf(getURL());
            get(browser+"irhp-application/");
            IrhpPermitsApplyPage.viewApplication();
            IrhpPermitsApplyPage.grantApplication();
            IrhpPermitsApplyPage.continueButton();
            sleep(5000);
            deleteCookies();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true)).toString());
            waitForTextToBePresent("Sign in to your Vehicle Operator Licensing account                ");
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            refreshPage();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);

        });
        Then ("^the user is navigated to under consideration page$", UnderConsiderationPage::untilOnPage);
        Then ("^the user is navigated to awaiting fee page$", () -> isPath("/permits/\\d+/ecmt-awaiting-fee/"));

        When("^I try applying with a licence that has an existing annual ECMT application$", () -> {
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operatorStore);
            YearSelectionPage.EcmtValidityPeriod();
            LicencePage.licence(world.applicationDetails.getLicenceNumber());
            LicencePage.saveAndContinue();
        });
    }

    public static void completeEcmtApplication(OperatorStore operator, World world) {
        EcmtApplicationJourney.getInstance()
                .permitType(PermitType.ECMT_ANNUAL, operator);
        YearSelectionPage.EcmtValidityPeriod();
        EcmtApplicationJourney.getInstance().licencePage(operator, world);
        LicenceStore licenceStore = completeUpToCheckYourAnswersPage(world, operator);

        EcmtApplicationJourney.getInstance()
                .checkYourAnswersPage()
                .declaration(true)
                .feeOverviewPage()
                .cardDetailsPage()
                .cardHolderDetailsPage()
                .confirmAndPay()
                .passwordAuthorisation();
        SubmittedPage.untilPageLoad();
        SubmittedPage.goToPermitsDashboard();

    }

    public static void completeEcmtApplicationConfirmation(OperatorStore operator, World world) {
        EcmtApplicationJourney.getInstance()
                .permitType(PermitType.ECMT_ANNUAL, operator);
        YearSelectionPage.EcmtValidityPeriod();
        EcmtApplicationJourney.getInstance()
                .licencePage(operator, world);
        LicenceStore licenceStore = completeUpToCheckYourAnswersPage(world, operator);

        EcmtApplicationJourney.getInstance()
                .checkYourAnswersPage()
                .declaration(true)
                .feeOverviewPage()
                .cardDetailsPage()
                .cardHolderDetailsPage()
                .confirmAndPay()
                .passwordAuthorisation();
    }

    public static LicenceStore completeUpToCheckYourAnswersPage(@NotNull World world, OperatorStore store) {
        LicenceStore licenceStore = store.getCurrentLicence().orElseGet(LicenceStore::new);
        store.withLicences(licenceStore);
        org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
        CheckIfYouNeedECMTPermitsPage.needECMTPermits(true);
        CheckIfYouNeedECMTPermitsPage.saveAndContinue();
        CabotagePage.wontCarryCabotage(true);
        CertificatesRequiredPage.certificatesRequired(true);
        CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
        NumberOfPermitsPage.permitsValue();
        NumberOfPermitsPage.saveAndContinue();
        EuroEmissioStandardsPage.Emissionsconfirmation();
        EuroEmissioStandardsPage.saveAndContinue();
        licenceStore.setReferenceNumber(BasePermitPage.getReferenceFromPage());
        ECMTPermitApplicationSteps.applicationReference.put("application.reference",licenceStore.getReferenceNumber());
        store.withLicences(licenceStore);
        return licenceStore;

   }
}