package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;

import static org.junit.jupiter.api.Assertions.*;


public class DeclarationPageSteps {

    @Then("I should see the validation error message for the declaration page")
    public void iShouldSeeTheValidationErrorMessage() {
        assertTrue(DeclarationPage.isErrorMessagePresent());
    }

    @When("I save and continue on the declaration page")
    public void iSaveAndContinueOnTheDeclarationPage() {
        DeclarationPage.saveAndContinue();
    }

    @And("I should see the declaration advisory texts")
    public void iShouldSeeTheDeclarationTexts() {
        DeclarationPage.isECMTAdvisoryTextPresent();
    }

    @When("I accept and continue")
    public void iAcceptAndContinue() {
        DeclarationPage.saveAndContinue();
    }

    @When("I should be on the ECMT permit fee page")
    public void iShouldBeOnTheECMTPermitFeePage() {
        assertEquals("Permit fee", PermitFeePage.getPageHeading());
    }

    @Then("the status for the declaration section in annual ECMT is complete")
    public void theStatusForTheDeclarationSectionInAnnualECMT() {
        OverviewPageJourney.checkStatus(OverviewSection.Declaration, PermitStatus.COMPLETED);
    }
}