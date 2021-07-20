package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.EmissionStandardsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Euro6Steps extends BasePage implements En {

    public Euro6Steps(OperatorStore operatorStore, World world) {
        And("^I am on the euro emission standard page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPageJourney.completePage();
            CabotagePage.confirmWontUndertakeCabotage();
            CabotagePage.saveAndContinue();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPageJourney.completeECMTPage();
        });

        When("^I click the back link", BasePermitPage::back);

        Then("^should see the overview page without updating any changes$",() -> {
            OverviewPage.untilOnPage();
            OverviewPageJourney.checkStatus(OverviewSection.EuroEmissionStandards, PermitStatus.NOT_STARTED_YET);
        });

        Then("^I should see the overview page with updated changes$", () -> {
            OverviewPageJourney.checkStatus(OverviewSection.EuroEmissionStandards, PermitStatus.COMPLETED);
        });

        Then("^I should see the validation errors for euro 6 page$", () -> {
            String errorText = EmissionStandardsPage.getErrorText();
            assertEquals("Tick to confirm your vehicles will meet the minimum Euro emission standards that the permit allows.", errorText);
        });
        Then("^the texts are displayed correctly$", () -> {
            assertTrue(EmissionStandardsPage.isAdvisoryTextPresent());
        });
    }
}