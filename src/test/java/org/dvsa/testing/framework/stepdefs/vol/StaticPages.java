package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;

public class StaticPages {
    private final World world;

    public StaticPages(World world) {
        this.world = world;
    }

    @When("I follow the footer link to {string}")
    public void followFooterLink(String path) {
        world.staticPagesJourney.followFooterLink(path);
    }

    @Then("the destination page contains {string}")
    public void verifyPageContains(String expectedText) {
        world.staticPagesJourney.verifyPageContains(expectedText);
    }
}
