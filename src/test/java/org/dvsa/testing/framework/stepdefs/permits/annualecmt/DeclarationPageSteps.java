package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitFeePage;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

public class DeclarationPageSteps implements En {

    public DeclarationPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the declaration page$", () -> {
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world,operatorStore);
            ECMTPermitApplicationSteps.saveAndContinue();
        });
        Then("^I should see the validation error message for the declaration page$", () -> Assert.assertTrue(DeclarationPage.isErrorMessagePresent()));
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
