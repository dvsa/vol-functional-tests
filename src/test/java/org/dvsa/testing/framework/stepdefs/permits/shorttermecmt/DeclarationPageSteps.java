package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.permits.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;

public class DeclarationPageSteps {
    @Then("I should see the correct heading on the declaration page")
    public void iShouldSeeTheCorrectHeadingOnTheDeclaration() {
        DeclarationPageJourney.hasPageHeading();
    }

    @Then("the declaration page has a reference number")
    public void theDeclarationPageHasARefNumber() {
        DeclarationPage.hasReference();
    }
}
