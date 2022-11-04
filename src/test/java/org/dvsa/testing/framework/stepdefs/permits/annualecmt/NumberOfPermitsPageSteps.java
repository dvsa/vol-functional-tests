package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumberOfPermitsPageSteps extends BasePage implements En {

    public NumberOfPermitsPageSteps(World world) {
        And("^I am on the number of permits page$", () -> {
            EcmtApplicationJourney.completeUntilNumberOfPermitsPage(world);
        });
        And("^the page heading on the ECMT number of permits page is displayed correctly$", NumberOfPermitsPageJourney::hasPageHeading);
        And("^the advisory texts are displayed correctly$", () -> {
            assertTrue(NumberOfPermitsPage.isAdvisoryTextPresent());
        });

        Given("^I have specified a number greater than the number of authorised vehicles$", NumberOfPermitsPage::exceedAuthorisedVehicle);
        Then("^I select the fee tab and pay the outstanding fees$", () -> {
            HomePage.selectTab(Tab.FEES);
            assertTrue(HomePage.FeesTab.areOutstandingFeesPresent());
            HomePageJourney.payAllOutstandingFees();
            world.feeAndPaymentJourney.customerPaymentModule();
        });
        Then("^I am taken to the payment successful page$", () -> {
            assertTrue(isPath("/fees/receipt/"));
            assertEquals(BasePage.getElementValueByText("//h1[@class='js-title']",SelectorType.XPATH),"Payment successful");
        });
    }
}