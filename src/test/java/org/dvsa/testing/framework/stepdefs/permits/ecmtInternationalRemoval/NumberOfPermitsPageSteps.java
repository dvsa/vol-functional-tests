
package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.CabotagePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.NumberofPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.Section.NumberofPermits;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(World world, OperatorStore operatorStore) {
        When("^I am on the ECMT Removal number of permits page", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.EcmtInternationalRemoval, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewPage.Section.RemovalsEligibility, operatorStore)
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage();
            BasePermitPage.saveAndContinue();
            NumberofPermitsPage.PermitsPageLoad();
        });
        And("^the page heading on the ECMT removals number of permits page is displayed correctly$", NumberofPermitsPage::hasheading);
        And("^the advisory text on the ECMT removals number of permits page is displayed correctly$", () -> {
            NumberofPermitsPage.hasAdvisoryText1();
            NumberofPermitsPage.hasAdvisoryText2();
        });

        And("^the application reference on the ECMT removals number of permits page is displayed correctly$", () -> {
            String actualReference = CabotagePage.reference();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);

        });
        Given("^I have not entered anything in number of  permits field$", () -> {
            // Here for readability
        });
        Then("^I should get the ECMT Removals number of permits page error message$", () -> {
            NumberofPermitsPage.hasErrorMessagePresent();
        });


        And("^I enter number of permits more than the authorised vehicles and click save and continue$", () -> {
            NumberofPermitsPage.authorisedVehicleExceed();
            BasePermitPage.saveAndContinue();
        });
        Then("^I should get the ECMT Removals number of permits page validation error message$", () -> {
            NumberofPermitsPage.hasErrorMessagePresent();

        });
        And("^I enter valid number of permits on the removals number of permits page and click save and continue$", () -> {
            NumberofPermitsPage.enterAuthorisedVehicles();
            BasePermitPage.saveAndContinue();
        });
        And("^I enter valid number of permits on the removals number of permits page$", () -> {
            NumberofPermitsPage.enterAuthorisedVehicles();
        });
        Then("^the number of permits section on the ECMT Removals Overview page is complete$", () -> {
            OverviewPage.OverviewPageLoad();
            NumberofPermitsPage.OverviewNumberOfPermitsPageLoad();
            boolean isComplete = org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.checkStatus(String.valueOf(NumberofPermits),PermitStatus.COMPLETED);
            Assert.assertTrue("The 'Number of Permits Required' section status is not complete", isComplete);
        });
    }
}