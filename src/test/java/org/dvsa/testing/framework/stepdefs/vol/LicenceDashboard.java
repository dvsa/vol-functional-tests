package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;

public class LicenceDashboard {
    private final World world;

    public LicenceDashboard(World world) {
        this.world = world;
    }

    @And("I sign in as the added user")
    public void signInAsTheAddedUser() {
        world.selfServeNavigation.navigateToLogin(
                world.DataGenerator.getOperatorUser(),
                world.DataGenerator.getOperatorUserEmail());
    }

    @Then("the current licence summaries should match and each licence should open from the dashboard")
    public void verifyLicenceDashboardSummariesAndSwitchBetweenLicences(DataTable expectedSummaries) {
        world.licenceDashboardJourney.verifyLicenceDashboardSummariesAndSwitchBetween(
                expectedSummaries.asMaps(String.class, String.class));
    }
}
