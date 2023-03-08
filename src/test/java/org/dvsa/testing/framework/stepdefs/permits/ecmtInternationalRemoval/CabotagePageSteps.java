package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.*;

public class CabotagePageSteps extends BasePage {
    private final World world;

    public CabotagePageSteps(World world) {
        this.world = world;
    }

    @And("I am on the ECMT International cabotage Page")
    public void iAmOnTheECMTInternationLCabotagePage() {
        world.ecmtInternationalRemovalJourney.completeUntilCabotagePage();
    }
    @And("the ECMT International Removal application reference number should be displayed")
    public void theECMTInternationalRemovalApplicationReferenceNumber() {
        assertEquals(world.basePermitJourney.getFullReferenceNumber(), CabotagePage.getReferenceFromPage());
    }
    @Then("the ECMT international removal cabotage heading should be correct")
    public void theECMTInternationalRemovalCabotageHeading() {
        String heading = CabotagePage.getPageHeading();
        assertEquals("Removal permits do not allow you to carry out cabotage", heading);
    }
    @When("save and continue  button is selected without selecting the checkbox")
    public void saveAndContinueButtonIsSelectedWithoutSelectingTheCheckbox() {
        BasePermitPage.saveAndContinue();
    }
    @Then("I should be taken to certificates required page")
    public void iShouldBeTakenToCertificatesRequiredPage() {
        assertTrue(isPath("/permits/application/\\d+/st-certificates/"));
    }
}