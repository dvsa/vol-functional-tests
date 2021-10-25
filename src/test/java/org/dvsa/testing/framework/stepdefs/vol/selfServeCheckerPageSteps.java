package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class selfServeCheckerPageSteps extends BasePage implements En {
    private final World world;

    public selfServeCheckerPageSteps(World world) {this.world=world;}

    @When("I am on the checker page")
    public void iAmOnTheCheckerPage() {
        world.selfServeNavigation.navigateToCheckerPage();
        waitForPageLoad();
    }

    @And("I click Continue on the checker page")
    public void iClickContinueOnTheCheckerPage() {
    }

    @Then("I should be on the dashboard")
    public void iShouldBeOnTheDashboard() {
    }
}
