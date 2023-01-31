package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.EmissionStandardsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.*;

public class Euro6Steps extends BasePage {

    private final World world;

    public Euro6Steps(World world) {
        this.world = world;
    }

    @And("I am on the euro emission standard page")
    public void iAMOnTheEuroEmissionStandardPage() {
        EcmtApplicationJourney.completeUntilNumberOfPermitsPage(world);
        NumberOfPermitsPageJourney.completeECMTPage();
    }

    @When("I click the back link")
    public void iClickTheBackLink() {
        BasePermitPage.back();
    }
    @Then("should see the overview page without updating any changes")
    public void shouldSeeTheOverviewPageWithoutUpdatingAnyChanges() {
        OverviewPage.untilOnPage();
        OverviewPageJourney.checkStatus(OverviewSection.EuroEmissionStandards, PermitStatus.NOT_STARTED_YET);
    }
    @Then("I should see the overview page with updated changes")
    public void iShouldSeeTheOverviewPageWithUpdatedChanges() {
        OverviewPageJourney.checkStatus(OverviewSection.EuroEmissionStandards, PermitStatus.COMPLETED);
    }

    @Then("I should see the validation errors for euro 6 page")
    public void iShouldSeeTheValidationErrorsForEuro6Page() {
        String errorText = EmissionStandardsPage.getErrorText();
        assertEquals("Tick to confirm your vehicles will meet the minimum Euro emission standards that the permit allows.", errorText);
    }
    @Then("the texts are displayed correctly")
    public void theTextsAreDisplayedCorrectly() {
        assertTrue(EmissionStandardsPage.isAdvisoryTextPresent());
    }
}