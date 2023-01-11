package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CabotagePageSteps {
    @Then("I should get the cabotage page error message")
    public void iShouldGetTheCabotagePageErrorMessage() {
        String errorText = CabotagePage.getErrorText();
        assertEquals("Tick to confirm your vehicle will not undertake cabotage journeys", errorText);
    }
    @Then("I should get the cabotage page error message for Annual ECMTs")
    public void iShouldGetTheCabotagePage() {
        String errorText = CabotagePage.getErrorText();
        assertEquals("Tick to confirm your vehicle will not undertake cabotage journeys.", errorText);
    }
}