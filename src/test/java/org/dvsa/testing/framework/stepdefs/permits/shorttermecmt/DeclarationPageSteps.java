package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EmissionStandardsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.external.enums.JourneyProportion;
import org.dvsa.testing.lib.newPages.external.enums.Sector;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.AnnualTripsAbroadPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.ProportionOfInternationalJourneyPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertTrue;

public class DeclarationPageSteps implements En {

    public DeclarationPageSteps (OperatorStore operatorStore, World world) {

        And("^I am on the Short term Declaration page$", () -> {

            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            OverviewPageJourney.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPageJourney.completeECMTPage();
            EmissionStandardsPageJourney.completePage();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.chooseDesiredProportion(JourneyProportion.LessThan60Percent);
            SectorPage.selectSectionAndContinue(Sector.random());
            ECMTShortTermJourney.getInstance().checkYourAnswersPage();
        });
        Then("^I should see the correct heading on the declaration page$", DeclarationPageJourney::hasPageHeading);
        Then("^the declaration page has a reference number", DeclarationPage::hasReference);
        Then("^the short term declarations page has got the correct advisory text$", () -> {
            assertTrue(DeclarationPage.isShortTermECMTAdvisoryTextPresent());
        });
        Then("^I should see the validation error message on the short term declaration page$", DeclarationPage::isErrorMessagePresent);

        And("^I click declaration link on the overview page again$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.Declaration);
        });

        Then("^I am on the short term permits overview page with Declaration section marked as complete$", () -> {
            OverviewPage.untilOnPage();
            OverviewPageJourney.checkStatus(OverviewSection.Declaration,PermitStatus.COMPLETED);
        });

        Then("^I am directed to the short term permit fee page$", PermitFeePage::untilOnPage);
    }
}
