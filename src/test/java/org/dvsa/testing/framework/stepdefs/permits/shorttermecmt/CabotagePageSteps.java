package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

<<<<<<< HEAD
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
=======
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
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
