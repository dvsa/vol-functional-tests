package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;

public class ApplicationOverviewPageSteps {
    public ApplicationOverviewPageSteps() {
    }

    @When("I click on read declaration on the application overview page")
    public void iClickOnReadDeclarationOnTheApplicationOverviewPage() {
        OverviewPageJourney.clickBilateralOverviewSection(OverviewSection.BilateralDeclaration);
    }
}