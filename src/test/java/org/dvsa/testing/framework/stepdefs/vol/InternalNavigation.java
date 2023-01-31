package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class InternalNavigation extends BasePage {

    public World world;

    public InternalNavigation(World world) {
        this.world = world;
    }

    @When("i navigate to the {string} safety and compliance page on internal")
    public void iNavigateToTheSafetyAndCompliancePageOnInternal(String type) {
        world.internalNavigation.navigateToPage(type, SelfServeSection.SAFETY_AND_COMPLIANCE);
    }

    @When("i click submit")
    public void iClickSubmit() {
        world.UIJourney.clickSubmit();
    }

    @And("i have logged in to internal")
    public void iHaveLoggedIntoInternal() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
    }

    @When("i am on the internal application overview page")
    public void iAmOnTheApplicationOverviewPage() {
        world.internalNavigation.navigateToPage("application", SelfServeSection.VIEW);
    }

    @When("i am on the internal variation overview page")
    public void iAmOnTheVariationOverviewPage() {
        world.internalNavigation.navigateToPage("variation", SelfServeSection.VIEW);
    }

    @When("I navigate to the undertakings page on internal")
    public void iNavigateToTheUndertakingsPageOnInternal() {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.CONDITIONS_AND_UNDERTAKINGS);
    }
}
