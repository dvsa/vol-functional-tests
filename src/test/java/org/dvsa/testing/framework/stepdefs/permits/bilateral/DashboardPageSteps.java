package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

import java.util.concurrent.TimeUnit;

public class DashboardPageSteps implements En {

    public DashboardPageSteps(OperatorStore operatorStore, World world) {

        Then("^The Bilateral (Turkey|Ukraine) application status on the self service dashboard goes to VALID$", () -> {
            HomePage.PermitsTab.untilPermitHasStatus(
                    world.applicationDetails.getLicenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES);
        });
        Then("^I click on my licence on the permits dashboard page$", HomePage.PermitsTab::selectFirstValidPermit);

        Then("^I am navigated back to permits dashboard page$", HomePage.PermitsTab::untilOnPage);
    }
}