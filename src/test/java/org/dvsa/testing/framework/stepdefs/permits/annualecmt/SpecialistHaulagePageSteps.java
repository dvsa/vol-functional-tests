package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.CabotagePage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CountriesWithLimitedPermitsPage;
import org.junit.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class SpecialistHaulagePageSteps implements En {

    public SpecialistHaulagePageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the specialist haulage page$", () -> {
            clickToPermitTypePage(world);
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operatorStore);
            YearSelectionPage.EcmtValidityPeriod();
            //EcmtApplicationJourney.getInstance().yearSelection(YearSelectionPage.YearSelection.YEAR_2020,operatorStore);
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPage.checkNeedECMTPermits();
            CheckIfYouNeedECMTPermitsPage.saveAndContinue();
            //    AnnualEcmtPermitUsagePage.annualEcmtPermitUsage(AnnualEcmtPermitUsage.random());
            BasePermitPage.saveAndContinue();
            //OverviewPage.section(PermitSection.Cabotage);
            CabotagePage.wontCarryCabotage(true);
            CertificatesRequiredPage.confirmCertificateRequired();
            CertificatesRequiredPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            NumberOfPermitsPage.selectEuroAndEnterPermitsValue();
            BasePermitPage.saveAndContinue();
            EmissionStandardsPage.confirmCheckbox();
            NumberOfTripsPage.enterNumberOfTripsValue();
            BasePermitPage.saveAndContinue();
            PercentageOfInternationalJourneysPage.proportion(JourneyProportion.LessThan60Percent);
        });
        Given("^I have specified a type of good to deliver$", () -> SectorPage.sector(Sector.random()));
        Then("^Non other sectors should be in alphabetical order$", () -> {
            List<String> sectorTitles = SectorPage.getSectorsOnPage().stream()
                    .filter((String sector) -> !sector.equals("Other non-metallic mineral products")
                            && !sector.equals("None or more than one of these sectors"))
                    .collect(Collectors.toList());

            for (int i = 0; i < sectorTitles.size() - 1; i++) {
                Assert.assertTrue(sectorTitles.get(i).substring(0, 1).compareTo(sectorTitles.get(i + 1).substring(0, 1)) <= 0);
            }
        });
    }

}
