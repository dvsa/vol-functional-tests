package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;

import static org.junit.Assert.assertTrue;

public class TurkeyDeclarationPageSteps implements En {
    public TurkeyDeclarationPageSteps() {

        When("^I am taken to the bilateral declaration Page with correct information and content$", () -> {
            DeclarationPage.untilOnPage();

            // Checking declaration page content
            DeclarationPageJourneySteps.hasPageHeading();
            assertTrue(DeclarationPage.isBilateralAdvisoryTextPresent());
            assertTrue(DeclarationPage.isWarningTextPresent());
        });
        When("^I click on Accept and continue on the Declaration page without selecting declaration checkbox$", DeclarationPage::saveAndContinue);
        Then("^I should get the correct error message on the declaration page$", DeclarationPageJourneySteps::hasErrorText);
    }
}

