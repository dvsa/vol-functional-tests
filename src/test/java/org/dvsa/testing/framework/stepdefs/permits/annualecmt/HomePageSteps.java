package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class HomePageSteps {
    public HomePageSteps(World world) {
    }

    @Then("I should be on the permits dashboard page")
    public void iShouldBeOnThePermitsDashboard() {
        assertEquals("Permits", HomePage.PermitsTab.getPageHeading());
    }

    @Then("I should be on the permits dashboard page with an ongoing application")
    public void iShouldBeOnThePermitsDashboardPage() {
        HomePage.PermitsTab.waitUntilOnGoingApplications();
    }

    @Then("The application status on the self service dashboard goes to UNDER CONSIDERATION")
    public void theApplicationStatusOnTheSelfService() {
        untilAnyPermitStatusMatch(PermitStatus.UNDER_CONSIDERATION);
    }
}

