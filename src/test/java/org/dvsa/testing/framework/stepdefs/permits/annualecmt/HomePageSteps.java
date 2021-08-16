package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class HomePageSteps implements En {
    public HomePageSteps() {
        Then("^I should be on the permits dashboard page$", () -> {
            assertEquals("Permits", HomePage.PermitsTab.getPageHeading());
        });

        Then("^I should be on the permits dashboard page with an ongoing application$", HomePage.PermitsTab::waitUntilOnGoingApplications);
        Then("^The application status on the self service dashboard goes to UNDER CONSIDERATION", () -> {
            untilAnyPermitStatusMatch(PermitStatus.UNDER_CONSIDERATION);
        });
    }
}



