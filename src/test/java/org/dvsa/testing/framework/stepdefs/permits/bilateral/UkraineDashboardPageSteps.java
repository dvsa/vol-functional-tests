package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

import java.util.concurrent.TimeUnit;

public class UkraineDashboardPageSteps implements En {

    public UkraineDashboardPageSteps(OperatorStore operatorStore, World world) {

        Then("^The Bilateral Ukraine application status on the self service dashboard goes to VALID$", () -> {
            HomePage.PermitsTab.untilPermitHasStatus(
                    operatorStore.getCurrentLicence().get().getReferenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES);
        });
    }
}