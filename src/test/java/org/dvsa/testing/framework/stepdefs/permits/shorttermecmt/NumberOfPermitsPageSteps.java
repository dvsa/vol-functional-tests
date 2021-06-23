package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            licence.getEcmt().setPermitUsage(PermitUsage.random());
            PermitUsagePage.permitUsage(licence.getEcmt().getPermitusage());
            BasePermitPage.saveAndContinue();
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
        });
        Then("^the page heading on the short term number of permits page is displayed correctly$", () -> {
            NumberOfPermitsPageJourneySteps.hasPageHeading();
            assertTrue(NumberOfPermitsPage.isAdvisoryTextPresent());
        });
        Then("^I should get the number of permits page error message$", () ->{
            assertTrue(NumberOfPermitsPage.isEnterNumberOfPermitsErrorTextPresent());
            assertTrue(NumberOfPermitsPage.isShortTermECMTEmissionErrorTextPresent());
        });

        Then("^I enter the valid number of short term permits required$", NumberOfPermitsPage::enterEuro5OrEuro6permitsValue);
        Then("^I enter the number of permits required more than the authorised vehicles$",() ->{
            NumberOfPermitsPage.euro5OrEuro6Select();
            NumberOfPermitsPage.exceedAuthorisedVehicle();
        });
        Then("^I should get the validation error message$", () -> {
            assertEquals("You have exceeded the maximum you can apply for", NumberOfPermitsPage.getShortTermECMTMaximumPermitsErrorText());
        });
        Then("^the user is navigated to the overview page with the number of permits page status as completed$", () -> {
            OverviewPageJourneySteps.checkStatus(OverviewSection.NumberOfPermits, PermitStatus.COMPLETED);
        });
        Then("^I am taken back to short term number of permits page$", () -> {
            NumberOfPermitsPage.untilOnPage();
            NumberOfPermitsPageJourneySteps.hasPageHeading();
        });
        Then("^the user is navigated to the overview page with the number of permits page status as not started yet$", () -> {
            OverviewPageJourneySteps.hasPageHeading();
            OverviewPageJourneySteps.checkStatus(OverviewSection.NumberOfPermits, PermitStatus.NOT_STARTED_YET);
        });

    }
}
