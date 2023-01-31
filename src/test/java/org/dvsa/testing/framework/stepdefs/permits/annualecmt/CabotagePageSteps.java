package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;

public class CabotagePageSteps extends BasePage {
    private final World world;

    public CabotagePageSteps(World world) {
        this.world = world;
    }

    @And("I am on the cabotage Page")
    public void iAmOnTheCabotagePage() {
        EcmtApplicationJourney.completeUntilCabotagePage(world);
    }

    @When("I declare not to undertake cabotage")
    public void iDeclareNotToUndertakeCabotage() {
        CabotagePage.confirmWontUndertakeCabotage();
    }
}