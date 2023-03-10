package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitTypePage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermitTypePageSteps {
    private final World world;

    public PermitTypePageSteps(World world) {
        this.world = world;
    }

    @And("I am on the permit type page")
    public void iAmOnThePermitTypePage() {
        world.basePermitJourney.clickToPermitTypePage();
    }

    @Then("continue button is selected without confirming the permit type")
    public void theContinueButtonIsSelectedWithoutConfirmingThePermitType() {
        PermitTypePage.clickContinue();
    }

    @Then("the error message is displayed in the permit type page")
    public void theErrorMessageIsDisplayedInThePermitTypePage() {
        String errorText = PermitTypePage.getErrorText();
        assertEquals("You must select an option to continue", errorText);
    }

    @Then("I click cancel button")
    public void iClickCancelButton() {
        PermitTypePage.clickCancel();
    }
}