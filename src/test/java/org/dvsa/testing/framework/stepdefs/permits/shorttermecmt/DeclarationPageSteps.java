package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.AnnualTripsAbroadPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.ProportionOfInternationalJourneyPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.SectorPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class DeclarationPageSteps implements En {

    public DeclarationPageSteps (OperatorStore operatorStore, World world) {

        And("^I am on the Short term Declaration page$", () -> {

            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
            org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.confirmCertificateRequired();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPage.enterEuro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
            EmissionStandardsPage.confirmCheckbox();
            BasePermitPage.saveAndContinue();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.chooseDesiredProportion(JourneyProportion.LessThan60Percent);
            SectorPage.sector(Sector.random());
            ECMTShortTermJourney.getInstance().checkYourAnswersPage();
        });
        Then("^I should see correct heading label on ECMT declaration page$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage.hasPageHeading();
        });
        Then("^the short term declarations page has reference number", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage.hasReference();
        });
        Then("^the short term declarations page has got the correct advisory text$", () -> {
            DeclarationPage.hasShortTermECMTAdvisoryText();
        });
        Then("^the short term declaration page has correct link under guidance notes$", org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage::hasGuidanceNotesLinkPresent);
        Then("^the short term declaration page checkbox has the correct text and displayed unselected by default$", org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage::hasCheckboxText);
        Then("^I should see the validation error message on the short term declaration page$", () -> {
            DeclarationPage.hasErrorMessagePresent();
        });

        And("^I click declaration link on the overview page again$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.Declaration);
        });

        Then("^I am directed back to the Declaration page$", org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage::untilOnPage);
        When("^I make my short term ECMT declaration$", org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage::confirmDeclaration);

        Then("^I am on the short term permits overview page with Declaration section marked as complete$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(OverviewSection.Declaration,PermitStatus.COMPLETED);
        });

        Then("^I am directed to the short term permit fee page$", () -> {
            PermitFeePage.untilOnPage();
        });
    }
}
