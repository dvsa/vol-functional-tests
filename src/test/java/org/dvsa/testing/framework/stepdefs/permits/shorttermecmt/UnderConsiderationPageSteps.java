package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.SectorPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CabotagePage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;


public class UnderConsiderationPageSteps implements En {

    public UnderConsiderationPageSteps(OperatorStore operatorStore, World world) {
        When ("^I am on Short term under consideration page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore)
            .licencePage(operatorStore,world);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.confirmCertificateRequired();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            NumberOfPermitsPage.enterAuthorisedVehicles();
            BasePermitPage.saveAndContinue();
            EmissionStandardsPage.confirmCheckbox();
            BasePermitPage.saveAndContinue();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.proportion(JourneyProportion.LessThan60Percent);
            SectorPage.sector(Sector.random());
            ECMTShortTermJourney.getInstance().checkYourAnswersPage();
            DeclarationPage.confirmDeclaration();
            DeclarationPage.saveAndContinue();
            PermitFeePage.submitAndPay();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            BilateralJourneySteps.clickFinishButton();
            String licence= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.select(licence);
        });
        Then ("^the page heading on under consideration page is displayed correctly$", UnderConsiderationPage::untilOnPage);
        And("^the table of contents in the short term  under consideration page are displayed correctly$", UnderConsiderationPage::tableCheck);
        And("^the warning message is displayed correctly$", () -> {
            UnderConsiderationPage.warningMessage();
        });
        When ("^I select withdraw application button$", UnderConsiderationPage::clickWithdrawApplication);
        Then("^I am taken to the Withdraw Application page$", WithdrawApplicationPage::untilOnPage);
        Then("^I am taken back to Under Consideration Page$", UnderConsiderationPage::untilOnPage);
        When ("^I go back to the permit application$", () -> {
            String licence= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.select(licence);
        });
    }
}
