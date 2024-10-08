package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;

import static org.junit.jupiter.api.Assertions.*;

public class CheckYourAnswersPageSteps extends BasePage  {
    World world;

    public CheckYourAnswersPageSteps(World world) {
        this.world = world;
    }

    @Then("Country name displayed on the Bilateral check your answers page is the one clicked on the overview page")
    public void countryNameDisplayedOnTheBilateralCheckYourAnswersPageIsTheOneClickedOnTheOverviewPage() {
        CheckYourAnswerPage.untilOnPage();
        String country = getText("//div[@class='govuk-caption-xl']", SelectorType.XPATH);
        assertEquals(country, AnnualBilateralJourney.getCountry());
    }

    @Then("the page heading on the check your answers page is correct")
    public void thePageHeadingOnTheCheckYourAnswersPageIsCorrect() {
        CheckYourAnswerPage.untilOnPage();
        String heading = CheckYourAnswerPage.getPageHeading();
        assertEquals("Check your answers", heading);
    }

    @Then("I see four sections displayed on the table correctly")
    public void iSeeFourSectionsDisplayedOnTheTableCorrectly() {
        assertTrue(BilateralJourneySteps.areSectionsPresent(true));
    }

    @Then("Period type displayed on the check your answers page is the one I selected on the Period selection page")
    public void periodTypeDisplayedOnTheCheckYourAnswersPageIsTheOneISelected() {
        assertEquals(BilateralJourneySteps.getPeriodText(), world.annualBilateralJourney.getPeriodType());
    }

    @Then("Journey type displayed on the check your answers page is the one I selected on the Permits usage")
    public void journeyTypeDisplayedOnTheCheckYourAnswersPageIsTheOneISelectedOnThePermitsUsage() {
        assertEquals(BilateralJourneySteps.getJourney(), PermitUsagePage.getJourney());
    }

    @When("I click Confirm and return to overview")
    public void iClickConfirmAndReturnToOverview() {
        CheckYourAnswerPage.clickConfirmAndReturnToOverview();
    }

    @Then("the status of Answer questions for individual countries section for the selected country is set as complete")
    public void theStatusOfAnswerQuestionsForIndividualCountriesSectionsForTheSelectedCountry() {
        String s1 = BasePage.getElementValueByText("//li[2]//ul[1]//li[1]//span[2]", SelectorType.XPATH);
        assertEquals(s1, "COMPLETED");
    }
}