package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitFeePage;
import org.junit.Assert;

public class DeclarationPageSteps implements En {

    public DeclarationPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the declaration page$", () -> {
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world,operatorStore);
            ECMTPermitApplicationSteps.saveAndContinue();
        });
        Then("^I should see the validation error message for the declaration page$", () -> Assert.assertTrue(DeclarationPage.hasErrorMessagePresent()));
        When("^I save and continue on the declaration page$", DeclarationPage::saveAndContinue);
        And("^I should see the ECMT declaration advisory texts$", org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage::hasECMTAdvisoryText);
        When("^I make my ECMT declaration$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage.confirmDeclaration();
        });
        When("^I accept and continue$", DeclarationPage::saveAndContinue);
        When("^I should be on the ECMT permit fee page$", PermitFeePage::pageHeading);
        Then("^the status for the declaration section in annual ECMT is complete$", () -> {
            OverviewPage.checkStatus(OverviewSection.Declaration, PermitStatus.COMPLETED);
        });
    }

}
