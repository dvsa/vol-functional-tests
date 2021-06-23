package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EmissionStandardsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;
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
            YearSelectionPage.selectECMTValidityPeriod();
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPageJourneySteps.completePage();
            BasePermitPage.saveAndContinue();
            CabotagePage.confirmWontUndertakeCabotage();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPageJourneySteps.completeECMTPage();
            EmissionStandardsPageJourneySteps.completePage();
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
