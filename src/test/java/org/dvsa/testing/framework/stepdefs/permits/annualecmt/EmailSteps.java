package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.PendingException;
import cucumber.api.java8.En;

public class EmailSteps implements En {
    public EmailSteps() {
        Then("^I should get an email notifying of the permits being issued$", () -> {
            throw new PendingException();
        });
    }
}
