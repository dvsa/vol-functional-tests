package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.EuroEmissioStandardsPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class PercentageOfInternationalBusinessSteps implements En {

    public PercentageOfInternationalBusinessSteps(World world, OperatorStore operatorStore) {
        When("^I am on the percentage of international journeys page$", () -> {
            clickToPermitTypePage(world);
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.EcmtAnnual, operatorStore);
            YearSelectionPage.EcmtValidityPeriod();
            //EcmtApplicationJourney.getInstance().yearSelection(YearSelectionPage.YearSelection.YEAR_2020,operatorStore);
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            OverviewPage.section(PermitSection.CheckIfYouNeedECMTPermits);
            CheckIfYouNeedECMTPermitsPage.needECMTPermits(true);
         //   AnnualEcmtPermitUsagePage.annualEcmtPermitUsage(AnnualEcmtPermitUsage.random());
            BasePermitPage.saveAndContinue();
            //OverviewPage.section(PermitSection.Cabotage);
            CabotagePage.wontCarryCabotage(true);
            CertificatesRequiredPage.certificatesRequired(true);
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            NumberOfPermitsPage.permitsValue();
            BasePermitPage.saveAndContinue();
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
            NumberOfTripsPage.numberOfTripsValue();
            BasePermitPage.saveAndContinue();
        });
        When("^I confirm percentage international$", () -> {
            PercentageOfInternationalJourneysPage.proportionconfirmation(JourneyProportion.random());
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
        Then("^I am informed that I may be asked to verify my answers on the international journeys page$", () -> {
            Assert.assertTrue("Unable to find intensity of use message on current page", PercentageOfInternationalJourneysPage.hasIntensityMessage());
        });
        Then("^I should see the validation error message for the percentage of international business page$", () -> Assert.assertTrue(PercentageOfInternationalJourneysPage.hasErrorMessagePresent()));
        And("^I save and return to overview from percentage of international business page$", () -> {
            PercentageOfInternationalJourneysPage.overview();
            if (JourneyProportion.random()==JourneyProportion.MoreThan90Percent)
            {
             PercentageOfInternationalJourneysPage.overview();
            }
        });
    }

}
