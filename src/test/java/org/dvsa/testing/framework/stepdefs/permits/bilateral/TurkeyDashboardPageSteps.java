package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.Permits;

import java.util.concurrent.TimeUnit;

public class TurkeyDashboardPageSteps implements En {

    public TurkeyDashboardPageSteps(OperatorStore operatorStore, World world) {

        Then("^The Bilateral Turkey application status on the self service dashboard goes to VALID$", () -> {
            HomePage.PermitsTab.untilPermitHasStatus(
                    operatorStore.getCurrentLicence().get().getReferenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES);
        });
        Then("^I click on my licence on the permits dashboard page$", () -> {
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            HomePage.PermitsTab.select(licence.getLicNo());
        });

        Then("^I am navigated back to permits dashboard page$", Permits::untilOnPage);


    }
}