package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.Permits;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;


public class HomePageSteps implements En {
    public HomePageSteps() {
        Then("^I should be on the permits dashboard page$",
                () ->
                        Permits.Permit.hasPageHeading());

        Then("^I should be on the permits dashboard page with an ongoing application$",
                () ->
                        Permits.Permit.onGoingApplication());
        Then("^The application status on the self service dashboard goes to VALID", () -> {
            untilAnyPermitStatusMatch(PermitStatus.VALID);
        });
        Then("^The application status on the self service dashboard goes to UNDER CONSIDERATION", () -> {
            untilAnyPermitStatusMatch(PermitStatus.UNDER_CONSIDERATION);
        });
        Then("^I should be on the permits dashboard page with my application under Issued permits and certificates table$",
                () ->
                        Permits.Permit.hasIssuedPermitsAndCertificates());
    }
}



