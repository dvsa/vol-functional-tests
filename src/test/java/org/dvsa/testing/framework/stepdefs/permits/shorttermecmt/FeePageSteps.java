package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.SectorPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;


public class FeePageSteps implements En {

    public FeePageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on shortterm ECMT fee page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitTypePage.PermitType.ShortTermECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore)
            .licencePage(operatorStore,world);
            OverviewPage.select(OverviewPage.Section.HowwillyouusethePermits);
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
            org.dvsa.testing.lib.pages.external.permit.DeclarationPage.declare(true);
            org.dvsa.testing.lib.pages.external.permit.DeclarationPage.saveAndContinue();
        });
        Then("^the table contents on short term Fee page is  as per AC$", FeeOverviewPage::tableCheck);

    }

}
