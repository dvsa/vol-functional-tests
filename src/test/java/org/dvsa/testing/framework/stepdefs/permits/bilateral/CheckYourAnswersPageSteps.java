package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckYourAnswersPageSteps extends BasePage implements En {
    public CheckYourAnswersPageSteps(World world) {
        Then("^Country name displayed on the Bilateral check your answers page is the one clicked on the overview page$", () -> {
            CheckYourAnswerPage.untilOnPage();
            String country = getText("//div[@class='govuk-caption-xl']", SelectorType.XPATH);
            assertEquals(country, AnnualBilateralJourney.getCountry());
        });

        Then("^the page heading on the check your answers page is correct$", () -> {
            CheckYourAnswerPage.untilOnPage();
            String heading = CheckYourAnswerPage.getPageHeading();
            assertEquals("Check your answers", heading);
        });

        Then("^I see four sections displayed on the table correctly$", () -> {
            assertTrue(BilateralJourneySteps.areSectionsPresent(true));
        });

        Then("^Period type displayed on the check your answers page is the one I selected on the Period selection page$", () -> {
            assertEquals(BilateralJourneySteps.getPeriodText(), AnnualBilateralJourney.getPeriodType());
        });

        Then("^Journey type displayed on the check your answers page is the one I selected on the Permits usage$", () -> {
            assertEquals(BilateralJourneySteps.getJourney(),PermitUsagePage.getJourney());
        });

        When("^I click Confirm and return to overview$", CheckYourAnswerPage::clickConfirmAndReturnToOverview);

        Then("^the status of Answer questions for individual countries section for the selected country is set as complete$", () -> {
            String s1 = BasePage.getElementValueByText("//li[2]//ul[1]//li[1]//span[2]",SelectorType.XPATH);
            assertEquals(s1,"COMPLETED");
        });
    }
}
