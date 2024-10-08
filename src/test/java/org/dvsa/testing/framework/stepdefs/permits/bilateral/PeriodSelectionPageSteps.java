package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.PeriodSelectionPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.PeriodSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.*;

public class PeriodSelectionPageSteps extends BasePermitPage {

    World world;
    public PeriodSelectionPageSteps(World world) {
        this.world = world;
    }

    @Then("I am on the Bilateral {string} Period Selection page with correct information and content")
    public void iAmOnTheBilateralPeriodSelectionPageWthCorrectInformation(String country) {
        PeriodSelectionPage.untilOnPage();

        // Checking Page heading
        PeriodSelectionPageJourney.hasPageHeading();

        //Checking Country name displayed on the page is the one clicked on the overview page
        assertEquals(PeriodSelectionPage.getCountry(), AnnualBilateralJourney.getCountry());

        assertTrue(isElementPresent(String.format("//div[contains(text(),'%s')]", country), SelectorType.XPATH));
        if (country.equals("Turkey")) {
            world.annualBilateralJourney.setPeriodType(PeriodType.BilateralsTurkey);
        } else if (country.equals("Ukraine")) {
            world.annualBilateralJourney.setPeriodType(PeriodType.BilateralsUkraine);
        }
    }

    @When("I select continue button on the Bilateral period selection page")
    public void iSelectContinueButtonOnTheBilateralPeriod() {
        PeriodSelectionPage.saveAndContinue();
    }
}