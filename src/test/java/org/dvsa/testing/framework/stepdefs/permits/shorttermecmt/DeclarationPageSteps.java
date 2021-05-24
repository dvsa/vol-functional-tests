package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.pages.external.permit.BaseDeclarationPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.SectorPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class DeclarationPageSteps implements En {

    public DeclarationPageSteps (OperatorStore operatorStore, World world) {

        And("^I am on the Short term Declaration page$", () -> {

            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            OverviewPage.select(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.euro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.proportion(JourneyProportion.LessThan60Percent);
            SectorPage.sector(Sector.random());
            ECMTShortTermJourney.getInstance().checkYourAnswersPage();
        });
        Then("^I should see correct heading label on ECMT declaration page$", () -> {
            DeclarationPage.hasPageHeading();
        });
        Then("^the short term declarations page has reference number", () -> {
            BaseDeclarationPage.hasReference();
        });
        Then("^the short term declarations page has got the correct advisory text$", () -> {
            DeclarationPage.hasAdvisoryMessages();
        });
        Then("^the short term declaration page has correct link under guidance notes$", DeclarationPage::hasGuidanceNotesLinkPresent);
        Then("^the short term declaration page checkbox has the correct text and displayed unselected by default$", DeclarationPage::hasCheckbox);
        Then("^I should see the validation error message on the short term declaration page$", () -> {
            DeclarationPage.hasErrorMessagePresent();
        });

        And("^I click declaration link on the overview page again$", () -> {
            OverviewPage.select(OverviewSection.Declaration);

        });

        Then("^I am directed back to the Declaration page$", DeclarationPage::declarationPageLoad);
        When("^I make my short term ECMT declaration$", DeclarationPage::DeclarationConfirmation);

        Then("^I am on the short term permits overview page with Declaration section marked as complete$", () -> {
            String error = "Expected the status of Declarations  page to be complete but it wasn't";
            OverviewPage.untilOnPage();
            boolean complete = OverviewPage.checkStatus(OverviewSection.Declaration,PermitStatus.COMPLETED);
            Assert.assertTrue(error, complete);
        });

        Then("^I am directed to the short term permit fee page$", () -> {
            DeclarationPage.untilOnFeePage();
        });
    }
}
