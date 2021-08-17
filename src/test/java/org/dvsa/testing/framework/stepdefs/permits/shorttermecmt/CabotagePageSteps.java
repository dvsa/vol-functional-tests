package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;
import org.junit.Assert;

public class CabotagePageSteps implements En {
    public CabotagePageSteps(World world) {
        Then("^I should get the cabotage page error message$", () -> {
            String errorText = CabotagePage.getErrorText();
            Assert.assertEquals("Tick to confirm your vehicle will not undertake cabotage journeys", errorText);
        });
        Then("^I should get the cabotage page error message for Annual ECMTs$", () -> {
            String errorText = CabotagePage.getErrorText();
            Assert.assertEquals("Tick to confirm your vehicle will not undertake cabotage journeys.", errorText);
        });
    }
}
