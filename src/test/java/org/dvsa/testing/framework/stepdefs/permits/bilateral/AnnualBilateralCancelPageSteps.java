package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationConfirmationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;

import static org.junit.Assert.*;

public class AnnualBilateralCancelPageSteps extends BasePage implements En {

    public AnnualBilateralCancelPageSteps(World world) {
        When("the checkbox is selected", CancellationPage::clickCancelCheckbox);
        Then ("^I should be taken to cancel confirmation page$", () -> {
            CancellationConfirmationPage.untilOnPage();
            assertEquals("Application cancelled", CancellationConfirmationPage.getPanelHeading());
            assertTrue(BasePermitJourney.getFullReferenceNumber().contains(CancellationConfirmationPage.getReferenceNumberHeading()));
            assertEquals("What happens now", CancellationConfirmationPage.getAdvisoryHeadingPresent());
            assertEquals("You have cancelled your application and you will no longer be able to view or access it.", CancellationConfirmationPage.getAdvisoryTextPresent());
        });
        And("I select cancel application button", CancellationPage::clickCancelButton);
        //Guidance link no more displayed on the page,changed the assertion
        Then("I select finish button", SubmittedPage::goToPermitsDashboard);
    }
}

