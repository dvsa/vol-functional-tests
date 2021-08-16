package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.Assert.assertEquals;


public class CancelApplicationPageSteps extends BasePermitPage implements En {

    public CancelApplicationPageSteps(World world) {
        Given("^I am on the cancel application page$", () -> {
            EcmtApplicationJourney.beginApplication(world);
            OverviewPage.clickCancelApplication();
        });
        Then("^I should get an error message on cancel application page$", () -> {
            assertEquals("Tick to confirm you want to withdraw your application", CancellationPage.getErrorText());
        });
        Then("^I should get an error message on Annual ECMT cancel application page$", () -> {
            assertEquals("You must select the checkbox to continue", CancellationPage.getErrorText());
        });
        When("^I cancel my ECMT application$", CancellationPage::saveAndContinue);
    }

}
