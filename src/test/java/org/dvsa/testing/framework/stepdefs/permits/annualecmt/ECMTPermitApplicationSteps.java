package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import activesupport.IllegalBrowserException;
import activesupport.string.Str;
import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import apiCalls.eupaActions.external.ECMTPermitApplicationAPI;
import cucumber.api.java8.En;
import org.apache.commons.lang3.StringUtils;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.PermitApplication;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.ChangeYourPasswordPage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.ApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.ecmt.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.ecmt.UnderConsiderationPage;
import org.dvsa.testing.lib.pages.external.permit.enums.AnnualEcmtPermitUsage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.EuroEmissioStandardsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;
import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.signIn;
import static org.dvsa.testing.lib.pages.external.HomePage.untilOnHomePage;

public class ECMTPermitApplicationSteps extends BasePage implements En {

    public ECMTPermitApplicationSteps(World world, OperatorStore operatorStore) {
        When("^I fill in the permits form$", () -> {
            completeEcmtApplication(operatorStore, world);
        });
        Then("^(?:I expect my application to be submitted|my submitted application should be on the dashboard)$", () -> {
            untilOnHomePage();
            untilAnyPermitStatusMatch(PermitStatus.UNDER_CONSIDERATION);
            //System.out.println(HomePage.hasReferenceNumber(world.get("referenceNumber")));
            //Assert.assertTrue(HomePage.hasReferenceNumber(world.get("referenceNumber")));
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

        When("^I view the permits tab$", () -> {
            signIn(world);
            changePassword(world);
            HomePage.selectTab(Tab.PERMITS);
        });
        Then("^I should see all permit applications$", () -> {
            List<PermitApplication> expectedApplications = ECMTPermitApplicationAPI.get(world.get("organisationId")).getResults()
                    .stream()
                    .sorted((o1, o2) -> o2.getPermitId().compareTo(o1.getPermitId()))
                    .map(permit -> {
                                return new PermitApplication()
                                        .withReferenceNumber(permit.getApplicationRef())
                                        .withNoOfPermits(permit.getPermitsRequired())
                                        .withType(permit.getPermitType().getDescription())
                                        .withStatus(permit.getStatus().getDescription().toString());
                            }
                    ).collect(Collectors.toList());
            List<PermitApplication> actualApplications = HomePage.PermitsTab.ongoingPermitApplications();

            assertApplicationsAreAllEqual(expectedApplications, actualApplications);
        });
        Then("^ongoing permits should be sorted by permit ID in descending order$", () -> {
            List<PermitApplication> actualApplications = HomePage.PermitsTab.ongoingPermitApplications();
            for (int i = 0; i < (actualApplications.size() - 1); i++) {
                Assert.assertThat(actualApplications.get(i).getId(), Matchers.greaterThan(actualApplications.get(i + 1).getId()));
            }
        });
        Then("^only ECMT applications with the right status are displayed$", () -> {
            List<PermitApplication> actualApplications = HomePage.PermitsTab.ongoingPermitApplications();
            boolean allMatch = actualApplications.stream().allMatch(application ->
                    application.getStatus() == PermitStatus.UNDER_CONSIDERATION ||
                            application.getStatus() == PermitStatus.NOT_YET_SUBMITTED
            );

            Assert.assertTrue("There was one or more applications with an incorrect permit status", allMatch);
        });
        Given("^I have completed all sections$", () -> {
            completeUpToCheckYourAnswersPage(world, operatorStore);
            CheckYourAnswersPage.saveAndContinue();
            DeclarationPage.declare(true);
            DeclarationPage.saveAndContinue();
        });
        And("^I have completed (an|all) ECMT application$", (String arg) -> {
            signIn(world);
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
        });
        When("^I try applying for an annual ECMT again$", () -> {
            HomePage.applyForLicenceButton();
            AnnualBilateralJourney.getInstance().permitType();
        });
        Given("^I have a high intensity of use for number of permits$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPage.section(PermitSection.CheckIfYouNeedECMTPermits);
            AnnualEcmtPermitUsagePage.annualEcmtPermitUsage(AnnualEcmtPermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.wontCarryCabotage(true);
            CertificatesRequiredPage.certificatesRequired(true);
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            NumberOfPermitsPage.permitsValue();
            BasePermitPage.saveAndContinue();
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
            EcmtApplicationJourney.getInstance()
                    .numberOfTripsPage(world,licenceStore);
        });
        When("^I withdraw without confirming$", () -> {
            HomePage.PermitsTab.selectOngoing(world.get("application.reference"));
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
        Then("^I have partial annual ECMT application$", () -> {
            signIn(world);
            IntStream.range(0, operatorStore.getLicences().size()).forEach((i) -> {
               /* try {
                    HomePage.selectTab(Tab.PERMITS);
                } catch (MalformedURLException | IllegalBrowserException e) {
                    e.printStackTrace();
                }
                try {
                    HomePage.applyForLicenceButton();
                } catch (MalformedURLException | IllegalBrowserException e) {
                    e.printStackTrace();
                }
                try {
                    EcmtApplicationJourney.getInstance()
                            .permitType(PermitTypePage.PermitType.EcmtAnnual, operatorStore);
                } catch (MalformedURLException | IllegalBrowserException e) {
                    e.printStackTrace();
                }
                try {
                    EcmtApplicationJourney.getInstance().yearSelection(YearSelectionPage.YearSelection.YEAR_2020, operatorStore);
                } catch (MalformedURLException | IllegalBrowserException e) {
                    e.printStackTrace();
                }
                try {
                    EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
                } catch (MalformedURLException | IllegalBrowserException e) {
                    e.printStackTrace();
                }
                try {
                    BasePermitPage.back();
                } catch (MalformedURLException | IllegalBrowserException e) {
                    e.printStackTrace();
                }*/
            });
        });
        When("^I have a partial completed ECMT application$", () -> {
          signIn(world);
          HomePage.selectTab(Tab.PERMITS);
          HomePage.applyForLicenceButton();
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.EcmtAnnual, operatorStore);
            YearSelectionPage.EcmtValidityPeriod();
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            BasePermitPage.back();
        });
        When("^I view the application from ongoing permit application table$", () -> {
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
        });
        Then ("^I am on the annual ECMT application overview page$", OverviewPage::overviewPageHeading);
        Then ("^I have an annual ECMT application in under consideration status$", () -> {
            signIn(world);
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
        });
        Then ("^I have an annual ECMT application in awaiting fee status$", () -> {
            signIn(world);
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());
            deleteCookies();
            refreshPage();
            BaseInternalJourney.getInstance().openLicence(
                    licence.getLicenceId()
            ).signin();
            IrhpPermitsApplyPage.licence();
            // TODO: Need to fix this. This selects a licence near on random and goes to internal and navigates to the
            //  wrong licence and can't find the permit half the time.
            String browser = String.valueOf(getURL());
            get(browser+"irhp-application/");
            IrhpPermitsApplyPage.viewApplication();
           // BasePermitPage.waitAndClick("//input[@id='checked']", SelectorType.XPATH);
            //IrhpPermitsApplyPage.saveIRHP();
            //IrhpPermitsApplyPage.viewApplication();
            IrhpPermitsApplyPage.grantApplication();
            IrhpPermitsApplyPage.continueButton();
            sleep(5000);
            deleteCookies();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true)).toString());
            waitForTextToBePresent("Sign in to your Vehicle Operator Licensing account                ");
            LoginPage.signIn(world.get("username"), world.get("password"));
            HomePage.selectTab(Tab.PERMITS);
            refreshPage();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);

        });
        Then ("^the user is navigated to under consideration page$", UnderConsiderationPage::untilOnPage);
        Then ("^the user is navigated to awaiting fee page$", () -> isPath("/permits/\\d+/ecmt-awaiting-fee/"));

        When("^I try applying with a licence that has an existing annual ECMT application$", () -> {
          // HomePage.untilOnHomePage();
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.EcmtAnnual, operatorStore);
            YearSelectionPage.EcmtValidityPeriod();
            LicenceStore licence = operatorStore.getLicences().get(0);
            LicencePage.licence(licence.getLicenceNumber());
            LicencePage.saveAndContinue();
        });
    }

    public static void completeEcmtApplication(World world) {
        completeEcmtApplication(new OperatorStore(), world);
    }

    public static void completeEcmtApplication(OperatorStore operator, World world) {
        EcmtApplicationJourney.getInstance()
                .permitType(PermitTypePage.PermitType.EcmtAnnual, operator);
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
                BaseApplicationSubmitPage.untilSubmittedPageLoad();
        ApplicationSubmitPage.finish();

    }

    public static void completeEcmtApplicationConfirmation(OperatorStore operator, World world) {
        EcmtApplicationJourney.getInstance()
                .permitType(PermitTypePage.PermitType.EcmtAnnual, operator);
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
        OverviewPage.section(PermitSection.CheckIfYouNeedECMTPermits);
        CheckIfYouNeedECMTPermitsPage.needECMTPermits(true);
        BasePermitPage.saveAndContinue();
        CabotagePage.wontCarryCabotage(true);
        CertificatesRequiredPage.certificatesRequired(true);
        CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
        NumberOfPermitsPage.permitsValue();
        BasePermitPage.saveAndContinue();
        EuroEmissioStandardsPage.Emissionsconfirmation();
        BasePermitPage.saveAndContinue();
        licenceStore.setReferenceNumber(CheckYourAnswersPage.reference());
        world.put("application.reference",licenceStore.getReferenceNumber());
        store.withLicences(licenceStore);
        return licenceStore;

   }
    private static void changePassword(@NotNull World world)  {
        String newPassword = Str.randomWord(7).concat("1");
        if (ChangeYourPasswordPage.onPage()) {
            ChangeYourPasswordPage.update(world.get("password"), newPassword);

            world.put("password", newPassword);
        }
    }

    private static void assertApplicationsAreAllEqual(List<PermitApplication> expected, List<PermitApplication> actual) {
        for (int i = 0; i < expected.size(); i++){
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }
}