package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class PercentageOfInternationalBusinessSteps implements En {

    public PercentageOfInternationalBusinessSteps(World world, OperatorStore operatorStore) {
        When("^I am on the percentage of international journeys page$", () -> {
            clickToPermitTypePage(world);
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operatorStore);
            YearSelectionPage.selectECMTValidityPeriod();
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPage.checkNeedECMTPermits();
            CheckIfYouNeedECMTPermitsPage.saveAndContinue();
            BasePermitPage.saveAndContinue();
            CabotagePage.confirmWontUndertakeCabotage();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPage.selectEuroAndEnterPermitsValue();
            BasePermitPage.saveAndContinue();
            EmissionStandardsPage.confirmCheckbox();
            EmissionStandardsPage.saveAndContinue();
            NumberOfTripsPage.enterNumberOfTripsValue();
            BasePermitPage.saveAndContinue();
        });
        When("^I select my percentage of business that's international$", () -> {
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);
            licence.getEcmt().setInternationalBusiness(JourneyProportion.random());

            PercentageOfInternationalJourneysPage.proportion(licence.getEcmt().getInternationalBusiness());
        });
        When("^I specify a high percentage$", () -> {
            PercentageOfInternationalJourneysPage.proportion(JourneyProportion.MoreThan90Percent);
        });
        Then("^I should get the appropriate warning message$", () -> {
           PercentageOfInternationalJourneysPage.intensityMessage();
        });
    }

}
