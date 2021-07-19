package org.dvsa.testing.framework.stepdefs.permits.common;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.dvsa.testing.framework.stepdefs.permits.internal.IRHPPermitsPageSteps.payOutstandingFees;
import static org.junit.Assert.assertTrue;

public class CommonSteps extends BasePage implements En {

    public static Map<String, java.net.URL> origin;

    public CommonSteps(OperatorStore operator, World world) {
        Given("^I am on the VOL self-serve site$", () -> {
            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());
        });

        Given("^I am on the VOL self-serve site with cookies$", () -> {
            refreshPage();
            String ssl="//*[@id='details-button']";
            String proceed="#proceed-link";
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());
            waitAndClick(ssl, SelectorType.XPATH);
            waitAndClick(proceed, SelectorType.CSS);
        });

        Given("^I have began applying for an (?:ECMT|Annual Bilateral|Annual Multilateral|Short-term ECMT) Permit$", () -> {
            clickToPermitTypePage(world);
        });
        Then ("^I submit the annual ECMT APGG application$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.completePage();
            BasePermitPage.saveAndContinue();
            RestrictedCountriesPage.deliverToRestrictedCountry(false);
            RestrictedCountriesPage.saveAndContinue();
            NumberOfPermitsPageJourney.completeECMTPage();
            EmissionStandardsPageJourney.completePage();
            BasePermitPage.saveAndContinue();
            DeclarationPageJourney.completeDeclaration();
            PermitFeePage.submitAndPay();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.goToPermitsDashboard();
        });
        And("^I am on the Annual ECMT licence selection page$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.beginPermitApplication();
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operator);
            YearSelectionPage.selectECMTValidityPeriod();

        });
        Then("^I will get an error message on the licence page$", () -> {
            assertTrue(SelectALicencePage.isErrorMessagePresent());
        });
        Then("^I should get an error message$", () -> {
             boolean hasError = BasePage.isErrorMessagePresent();
             assertTrue("Error message was not displayed on the page", hasError);
        });
        When("^I save and return to overview$", BasePermitPage::clickReturnToOverview);
        When("^I sign on as an external user$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());        });
        And("^all fees have been waived$", () -> {
            LicenceDetailsPageJourney.clickFeesTab();
            FeeDetailsPageJourney.whileFeesPresentWaveFee();
        });
        And("^A case worker begins to process my fee payment$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            payOutstandingFees(world);
            FeesDetailsPage.pay();
        });
        And("^I am on the permits dashboard on external$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
        });
        Then("^Information and Text appear correctly$", () -> {
            assertTrue(HomePage.PermitsTab.isPermitDashboardTextPresent());
        });
    }

    public static void clickToPermitTypePage(@NotNull World world) {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        HomePageJourney.beginPermitApplication();
    }

    public static void clickToPage(@NotNull OperatorStore operatorStore, @NotNull World world, @NotNull OverviewSection section) {
        beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
        OverviewPageJourney.clickOverviewSection(section);
    }

    public static void beginEcmtApplicationAndGoToOverviewPage(World world, OperatorStore operatorStore) {
        clickToPermitTypePage(world);
        EcmtApplicationJourney.getInstance()
                .permitType(PermitType.ECMT_ANNUAL, operatorStore);
        YearSelectionPage.selectECMTValidityPeriod();
        EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
        OverviewPage.untilOnPage();
    }
}
