package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;
import io.cucumber.java8.PendingException;

public class EmailSteps implements En {
    public EmailSteps() {
        Then("^I should get an email notifying of the permits being issued$", () -> {
            throw new PendingException();
        });
    }
}
