package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.RemovalsEligibilityPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.testng.Assert;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class ECMTInternationalRemovalEligibilityPageSteps {
    private final World world;

    public ECMTInternationalRemovalEligibilityPageSteps(World world) {
        this.world = world;
    }

    @When("I am on the ECMT International Removal Eligibity page")
    public void iAmOnTheECMTInternationalRemoval() {
        EcmtInternationalRemovalJourney.completeUntilRemovalEligibilityPage(world);
    }

    @And("the text is shown below the page heading")
    public void theTextIsShownBelowThePageHeading() {
        String advisoryText = RemovalsEligibilityPage.getAdvisoryText();
        assertEquals("You may only use ECMT international removal permits for laden or empty journeys carried out by specialist removal companies to move household goods and business possessions.", advisoryText);
    }

    @And("the text is shown next to the tick box")
    public void theTextIsShownNextToTickBox() {
        Assert.assertTrue(RemovalsEligibilityPage.isCheckboxAdvisoryTextPresent());
    }

    @And("I save and continue without selecting the checkbox")
    public void iSaveAndContinueWithout() {
        BasePermitPage.saveAndContinue();
    }

    @When("the checkbox is ticked")
    public void theCheckboxIsTicked() {
        RemovalsEligibilityPage.confirmCheckbox();
    }

    @Then("the error message is displayed on ECMT Remove Eligibility Page")
    public void theErrorMessageIsDisplayedOnECMTRemoveEligibility() {
        assertTrue(RemovalsEligibilityPage.isErrorMessagePresent());
    }

    @And("the Application Number is shown correctly on ECMT International Eligibility page")
    public void theApplicationNumberIsShownCorrectly() {
        String actualReference = BasePermitPage.getReferenceFromPage();
        assertEquals(BasePermitJourney.getFullReferenceNumber(), actualReference);
    }

    @And("the page heading is shown as per updated AC")
    public void thePageHeadingIsShownAsPerAC() {
        String heading = RemovalsEligibilityPage.getPageHeading();
        assertEquals("Removal permits can only be used for removal operations using specialised equipment and staff", heading);
    }

    @Then("I should be on the ECMT number of permits page")
    public void iShouldBeOnTheECMTPage() {
        NumberOfPermitsPageJourney.hasPageHeading();
    }
}