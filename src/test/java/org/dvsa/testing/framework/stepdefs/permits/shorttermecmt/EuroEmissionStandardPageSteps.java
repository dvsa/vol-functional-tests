package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.external.pages.EmissionStandardsPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EuroEmissionStandardPageSteps {

    @Then("I should get the emissions  page error message")
    public void iShouldGetTheEmissionsPageError() {
        String errorText = EmissionStandardsPage.getErrorText();
        assertEquals("Tick to confirm your vehicles will meet the minimum Euro emission standards that the permit allows.", errorText);
    }
    @When("I confirm the emissions standards checkbox")
    public void iConfirmTheEmissionsStandardsCheckbox() {
        EmissionStandardsPage.confirmCheckbox();
    }
}