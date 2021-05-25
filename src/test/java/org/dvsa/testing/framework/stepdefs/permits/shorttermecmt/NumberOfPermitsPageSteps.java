package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.euro5OrEuro6Select;
import static org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.euro5OrEuro6permitsValue;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the short term number of permits page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore)
                    .licencePage(operatorStore,world);
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);

            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            licence.getEcmt().setPermitUsage(PermitUsage.random());
            PermitUsagePage.permitUsage(licence.getEcmt().getPermitusage());
            BasePermitPage.saveAndContinue();
            CabotagePage.cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
        });
        Then("^the page heading on the short term number of permits page is displayed correctly$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.hasPageHeading();
           NumberOfPermitsPage.guidanceText();
        });
        Then("^I should get the number of permits page error message$", () ->{
            NumberOfPermitsPage.errorText();
            NumberOfPermitsPage.errorTextEmission();
        });

        Then("^I enter the valid number of permits required$", () -> {
            euro5OrEuro6permitsValue();
                });
        Then("^I enter the number of permits required more than the authorised vehicles$",() ->{
                euro5OrEuro6Select();
                NumberOfPermitsPage.shortTTermAuthorisedVehicleExceed();
        });
        Then("^I should get the validation error message$", NumberOfPermitsPage::maxNumEntry);
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
