package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.CancellationPage;
import org.dvsa.testing.lib.newPages.permits.pages.CancellationConfirmationPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.OverviewPage;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class AnnualBilateralCancelPageSteps extends BasePage implements En {

    public AnnualBilateralCancelPageSteps(OperatorStore operatorStore, World world) {
        And("^I click cancel application link$", () -> {
            operatorStore.getLatestLicence().get().setReferenceNumber(BasePermitPage.getReference());
            OverviewPage.Application.cancel();
        });
        Then("^the application reference number should be displayed above the heading$", () -> {
            String actualReference = BasePermitPage.getReference();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        Then("^the bilateral CancelApplication heading should be correct$", CancellationPage::untilOnCancelApplicationPage);

        When("^I should see the correct text displayed next to the checkbox", () -> {
            assertTrue(isElementPresent("//label[@class='form-control form-control--checkbox']", SelectorType.XPATH));
        });
        When("the checkbox is selected", CancellationPage::clickCancelCheckbox);
        Then("^the bilateral CancelApplication page displays the correct advisory text$", CancellationPage::assertAdvisoryTextOnCancelApplicationPage);
        When("^the cancel application button is selected without checkbox ticked$", CancellationPage::clickCancelButton);
        Then ("I should be taken to cancel confirmation page", () -> {
            CancellationConfirmationPage.untilOnCancelConfirmationPage();
            CancellationConfirmationPage.assertReferenceOnCancelConfirmationPage(world.applicationDetails.getLicenceNumber());
            CancellationConfirmationPage.assertCancelConfirmationPageAdvisoryText();
        });
        And("I select cancel application button", CancellationPage::clickCancelButton);
        //Guidance link no more displayed on the page,changed the assertion
        Then("I select finish button", BilateralJourneySteps::clickFinishButton);
        And("^I click cancel application link for bilateral application$", BilateralJourneySteps::bilateralCancel);
        And("^I am on the cancel application page for Annual Bilateral page$", CancellationPage::untilOnCancelApplicationPage);
    }
}

