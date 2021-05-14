package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.permit.BaseOverviewPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.junit.Assert;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class EuroEmissionStandardPageSteps implements En {

    public EuroEmissionStandardPageSteps (OperatorStore operatorStore, World world) {

        Then("^I am on the Short term euro emission standard page$", () -> {

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
            BasePermitPage.saveAndContinue();
            CabotagePage.cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.euro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
        });
        Then("^the euro emissions  page has the relevant information$", () -> {
            EuroEmissioStandardsPage.untilElementIsPresent("//h1[@class='govuk-fieldset__heading']", SelectorType.XPATH,10, TimeUnit.SECONDS);
            EuroEmissioStandardsPage.hasPageHeading();
        });
        Then("^the short term emissions page has got the correct advisory text$", () -> {
            EuroEmissioStandardsPage.hasAdvisoryText();
        });
        Then("^the short term emissions page checkbox has the correct text and displayed unselected by default", () -> {
            EuroEmissioStandardsPage.hasCheckbox();
        });
        Then("I should get the emissions  page error message", EuroEmissioStandardsPage::hasErrorMessage);

        When("^I confirm the emissions standards checkbox", () -> {
            EuroEmissioStandardsPage.Emissionsconfirmation();

        });
        Then("^the user is navigated to the short term overview page with the status of emissions displayed as completed$", () -> {
            boolean emissions =  BaseOverviewPage.checkStatus(PermitSection.EuroEmissionsStandards.toString(),PermitStatus.COMPLETED);
            Assert.assertTrue(emissions);
        });

        Then("^I am navigated to annual trips abroad page", () -> {

           AnnualTripsAbroadPage.pageHeading();
        });
    }
}