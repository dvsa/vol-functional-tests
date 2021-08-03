package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

public class DashboardPageSteps implements En {

    public DashboardPageSteps(World world) {
        Then("^I click on my licence on the permits dashboard page$", HomePage.PermitsTab::selectFirstValidPermit);

        Then("^I am navigated back to permits dashboard page$", HomePage.PermitsTab::untilOnPage);
    }
}