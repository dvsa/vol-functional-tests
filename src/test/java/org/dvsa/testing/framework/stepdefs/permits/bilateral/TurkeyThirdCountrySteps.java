package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.TurkeyThirdCountryPage;

import static org.junit.jupiter.api.Assertions.*;


public class TurkeyThirdCountrySteps {
    public TurkeyThirdCountrySteps(World world) {
    }

    @Then("I am on the Turkey third country page with correct information and content")
    public void iAmOnTheTurkeyPageWithCorrectInfo() {
        // Make sure the page has loaded before any further checks
        TurkeyThirdCountryPage.untilOnPage();

        //checking the Country name displayed on the page is Turkey
        assertEquals(TurkeyThirdCountryPage.getCountry(), AnnualBilateralJourney.getCountry());

        //checking the Page heading on the Turkey third country page is correct
        assertEquals("Are you transporting goods from Turkey to a third-country?", TurkeyThirdCountryPage.getPageHeading());

        //checking the  YES and NO radio buttons  with display of respective help texts on seletion
        assertTrue(TurkeyThirdCountryPage.areCorrectRadioButtonsPresent());
    }

    @When("I save and continue on the Turkey third country page")
    public void iSaveAndContinueOnTheTurkeyPage() {
        TurkeyThirdCountryPage.saveAndContinue();
    }

    @Then("I should get the validation error message to select one option")
    public void iShouldGetTheValidationErrorMessage() {
        assertEquals("Please select one option", TurkeyThirdCountryPage.getErrorText());
    }

    @When("I select Yes radio button on the Turkey third country page")
    public void iSelectYesRadioButtonOnTheTurkeyPage() {
        TurkeyThirdCountryPage.clickYesToRadioButton();
    }

    @When("I select No radio button on the Turkey third country page")
    public void iSelectNoRadioButtonOnTheTurkeyPage() {
        TurkeyThirdCountryPage.clickNoToRadioButton();
    }

    @When("I get advisory text that I don't need Turkey permits")
    public void iGetAdvisoryTextThatIDoNotNeedTurkeyPermits() {
        assertTrue(TurkeyThirdCountryPage.noSelectionAdvisoryText());
    }

    @When("I am navigated to the cancel application page")
    public void iAmNavigatedToTheCancelApplicationPage() {
        CancellationPage.untilOnPage();
        String heading = CancellationPage.getPageHeading();
        assertEquals("Cancel application", heading);
    }
}