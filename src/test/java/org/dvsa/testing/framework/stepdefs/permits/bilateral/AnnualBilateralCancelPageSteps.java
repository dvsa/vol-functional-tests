package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.BaseJourney;
import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.CancellationConfirmationPage;
import org.dvsa.testing.lib.newPages.external.pages.CancellationPage;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.BasePage;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnnualBilateralCancelPageSteps extends BasePage implements En {

    public AnnualBilateralCancelPageSteps(OperatorStore operatorStore, World world) {
        And("^I click cancel application link$", () -> {
            operatorStore.getLatestLicence().get().setReferenceNumber(BasePermitPage.getReferenceFromPage());
            OverviewPage.clickCancelApplication();
        });
        Then("^the application reference number should be displayed above the heading$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });

        When("^I should see the correct text displayed next to the checkbox", () -> {
            assertTrue(isElementPresent("//label[@class='form-control form-control--checkbox']", SelectorType.XPATH));
        });
        When("the checkbox is selected", CancellationPage::clickCancelCheckbox);
        When("^the cancel application button is selected without checkbox ticked$", CancellationPage::clickCancelButton);
        Then ("^I should be taken to cancel confirmation page$", () -> {
            CancellationConfirmationPage.untilOnPage();
            assertEquals("Application cancelled", CancellationConfirmationPage.getPanelHeading());
            assertEquals(BasePermitJourney.getReferenceNumber(), CancellationConfirmationPage.getReferenceNumberHeading());
            assertEquals("What happens now", CancellationConfirmationPage.getAdvisoryHeadingPresent());
            assertEquals("You have cancelled your application and you will no longer be able to view or access it.", CancellationConfirmationPage.getAdvisoryTextPresent());
        });
        And("I select cancel application button", CancellationPage::clickCancelButton);
        //Guidance link no more displayed on the page,changed the assertion
        Then("I select finish button", BilateralJourneySteps::clickFinishButton);
        And("^I click cancel application link for bilateral application$", BilateralJourneySteps::bilateralCancel);
    }
}

