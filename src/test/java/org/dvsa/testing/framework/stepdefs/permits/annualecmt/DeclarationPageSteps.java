package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.BaseCheckYourAnswersPage;
import org.dvsa.testing.lib.pages.external.permit.DeclarationPage;
import org.dvsa.testing.lib.pages.external.permit.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.ecmt.FeeOverviewPage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.junit.Assert;

public class DeclarationPageSteps implements En {

    public DeclarationPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the declaration page$", () -> {
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world,operatorStore);
            BaseCheckYourAnswersPage.saveAndContinue();
        });
        Then("^I should see the validation error message for the declaration page$", () -> Assert.assertTrue(DeclarationPage.hasErrorMessagePresent()));
        When("^I save and continue on the declaration page$", DeclarationPage::saveAndContinue);
        And("^I should see the ECMT declaration advisory texts$", DeclarationPage::ECMTDeclarationAdvisoryText);
        When("^I make my ECMT declaration$", () -> {
            DeclarationPage.declare(true);
        });
        When("^I accept and continue$", DeclarationPage::saveAndContinue);
        When("^I should be on the ECMT permit fee page$", FeeOverviewPage::pageHeading);
        Then("^the status for the declaration section in annual ECMT is complete$", () -> {
            boolean isComplete = OverviewPage.checkStatus(PermitSection.Declaration,PermitStatus.COMPLETED);
            Assert.assertTrue("The annual ECMT section status is not complete", isComplete);
        });
    }

}
