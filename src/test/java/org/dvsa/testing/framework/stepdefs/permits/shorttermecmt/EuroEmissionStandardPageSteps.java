package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

<<<<<<< HEAD
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
=======
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.external.pages.EmissionStandardsPage;

import static org.junit.Assert.assertEquals;

public class EuroEmissionStandardPageSteps implements En {

    public EuroEmissionStandardPageSteps (World world) {
        Then("I should get the emissions  page error message", () -> {
            String errorText = EmissionStandardsPage.getErrorText();
            assertEquals("Tick to confirm your vehicles will meet the minimum Euro emission standards that the permit allows.", errorText);
        });

        When("^I confirm the emissions standards checkbox", EmissionStandardsPage::confirmCheckbox);
    }
}