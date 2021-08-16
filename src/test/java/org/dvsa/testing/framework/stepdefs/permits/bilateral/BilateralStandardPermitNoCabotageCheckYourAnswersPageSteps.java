package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class BilateralStandardPermitNoCabotageCheckYourAnswersPageSteps implements En {
    public BilateralStandardPermitNoCabotageCheckYourAnswersPageSteps(World world) {
        Then("^I am on the Bilateral Standard permits no Cabotage check your answers page$", () -> {
            AnnualBilateralJourney.startBilateralJourneyTypeAndSelectCabotageUntilCheckYourAnswersPage(world, PeriodType.BilateralsStandardPermitsNoCabotage, Country.Norway, null);
        });

        Then("^I see three sections displayed on the table correctly$", () -> {
            assertTrue(BilateralJourneySteps.areSectionsPresent(false));
        });

        Then("^For bilateral standard permits no cabotage permit type,the value of how many permits you need, will be as per the ones saved on the number of permits page$", () -> {
            String permitLabel = NumberOfPermitsPageJourney.getLabel();
            String permitValue = String.valueOf(NumberOfPermitsPageJourney.getPermitValue());
            Assert.assertEquals(BilateralJourneySteps.getCabotageValue(), permitValue + " " + permitLabel + "s");
        });
    }
}

