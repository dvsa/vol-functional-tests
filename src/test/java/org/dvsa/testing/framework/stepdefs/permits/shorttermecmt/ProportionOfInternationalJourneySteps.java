package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.AnnualTripsAbroadPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.permits.pages.EmissionStandardsPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class ProportionOfInternationalJourneySteps implements En {

    public ProportionOfInternationalJourneySteps(OperatorStore operatorStore, World world) {
        And("^I am on short term proportion of international journey page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore)
                    .licencePage(operatorStore,world);
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            licence.getEcmt().setPermitUsage(PermitUsage.random());
            PermitUsagePage.permitUsage(licence.getEcmt().getPermitusage());
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
        });
        And("^the page heading on short term international journey page is displayed correctly$", ProportionOfInternationalJourneyPage::pageHeading);
        And("^the error message should be displayed$", ProportionOfInternationalJourneyPage::errorText);
        When("^I select proportion of International Journey and save and continue$", () -> {
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);
            licence.getEcmt().setInternationalBusiness(JourneyProportion.random());
            ProportionOfInternationalJourneyPage.proportion(licence.getEcmt().getInternationalBusiness());
        });
        When("^I specify  high percentage of international journey$", () -> {
           ProportionOfInternationalJourneyPage.proportion(JourneyProportion.MoreThan90Percent);
        });
        When("^the high intensity warning message is displayed$", ProportionOfInternationalJourneyPage::hasIntensityMessage);

    }
}