package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.BaseOverviewPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.*;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the short term number of permits page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitTypePage.PermitType.ShortTermECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore)
                    .licencePage(operatorStore,world);
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);
            OverviewPage.select(OverviewPage.Section.HowwillyouusethePermits);
            licence.getEcmt().setPermitUsage(PermitUsage.random());
            PermitUsagePage.permitUsage(licence.getEcmt().getPermitusage());
            world.put("permit.usage", licence.getEcmt().getPermitusage());
            BasePermitPage.saveAndContinue();
            CabotagePage.cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
        });
        Then("^the page heading on the short term number of permits page is displayed correctly$", () -> {
           NumberOfPermitsPage.pageHeading();
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
            boolean numberOfPermitsPage =  BaseOverviewPage.checkStatus(PermitSection.NumberOfPermitsRequired.toString(), PermitStatus.COMPLETED);
            Assert.assertTrue(numberOfPermitsPage);
        });
        Then("^I am taken back to short term number of permits page$", () -> {
            NumberOfPermitsPage.pageHeading();
        });
        Then("^the user is navigated to the overview page with the number of permits page status as not started yet$", () -> {
            OverviewPage.pageHeading();
            boolean numberOfPermitsPage =  BaseOverviewPage.checkStatus(PermitSection.NumberOfPermitsRequired.toString(), PermitStatus.NOT_STARTED_YET);
            Assert.assertTrue(numberOfPermitsPage);
        });

    }
}
