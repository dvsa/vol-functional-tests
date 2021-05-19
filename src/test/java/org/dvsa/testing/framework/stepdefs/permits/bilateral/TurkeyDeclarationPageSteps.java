package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;

public class TurkeyDeclarationPageSteps implements En {
    public TurkeyDeclarationPageSteps() {

        When("^I am taken to the bilateral declaration Page with correct information and content$", () -> {
            DeclarationPage.untilOnPage();

            // Checking declaration page content
            DeclarationPage.hasPageHeading();
            DeclarationPage.hasBilateralAdvisoryTexts();
            DeclarationPage.hasWarningText();
        });
        When("^I click on Accept and continue on the Declaration page without selecting declaration checkbox$", DeclarationPage::saveAndContinue);
        Then("^I should get the validation error message on the declaration page$", DeclarationPage::hasErrorText);

    }
}

