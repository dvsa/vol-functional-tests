package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;

public class CabotagePageSteps extends BasePage implements En {

    public CabotagePageSteps(World world) {
        And("^I am on the cabotage Page$", () -> {
            EcmtApplicationJourney.completeUntilCabotagePage(world);
        });
        When("^I declare not to undertake cabotage$", CabotagePage::confirmWontUndertakeCabotage);
    }

}
