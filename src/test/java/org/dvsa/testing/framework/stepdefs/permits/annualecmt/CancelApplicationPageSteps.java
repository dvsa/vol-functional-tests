package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CancelApplicationPageSteps extends BasePermitPage {
    private final World world;

    public CancelApplicationPageSteps(World world) {
        this.world = world;
    }

    @Given("I am on the cancel application page")
    public void iAmOnTheCancelApplicationPage() {
        world.ecmtApplicationJourney.beginApplication();
        OverviewPage.clickCancelApplication();
    }

    @Then("I should get an error message on cancel application page")
    public void iShouldGetAnErrorMessage() {
        assertEquals("Tick to confirm you want to withdraw your application", CancellationPage.getErrorText());
    }

    @Then("I should get an error message on Annual ECMT cancel application page")
    public void iShouldGetAnErrorMessageOnAnnualECMT() {
        assertEquals("You must select the checkbox to continue", CancellationPage.getErrorText());
    }

    @When("I cancel my ECMT application")
    public void iCancelMyECMTApplication() {
        CancellationPage.saveAndContinue();
    }
}
