package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class InternalNavigation extends BasePage {

    public World world;

    public InternalNavigation(World world) {
        this.world = world;
    }

    @When("i navigate to the {string} safety and compliance page on internal")
    public void iNavigateToTheSafetyAndCompliancePageOnInternal(String type) {
        if (type.equals("application"))
            world.internalNavigation.getApplication();
        else if (type.equals("licence"))
            world.internalNavigation.getLicence();
        else
            world.internalNavigation.getVariationApplication();
        clickByLinkText("Safety and compliance");
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
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getApplication();
    }

    @When("i am on the internal variation overview page")
    public void iAmOnTheVariationOverviewPage() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getVariationApplication();
    }
}
