package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;

public class CabotagePageSteps extends BasePage implements En {

    public CabotagePageSteps(World world) {
        And("^I am on the cabotage Page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world);
            OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPageJourney.completePage();
        });
        When("^I declare not to undertake cabotage$", CabotagePage::confirmWontUndertakeCabotage);
    }

}
