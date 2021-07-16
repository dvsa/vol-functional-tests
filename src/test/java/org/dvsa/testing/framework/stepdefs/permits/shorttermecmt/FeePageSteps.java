package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EmissionStandardsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.PermitUsage;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyProportion;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.AnnualTripsAbroadPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.ProportionOfInternationalJourneyPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.enums.Sector;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;


public class FeePageSteps implements En {

    public FeePageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on shortterm ECMT fee page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore)
            .licencePage(operatorStore,world);
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
            DeclarationPageJourney.completeDeclaration();
        });
        Then("^the table contents on short term Fee page is  as per AC$", PermitFeePage::tableCheck);

    }

}
