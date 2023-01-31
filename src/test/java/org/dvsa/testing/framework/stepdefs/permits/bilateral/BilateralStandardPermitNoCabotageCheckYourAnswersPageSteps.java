package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;

import static org.junit.jupiter.api.Assertions.*;

public class BilateralStandardPermitNoCabotageCheckYourAnswersPageSteps {
    private final World world;

    public BilateralStandardPermitNoCabotageCheckYourAnswersPageSteps(World world) {
        this.world = world;
    }

    @Then("I am on the Bilateral Standard permits no Cabotage check your answers page")
    public void iAmOnTheBilateralStandardPermitsNoCabotageCheckYourAnswers() {
        AnnualBilateralJourney.startBilateralJourneyTypeAndSelectCabotageUntilCheckYourAnswersPage(world, PeriodType.BilateralsStandardPermitsNoCabotage, Country.Norway, null);
    }

    @Then("I see three sections displayed on the table correctly")
    public void iSeeThreeSectionsDisplayedOnTheTableCorrectly() {
        assertTrue(BilateralJourneySteps.areSectionsPresent(false));
    }

    @Then("For bilateral standard permits no cabotage permit type,the value of how many permits you need, will be as per the ones saved on the number of permits page")
    public void forBilateralStandardPermitsNoCabotagePermitType() {
        String permitLabel = NumberOfPermitsPageJourney.getLabel();
        String permitValue = String.valueOf(NumberOfPermitsPageJourney.getPermitValue());
        assertEquals(BilateralJourneySteps.getCabotageValue(), permitValue + " " + permitLabel + "s");
    }
}