package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class DeclarationPageSteps {
    public DeclarationPageSteps(World world) {

    }

    @When("I am taken to the bilateral declaration Page with correct information and content")
    public void iAmTakenToTheBilateralDeclarationPageWithCorrectInfo() {
        DeclarationPage.untilOnPage();
        // Checking declaration page content
        DeclarationPageJourney.hasPageHeading();
        assertTrue(DeclarationPage.isBilateralAdvisoryTextPresent());
        assertTrue(DeclarationPage.isWarningTextPresent());
    }

    @When("I click on Accept and continue on the Declaration page without selecting declaration checkbox")
    public void iClickOnAcceptAndContinueOnTheDeclaration() {
        DeclarationPage.saveAndContinue();
    }

    @Then("I should get the correct error message on the declaration page")
    public void iShouldGetTheCorrectErrorMessageOnTheDeclarationPage() {
        DeclarationPageJourney.hasErrorText();
    }
}