package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.isPath;

public class CabotagePageSteps implements En {
    public CabotagePageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the ECMT International cabotage Page$", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewSection.RemovalsEligibility)
                    .removalsEligibility(true);
        });
        And ("^the ECMT International Removal application reference number should be displayed$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        Then("^the ECMT international removal cabotage heading should be correct$", CabotagePage::hasShortTermECMTPageHeading);
        Then ("^the correct text is displayed next to the checkbox in ECMT Removal cabotage page", () -> {
            //TODO: Previous code didn't make any sense being here.
        });
        When("^save and continue  button is selected without selecting the checkbox$", BasePermitPage::saveAndContinue);
        Then("^I should get the user defined error message$", CabotagePage::hasErrorText);
        When ("^the cabotage checkbox is selected$", CabotagePage::confirmWontUndertakeCabotage);
        Then("^I should be taken to certificates required page", () -> {
            Assert.assertTrue(isPath("/permits/application/\\d+/st-certificates/"));
        });

    }
}
