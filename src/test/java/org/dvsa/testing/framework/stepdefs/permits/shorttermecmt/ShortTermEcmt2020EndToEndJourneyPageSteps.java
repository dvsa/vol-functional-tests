package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;


import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EmissionStandardsPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.AnnualTripsAbroadPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.ProportionOfInternationalJourneyPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.SectorPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;
import static org.dvsa.testing.lib.pages.BasePage.untilExpectedTextInElement;
import static org.junit.Assert.assertEquals;

public class ShortTermEcmt2020EndToEndJourneyPageSteps implements En {
    public ShortTermEcmt2020EndToEndJourneyPageSteps(OperatorStore operatorStore, World world)  {
        LicenceStore licenceStore = operatorStore.getCurrentLicence().orElseGet(LicenceStore::new);
        And("^I select Short term ecmt permit on the select permit page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
        });
        Then("^I select year on the select year page$", YearSelectionPage::selectShortTermValidityPeriod);

        Then("^I select any licence number for short term permit$", () -> {
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
        });
        Then("^I am on short term ECMT Application overview Page$", org.dvsa.testing.lib.newPages.permits.pages.OverviewPage::untilOnPage);
        Then("^I complete the How will you use the permits section and click save and continue$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
        });
        Then("^I complete the Check if you need ECMT permits section and click save and continue$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPage.checkNeedECMTPermits();
            CheckIfYouNeedECMTPermitsPage.saveAndContinue();
        });
        Then("^I complete Cabotage page section and click save and continue$", () -> {
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
        });
        Then("^I complete Certificates required page section and click save and continue$", () -> {
            String heading = CertificatesRequiredPage.getPageHeading();
            assertEquals("Mandatory certificates for vehicles and trailers you intend to use", heading);
            CertificatesRequiredPage.completePage();
        });
        Then("^I complete Countries with limited permits section and click save and continue$", () -> {
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            BasePermitPage.saveAndContinue();
        });
        Then("^I complete Number of permits required section and click save and continue$", () -> {
            NumberOfPermitsPage.hasPageHeading();
            NumberOfPermitsPage.enterEuro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
        });
        Then("^I complete Euro emissions standard page section and click save and continue$", () -> {
            EmissionStandardsPageJourneySteps.hasPageHeading();
            EmissionStandardsPage.confirmCheckbox();
            BasePermitPage.saveAndContinue();
        });
        Then("^I complete Annual trips abroad page section and click save and continue$", () -> {
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
        });
        Then("^I complete Percentage of International journeys section and click save and continue$", () -> {
            ProportionOfInternationalJourneyPage.chooseDesiredProportion(JourneyProportion.LessThan60Percent);
            BasePermitPage.saveAndContinue();
        });
        Then("^I complete sectors page and click on save and continue$", () -> {
            SectorPage.sector(Sector.random());
        });
        Then("^I click confirm and continue on the Check your answers page$", () -> {
            ECMTShortTermJourney.getInstance().checkYourAnswersPage();
        });
        Then("^I click on Accept and continue on the Declaration page$", () -> {
            DeclarationPageJourneySteps.completeDeclaration();
        });
        Then("^I click on Submit and Pay button on the Permit fee page and complete the payment", () -> {
            PermitFeePage.submitAndPay();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .passwordAuthorisation();
            org.dvsa.testing.lib.newPages.permits.pages.SubmittedPage.untilOnPage();
        });
        Then("^I click on the Finish button on the Application submitted page", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.SubmittedPage.untilOnPage();
            org.dvsa.testing.lib.newPages.permits.pages.SubmittedPage.hasPageHeading();
            untilExpectedTextInElement("//a[contains(text(),'Go to permits dashboard')]",SelectorType.XPATH,"Go to permits dashboard",1000);
            BilateralJourneySteps.clickFinishButton();
        });;
        Then("^I am navigated back to the permits dashboard page with my application status shown as Under Consideration", () -> {
            String licence= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence);
            assertEquals(getElementValueByText("//span[@class='status orange']",SelectorType.XPATH),"UNDER CONSIDERATION");
        });
        Then("^I apply and pay for a short term APSG without sectors application", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithoutSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPage.selectEuroAndEnterPermitsValue();
            BasePermitPage.saveAndContinue();
            EmissionStandardsPage.confirmCheckbox();
            EmissionStandardsPage.saveAndContinue();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.chooseDesiredProportion(JourneyProportion.LessThan60Percent);
            ECMTShortTermJourney.getInstance().checkYourAnswersPage();
            DeclarationPageJourneySteps.completeDeclaration();
            PermitFeePage.submitAndPay();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            SubmittedPage.hasShortTermECMTAdvisoryText();
            BilateralJourneySteps.clickFinishButton();
        });

    }
}
