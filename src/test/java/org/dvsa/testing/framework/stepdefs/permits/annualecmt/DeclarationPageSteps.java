package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeclarationPageSteps implements En {

    public DeclarationPageSteps(World world) {
        Then("^I should see the validation error message for the declaration page$", () -> assertTrue(DeclarationPage.isErrorMessagePresent()));
        When("^I save and continue on the declaration page$", DeclarationPage::saveAndContinue);
        And("^I should see the declaration advisory texts$", DeclarationPage::isECMTAdvisoryTextPresent);
        When("^I accept and continue$", DeclarationPage::saveAndContinue);
        When("^I should be on the ECMT permit fee page$", () -> {
            assertEquals("Permit fee", PermitFeePage.getPageHeading());
        });
        Then("^the status for the declaration section in annual ECMT is complete$", () -> {
            OverviewPageJourney.checkStatus(OverviewSection.Declaration, PermitStatus.COMPLETED);
        });
    }
}