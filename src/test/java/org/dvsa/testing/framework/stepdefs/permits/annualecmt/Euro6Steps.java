package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.permits.pages.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.EmissionStandardsPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.junit.Assert;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.lib.pages.BasePage.getURL;
import static org.dvsa.testing.lib.pages.BasePage.untilUrlPathIs;
import static org.junit.Assert.assertEquals;

public class Euro6Steps implements En {

    public Euro6Steps(OperatorStore operatorStore, World world) {
        And("^I am on the euro emission standard page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPage.checkNeedECMTPermits();
            CheckIfYouNeedECMTPermitsPage.saveAndContinue();
            org.dvsa.testing.lib.newPages.permits.pages.CabotagePage.confirmWontUndertakeCabotage();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPage.selectEuroAndEnterPermitsValue();
            BasePermitPage.saveAndContinue();
        });

        When("^I select the back hyperlink$", BasePermitPage::back);

        Then("^should see the overview page without updating any changes$",() -> {
            untilUrlPathIs(OverviewPage.RESOURCE, TimeUnit.SECONDS, Duration.MEDIUM);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(OverviewSection.EuroEmissionStandards, PermitStatus.NOT_STARTED_YET);
        });

        Given("^I select the emission checkbox$", () -> {
            EmissionStandardsPage.confirmCheckbox();
        });
        When("^I select save and return overview link$", BasePermitPage::overview);

        Then("^I should see the overview page with updated changes$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(OverviewSection.EuroEmissionStandards, PermitStatus.COMPLETED);
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
            EmissionStandardsPage.confirmCheckbox();
        });

    }
    }