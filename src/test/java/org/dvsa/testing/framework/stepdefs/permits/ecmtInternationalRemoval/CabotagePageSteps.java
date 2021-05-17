package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.CabotagePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.CancelApplicationPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.isPath;

public class CabotagePageSteps implements En {
    public CabotagePageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the ECMT International cabotage Page$", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.EcmtInternationalRemoval, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewPage.Section.RemovalsEligibility, operatorStore)
                    .removalsEligibility(true);
        });
        And ("^the ECMT International Removal application reference number should be displayed$", () -> {
            String actualReference = BasePermitPage.getReference();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        Then("^the ECMT international removal cabotage heading should be correct$", CabotagePage::cabotagePageText);
        Then ("^the correct text is displayed next to the checkbox in ECMT Removal cabotage page", CancelApplicationPage::ecmtInternationalRemovalcancelConfirmationText);
        When("^save and continue  button is selected without selecting the checkbox$", BasePermitPage::saveAndContinue);
        Then("^I should get the user defined error message$", CabotagePage::errorText);
        When ("^the cabotage checkbox is selected$", CabotagePage::cabotageConfirmation);
        Then("^I should be taken to certificates required page", () -> {
            Assert.assertTrue(isPath("/permits/application/\\d+/st-certificates/"));
        });

    }
}
