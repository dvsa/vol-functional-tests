package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.EmissionStandardsPage;

import static org.junit.jupiter.api.Assertions.*;


public class UkraineEmissionsStandardsSteps {
    @Then("I am on the Ukraine emissions standards page with correct information and content")
    public void iAmOnTheUkraineEmissionsStandardsPageWithCorrectInfo() {
        // Make sure the page has loaded before any further checks
        EmissionStandardsPage.untilOnPage();

        //checking the Country name displayed on the page is Ukraine
        assertEquals(EmissionStandardsPage.getCountry(), AnnualBilateralJourney.getCountry());

        //checking the Page heading on the Ukraine emissions standards page is correct
        String heading = EmissionStandardsPage.getPageHeading();
        assertEquals("Which vehicle euro emission standard will you use?", heading);

        //checking the  YES and NO radio buttons  with display of respective help texts on selection
        assertTrue(EmissionStandardsPage.isEuro3To6RadioButtonsChoices());
    }

    @When("I save and continue on the Ukraine emissions standards page")
    public void iSaveAndContinueOnUkraineEmissionsPage() {
        EmissionStandardsPage.saveAndContinue();
    }

    @When("I select Euro 3 or Euro 4 radio button on the Ukraine emissions standards page")
    public void iSelectEuro3OrEuro4() {
        EmissionStandardsPage.clickYes();
    }

    @When("I select Euro 5, Euro 6 or higher emission standard radio button on the Ukraine emissions standards page")
    public void iSelectEuro5OrEuro6() {
        EmissionStandardsPage.clickNo();
    }

    @When("I get advisory text that I don't need Ukraine permits")
    public void iGetAdvisoryTextThatIDoNotNeedUkrainePermits() {
        EmissionStandardsPage.ukraineSelectNoAdvisoryText();
    }
}