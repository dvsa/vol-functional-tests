package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.PermitStartDatePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.*;

public class PermitStartDatePageSteps extends BasePage {

    private final World world;

    public PermitStartDatePageSteps(World world) {
        this.world = world;
    }

    @And("I am on the ECMT removals permit start page")
    public void iAmOnTheECMTRemovalsPermitStartPage() {
        world.ecmtInternationalRemovalJourney.completeUntilPermitStartDatePage();
    }

    @And("the reference number is displayed correctly")
    public void theReferenceNumberIsDisplayedCorrectly() {
//        String actualReference = BasePermitPage.getReferenceFromPage();
//        assertEquals(world.basePermitJourney.getFullReferenceNumber(), actualReference);
    }

    @And("the page heading on permit start date page should be correct")
    public void thePageHeadingOnPermitStartDatePageShouldBeCorrect() {
        String heading = PermitStartDatePage.getPageHeading();
        assertEquals("When do you need your permits to be valid from?", heading);
    }

    @And("the advisory texts on permit start date page are displayed correctly")
    public void theAdvisoryTextsOnPermitStartDatePage() {
        assertTrue(PermitStartDatePage.checkAdvisoryTextPresent());
    }

    @Then("the error message is displayed in the permit start date page")
    public void theErrorMessageIsDisplayedInThePermitStartDatePage() {
        String errorText = PermitStartDatePage.getErrorText();
        assertEquals("Enter a valid permit start date and include a day, month and year", errorText);
    }

    @And("I am taken to the number of permits page")
    public void iAmTakenToTheNumberOfPermitsPage() {
        isPath("/permits/application/\\d+/number-of-permits/");
    }

    @When("I enter the valid date")
    public void iEnterTheValidDate() {
        PermitStartDatePage.permitDate();
    }

    @When("I dont enter all the fields")
    public void iDontEnterAllTheFields() {
        PermitStartDatePage.leaveDateBlank();
    }

    @When("I enter invalid date")
    public void iEnterInvalidDate() {
        PermitStartDatePage.inValidDate();
    }

    @And("I enter a date ahead of 60 days")
    public void iEnterADateAheadOf60Days() {
        PermitStartDatePage.dayAhead();
    }

    @And("I should get a valid error message")
    public void iShouldGetAValidErrorMessage() {
        assertTrue(PermitStartDatePage.checkDaysAheadErrorMessagePresent());
    }
}