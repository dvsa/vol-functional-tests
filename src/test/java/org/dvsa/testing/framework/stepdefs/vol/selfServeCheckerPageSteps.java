package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class selfServeCheckerPageSteps extends BasePage{
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
        assertTrue(isPath("/dashboard"));
    }

    @Then("I should be on the Self Serve login page")
    public void iShouldBeOnTheSelfServeLoginPage() {
        waitForElementToBeClickable("auth.login.username", SelectorType.ID);
        assertTrue(isPath("auth/login/"));
    }
}
