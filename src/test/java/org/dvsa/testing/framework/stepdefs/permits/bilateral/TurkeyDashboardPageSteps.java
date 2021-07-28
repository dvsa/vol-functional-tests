package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;

import java.util.concurrent.TimeUnit;

public class TurkeyDashboardPageSteps implements En {

    public TurkeyDashboardPageSteps(OperatorStore operatorStore, World world) {

        Then("^The Bilateral Turkey application status on the self service dashboard goes to VALID$", () -> {
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