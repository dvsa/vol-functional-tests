
package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.*;

public class NumberOfPermitsPageSteps {
    private final World world;

    public NumberOfPermitsPageSteps(World world) {
        this.world = world;
    }

    @When("I am on the ECMT Removal number of permits page")
    public void iAmOnTheECMTRemovalNumberOfPermitsPage() {
        world.ecmtInternationalRemovalJourney.completeUntilNumberOfPermitsPage();
    }
    @And("the page heading on the ECMT removals number of permits page is displayed correctly")
    public void thePageHeadingOnTheECMTRemovalsNumberOfPermitsPage() {
        NumberOfPermitsPageJourney.hasECMTPageHeading();
    }

    @And("the advisory text on the ECMT removals number of permits page is displayed correctly")
    public void theAdvisoryTextOnTheECMTRemovalsNumberOfPermitsPageIsDisplayedCorrectly() {
        assertTrue(NumberOfPermitsPage.isFeeTextPresent());
    }

    @And("the application reference on the ECMT removals number of permits page is displayed correctly")
    public void theApplicationReferenceOnTheECMTRemovalsNumberOfPermitsPageIsDisplayedCorrectly() {
        String actualReference = BasePermitPage.getReferenceFromPage();
        assertEquals(world.basePermitJourney.getFullReferenceNumber(), actualReference);
    }

    @And("I enter number of permits more than the authorised vehicles and click save and continue")
    public void iEnterNumberOfPermitsMoreThanTheAuthorisedVehiclesAndClickSaveAndContinue() {
        NumberOfPermitsPage.exceedAuthorisedVehicle();
        BasePermitPage.saveAndContinue();
    }

    @And("I enter valid number of permits on the removals number of permits page and click save and continue")
    public void iEnterValidNumberOfPermitsOnTheRemovalsNumberOfPermitsPageAndClickSaveAndContinue() {
        NumberOfPermitsPage.enterAuthorisedVehicles();
        BasePermitPage.saveAndContinue();
    }

    @And("I enter valid number of permits on the removals number of permits page")
    public void iEnterValidNumberOfPermitsOnTheRemovalsNumberOfPermitsPage() {
        NumberOfPermitsPage.enterAuthorisedVehicles();
    }

    @Then("the number of permits section on the ECMT Removals Overview page is complete")
    public void theNumberOfPermitsSectionOnTheECMTRemovals() {
        OverviewPage.untilOnPage();
        OverviewPageJourney.checkStatus(OverviewSection.NumberOfPermits, PermitStatus.COMPLETED);
    }
}