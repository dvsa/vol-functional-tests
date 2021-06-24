package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;

import static org.junit.Assert.assertTrue;

public class DeclarationPageSteps implements En {
    public DeclarationPageSteps(OperatorStore operator, World world) {
        And("I am on the annual multilateral declaration page", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication()
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator);
            NumberOfPermitsPageJourney.completeMultilateralPage();
            AnnualMultilateralJourney.INSTANCE
                    .checkYourAnswers();

            DeclarationPage.untilOnPage();
        });
        Then("^annual multilateral declaration page has advisory messages$", () -> {
            assertTrue(DeclarationPage.isMultilateralAdvisoryTextPresent());
            assertTrue(DeclarationPage.isGuidanceNotesLinkPresent());
        });
        Then("^the annual multilateral declaration checkbox is unselected$", () -> {
            String message = "Expected the checkbox to be unselected but it was";
            assertTrue(message, DeclarationPage.declarationIsNotConfirmed());
        });
        Then("^the status for the declaration section in annual multilateral is complete$", () -> {
            OverviewPageJourney.checkStatus(OverviewSection.Declaration, PermitStatus.COMPLETED);
        });
    }
}
