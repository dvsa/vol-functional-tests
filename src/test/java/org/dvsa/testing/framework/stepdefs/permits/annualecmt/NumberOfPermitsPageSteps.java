package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.*;


public class NumberOfPermitsPageSteps extends BasePage {
    private final World world;

    public NumberOfPermitsPageSteps(World world) {
        this.world = world;
    }

    @And("I am on the number of permits page")
    public void iAmOnTheNumberOfPermitsPage() {
        EcmtApplicationJourney.completeUntilNumberOfPermitsPage(world);
    }

    @And("the page heading on the ECMT number of permits page is displayed correctly")
    public void thePageHeadingOnTheECMTNumber() {
        NumberOfPermitsPageJourney.hasPageHeading();
    }

    @And("the advisory texts are displayed correctly")
    public void theAdvisoryTextsAreDisplayedCorrectly() {
        assertTrue(NumberOfPermitsPage.isAdvisoryTextPresent());
    }

    @Given("I have specified a number greater than the number of authorised vehicles")
    public void iHaveSpecifiedANumberGreaterThanTheNumber() {
        NumberOfPermitsPage.exceedAuthorisedVehicle();
    }

    @Then("I select the fee tab and pay the outstanding fees")
    public void iSelectTheFeeTab() {
        HomePage.selectTab(Tab.FEES);
        assertTrue(HomePage.FeesTab.areOutstandingFeesPresent());
        HomePageJourney.payAllOutstandingFees();
        world.feeAndPaymentJourney.customerPaymentModule();
    }

    @Then("I am taken to the payment successful page")
    public void iAmTakenToThePaymentSuccessfulPage() {
        isPath("//fees/receipt/");
        assertEquals(BasePage.getElementValueByText("//h1[@class='js-title']", SelectorType.XPATH), "Payment successful");
    }
}