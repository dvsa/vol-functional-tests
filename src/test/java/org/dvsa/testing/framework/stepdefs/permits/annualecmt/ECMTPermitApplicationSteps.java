package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.Journeys.permits.internal.IRHPPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static java.lang.Thread.sleep;
import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ECMTPermitApplicationSteps extends BasePermitPage implements En {

    public static Map<String, String> applicationReference;

    public ECMTPermitApplicationSteps(World world, OperatorStore operatorStore) {
        Then("^the permits tab should (not )?be displayed$", (String hidden) -> {
            if (hidden != null) {
                assertFalse(HomePage.isTabPresent(Tab.PERMITS));
            } else {
                assertTrue(HomePage.isTabPresent(Tab.PERMITS));
            }
        });
        Then("^I expect my application to be submitted$", () -> {
            HomePage.untilOnPage();
            untilAnyPermitStatusMatch(PermitStatus.UNDER_CONSIDERATION);
        });
        Then("^There should be no selected licences$", () -> assertFalse(SelectALicencePage.hasSelectedLicence()));
        And ("^I save and continue$", BasePermitPage::saveAndContinue);
        Then("^I should be taken to the permits dashboard$", () -> assertTrue(isPath(HomePage.PermitsTab.RESOURCE)));

        And("^I have completed an ECMT application$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.beginPermitApplication();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
        });
        When("^I withdraw without confirming$", () -> {
            HomePage.PermitsTab.selectFirstOngoingApplication();
            ApplicationDetailsPage.withdraw();
            WithdrawApplicationPage.clickWithdraw();
        });
        When("^I have a partial completed ECMT application$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.beginPermitApplication();
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operatorStore);
            YearSelectionPage.selectECMTValidityPeriod();
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            BasePermitPage.back();
        });
        When("^I view the application from ongoing permit application table$", HomePage.PermitsTab::selectFirstOngoingApplication);
        Then ("^I have an annual ECMT application in awaiting fee status$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.beginPermitApplication();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
            IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
            sleep(5000);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            refreshPage();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
        });
        Then ("^the user is navigated to awaiting fee page$", () -> isPath("/permits/\\d+/ecmt-awaiting-fee/"));

        When("^I try applying with a licence that has an existing annual ECMT application$", () -> {
            HomePageJourney.beginPermitApplication();
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operatorStore);
            YearSelectionPage.selectECMTValidityPeriod();
            SelectALicencePage.clickLicence(world.applicationDetails.getLicenceNumber());
            SelectALicencePage.saveAndContinue();
        });
    }

    public static void completeEcmtApplication(OperatorStore operator, World world) {
        completeEcmtApplicationConfirmation(operator, world);
        SubmittedPage.untilPageLoad();
        SubmittedPage.goToPermitsDashboard();

    }

    public static void completeEcmtApplicationConfirmation(OperatorStore operator, World world) {
        EcmtApplicationJourney.getInstance()
                .permitType(PermitType.ECMT_ANNUAL, operator);
        YearSelectionPage.selectECMTValidityPeriod();
        EcmtApplicationJourney.getInstance().licencePage(operator, world);
        completeUpToCheckYourAnswersPage(world, operator);
        CheckYourAnswerPage.untilOnPage();
        CheckYourAnswerPage.saveAndContinue();
        DeclarationPageJourney.completeDeclaration();
        PermitFeePage.saveAndContinue();
        world.feeAndPaymentJourney.customerPaymentModule();
    }

    public static LicenceStore completeUpToCheckYourAnswersPage(@NotNull World world, OperatorStore store) {
        LicenceStore licenceStore = store.getCurrentLicence().orElseGet(LicenceStore::new);
        store.withLicences(licenceStore);
        OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
        CheckIfYouNeedECMTPermitsPageJourney.completePage();
        CabotagePage.confirmWontUndertakeCabotage();
        CabotagePage.saveAndContinue();
        CertificatesRequiredPage.completePage();
        CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
        NumberOfPermitsPageJourney.completeECMTPage();
        EmissionStandardsPageJourney.completePage();
        licenceStore.setReferenceNumber(BasePermitPage.getReferenceFromPage());
        return licenceStore;

   }
}