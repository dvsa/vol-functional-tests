
package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertTrue;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(World world, OperatorStore operatorStore) {
        When("^I am on the ECMT Removal number of permits page", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
            EcmtInternationalRemovalJourney.getInstance()
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.untilOnPage();
        });
        And("^the page heading on the ECMT removals number of permits page is displayed correctly$", NumberOfPermitsPageJourney::hasECMTPageHeading);
        And("^the advisory text on the ECMT removals number of permits page is displayed correctly$", () -> {
            assertTrue(NumberOfPermitsPage.isFeeTextPresent());
        });

        And("^the application reference on the ECMT removals number of permits page is displayed correctly$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        Given("^I have not entered anything in number of  permits field$", () -> {
            // Here for readability
        });
        And("^I enter number of permits more than the authorised vehicles and click save and continue$", () -> {
            NumberOfPermitsPage.exceedAuthorisedVehicle();
            BasePermitPage.saveAndContinue();
        });
        And("^I enter valid number of permits on the removals number of permits page and click save and continue$", () -> {
            NumberOfPermitsPage.enterAuthorisedVehicles();
            BasePermitPage.saveAndContinue();
        });
        And("^I enter valid number of permits on the removals number of permits page$", NumberOfPermitsPage::enterAuthorisedVehicles);
        Then("^the number of permits section on the ECMT Removals Overview page is complete$", () -> {
            OverviewPage.untilOnPage();
            NumberOfPermitsPage.untilOnPage();
            OverviewPageJourney.checkStatus(OverviewSection.NumberOfPermits, PermitStatus.COMPLETED);
        });
    }
}