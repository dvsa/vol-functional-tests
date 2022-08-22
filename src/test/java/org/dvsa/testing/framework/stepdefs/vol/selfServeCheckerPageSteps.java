package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

public class selfServeCheckerPageSteps extends BasePage implements En {
    private final World world;

    public selfServeCheckerPageSteps(World world) {this.world=world;}

    @When("I am on the checker page")
    public void iAmOnTheCheckerPage() {
        world.selfServeNavigation.navigateToCheckerPage();
    }

    @And("I click Continue on the checker page")
    public void iClickContinueOnTheCheckerPage() {
        waitAndClick("Continue", SelectorType.LINKTEXT);
    }

    @Then("I should be on the dashboard")
    public void iShouldBeOnTheDashboard() {
        Assert.assertTrue(isPath("/dashboard"));
    }

    @Then("I should be on the Self Serve login page")
    public void iShouldBeOnTheSelfServeLoginPage() {
        waitForElementToBeClickable("auth.login.username", SelectorType.ID);
        Assert.assertTrue(isPath("auth/login/"));
    }
}
