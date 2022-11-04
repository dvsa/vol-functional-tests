package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CabotagePageSteps implements En {
    public CabotagePageSteps(World world) {
        Then("^I should get the cabotage page error message$", () -> {
            String errorText = CabotagePage.getErrorText();
            assertEquals("Tick to confirm your vehicle will not undertake cabotage journeys", errorText);
        });
        Then("^I should get the cabotage page error message for Annual ECMTs$", () -> {
            String errorText = CabotagePage.getErrorText();
            assertEquals("Tick to confirm your vehicle will not undertake cabotage journeys.", errorText);
        });
    }
}
