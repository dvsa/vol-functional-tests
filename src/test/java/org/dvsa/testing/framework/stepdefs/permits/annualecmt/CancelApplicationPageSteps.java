package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.Assert.assertEquals;


public class CancelApplicationPageSteps extends BasePermitPage implements En {

    public CancelApplicationPageSteps(World world, OperatorStore operatorStore) {
        Given("^I am on the cancel application page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPage.clickCancelApplication();
        });
        And("^I have not confirmed I would like to cancel$", () -> {
            // Here for readability and to stop cucumber from throwing an exception
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
