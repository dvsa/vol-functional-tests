package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the short term number of permits page$", () -> {
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
        });
        Then("^the page heading on the short term number of permits page is displayed correctly$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.hasPageHeading();
           org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.hasAdvisoryText();
        });
        Then("^I should get the number of permits page error message$", () ->{
            org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.hasEnterNumberOfPermitsErrorText();
            org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.hasShortTermECMTEmissionErrorText();
        });

        Then("^I enter the valid number of permits required$", () -> {
            NumberOfPermitsPage.enterEuro5OrEuro6permitsValue();
        });
        Then("^I enter the number of permits required more than the authorised vehicles$",() ->{
            NumberOfPermitsPage.euro5OrEuro6Select();
            NumberOfPermitsPage.exceedAuthorisedVehicle();
        });
        Then("^I should get the validation error message$", org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage::hasShortTermECMTMaximumPermitsErrorText);
        Then("^the user is navigated to the overview page with the number of permits page status as completed$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(OverviewSection.NumberOfPermits, PermitStatus.COMPLETED);
        });
        Then("^I am taken back to short term number of permits page$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.hasPageHeading();
        });
        Then("^the user is navigated to the overview page with the number of permits page status as not started yet$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.hasPageHeading();
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(OverviewSection.NumberOfPermits, PermitStatus.NOT_STARTED_YET);
        });

    }
}
