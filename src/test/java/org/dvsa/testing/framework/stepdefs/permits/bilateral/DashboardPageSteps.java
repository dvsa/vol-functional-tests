package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

public class DashboardPageSteps {
    private final World world;

    public DashboardPageSteps(World world) {
        this.world = world;
    }

    @Then("I click on my licence on the permits dashboard page")
    public void iClickOnMyLicenceOnThePermitsDashboardPage() {
        HomePage.PermitsTab.selectFirstValidPermit();
    }

    @Then("I am navigated back to permits dashboard page")
    public void iAmNavigatedBackToPermitsDashboardPage() {
        HomePage.PermitsTab.untilOnPage();
    }
}