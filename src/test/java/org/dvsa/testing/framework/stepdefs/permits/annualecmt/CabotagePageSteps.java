package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;

import static org.dvsa.testing.lib.pages.BasePage.getURL;

public class CabotagePageSteps implements En {

    public CabotagePageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the cabotage Page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPageJourneySteps.completePage();

        });
        When("^I declare not to undertake cabotage$", () -> {
            CabotagePage.confirmWontUndertakeCabotage();
        });
        Given("^I have not declared not to undertake cabotage$", () -> {
            CommonSteps.origin.put("origin", getURL());
        });
    }

}
