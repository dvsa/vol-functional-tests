package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.BaseJourney;
import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationConfirmationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnnualBilateralCancelPageSteps extends BasePage implements En {

    public AnnualBilateralCancelPageSteps(OperatorStore operatorStore, World world) {
        And("^I click cancel application link$", () -> {
            operatorStore.getLatestLicence().get().setReferenceNumber(BasePermitPage.getReferenceFromPage());
            OverviewPage.clickCancelApplication();
        });
        When("the checkbox is selected", CancellationPage::clickCancelCheckbox);
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
    }
}

