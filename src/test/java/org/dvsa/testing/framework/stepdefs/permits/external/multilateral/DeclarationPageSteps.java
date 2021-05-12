package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.multilateral.DeclarationPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage;
import org.junit.Assert;

public class DeclarationPageSteps implements En {
    public DeclarationPageSteps(OperatorStore operator, World world) {
        When("I should be on the Annual Multilateral Declaration page", DeclarationPage::untilOnPage);
        When("^I make my declaration$", () -> {
            DeclarationPage.untilOnPage();
            DeclarationPage.declare(true);
        });
        And("I am on the annual multilateral declaration page", () -> {
            AnnualMultilateralJourney.INSTANCE
                    .signin(operator, world)
                    .beginApplication()
                    .permitType(PermitTypePage.PermitType.AnnualMultilateral, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewPage.Section.NumberOfPaymentsRequired, operator)
                    .numberOfPermitsPage(operator)
                    .checkYourAnswers();

            DeclarationPage.untilOnPage();
        });
        Then("^the annual multilateral declaration page has an application reference number$", () -> {
            String message = "Expected Annual Multilateral page to possess reference number but" +
                    " unable to find one with the write format";
            Assert.assertTrue(message, DeclarationPage.hasReference());
        });
        Then("^annual multilateral declaration page has advisory messages$", () -> {
            DeclarationPage.hasAdvisoryText();
            Assert.assertTrue(DeclarationPage.hasGuidanceNotesLink());
        });
        Then("^the annual multilateral declaration checkbox is unselected$", () -> {
            String message = "Expected the checkbox to be unselected but it was";
            Assert.assertTrue(message, DeclarationPage.hasNotDeclared());
        });
        Then("^the status for the declaration section in annual multilateral is complete$", () -> {
            boolean isComplete = OverviewPage.checkStatus(String.valueOf(PermitSection.Declaration), PermitStatus.COMPLETED);
            Assert.assertTrue("The annual multilateral section status is not complete", isComplete);
        });
    }
}
