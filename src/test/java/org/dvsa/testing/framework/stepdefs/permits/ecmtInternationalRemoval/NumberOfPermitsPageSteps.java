
package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(World world, OperatorStore operatorStore) {
        When("^I am on the ECMT Removal number of permits page", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewSection.RemovalsEligibility, operatorStore)
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.untilOnPage();
        });
        And("^the page heading on the ECMT removals number of permits page is displayed correctly$", NumberOfPermitsPage::hasECMTPageHeading);
        And("^the advisory text on the ECMT removals number of permits page is displayed correctly$", NumberOfPermitsPage::hasFeeText);

        And("^the application reference on the ECMT removals number of permits page is displayed correctly$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);

        });
        Given("^I have not entered anything in number of  permits field$", () -> {
            // Here for readability
        });
        Then("^I should get the ECMT Removals number of permits page error message$", () -> {
            NumberOfPermitsPage.hasEnterNumberOfPermitsErrorText();
        });


        And("^I enter number of permits more than the authorised vehicles and click save and continue$", () -> {
            NumberOfPermitsPage.exceedAuthorisedVehicle();
            BasePermitPage.saveAndContinue();
        });
        Then("^I should get the ECMT Removals number of permits page validation error message$", () -> {
            NumberOfPermitsPage.hasEnterNumberOfPermitsErrorText();

        });
        And("^I enter valid number of permits on the removals number of permits page and click save and continue$", () -> {
            NumberOfPermitsPage.enterAuthorisedVehicles();
            BasePermitPage.saveAndContinue();
        });
        And("^I enter valid number of permits on the removals number of permits page$", () -> {
            NumberOfPermitsPage.enterAuthorisedVehicles();
        });
        Then("^the number of permits section on the ECMT Removals Overview page is complete$", () -> {
            OverviewPage.untilOnPage();
            NumberOfPermitsPage.untilOnPage();
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(OverviewSection.NumberOfPermits, PermitStatus.COMPLETED);
        });
    }
}