package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.external.permit.bilateral.DeclarationPage;

public class TurkeyDeclarationPageSteps implements En {
    public TurkeyDeclarationPageSteps() {

        When("^I am taken to the bilateral declaration Page with correct information and content$", () -> {
            DeclarationPage.untilOnDeclarationPage();

            // Checking declaration page content
            DeclarationPage.pageHeading();
            DeclarationPage.advisoryTexts();
            DeclarationPage.warningText();
        });
        When("^I click on Accept and continue on the Declaration page without selecting declaration checkbox$", DeclarationPage::acceptAndContinue);
        Then("^I should get the validation error message on the declaration page$", DeclarationPage::errorText);

    }
}

