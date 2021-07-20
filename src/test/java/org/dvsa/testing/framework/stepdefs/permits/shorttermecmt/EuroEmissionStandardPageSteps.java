package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.external.pages.EmissionStandardsPage;

import static org.junit.Assert.assertEquals;

public class EuroEmissionStandardPageSteps implements En {

    public EuroEmissionStandardPageSteps (OperatorStore operatorStore, World world) {
        Then("I should get the emissions  page error message", () -> {
            String errorText = EmissionStandardsPage.getErrorText();
            assertEquals("Tick to confirm your vehicles will meet the minimum Euro emission standards that the permit allows.", errorText);
        });

        When("^I confirm the emissions standards checkbox", EmissionStandardsPage::confirmCheckbox);
    }
}