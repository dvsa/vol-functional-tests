package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.BasePage.getURL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Euro6Steps implements En {

    public Euro6Steps(OperatorStore operatorStore, World world) {
        And("^I am on the euro emission standard page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPageJourneySteps.completePage();
            CabotagePage.confirmWontUndertakeCabotage();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPageJourneySteps.completeECMTPage();
        });

        When("^I click the back link", BasePermitPage::back);

        Then("^should see the overview page without updating any changes$",() -> {
            OverviewPage.untilOnPage();
            OverviewPageJourneySteps.checkStatus(OverviewSection.EuroEmissionStandards, PermitStatus.NOT_STARTED_YET);
        });

        When("^I select save and return overview link$", BasePermitPage::overview);

        Then("^I should see the overview page with updated changes$", () -> {
            OverviewPageJourneySteps.checkStatus(OverviewSection.EuroEmissionStandards, PermitStatus.COMPLETED);
        });

        Then("^I should be able to navigate to the next page$", () -> {
            Assert.assertNotEquals(CommonSteps.origin.get("origin"), getURL().toString());
        });

        Then("^I should see the validation errors for euro 6 page$", () -> {
            String errorText = EmissionStandardsPage.getErrorMessage();
            assertEquals("Tick to confirm your vehicles will meet the minimum Euro emission standards that the permit allows.", errorText);
        });
        Then("^I see the application reference number is displayed correctly$", () -> {
            String expectedLicenceNumber= operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber= BasePermitPage.getReferenceFromPage();
            Assert.assertTrue(actualReferenceNumber.contains(expectedLicenceNumber));
        });
        Then("^the texts are displayed correctly$", () -> {
            assertTrue(EmissionStandardsPage.isAdvisoryTextPresent());
        });
    }
}