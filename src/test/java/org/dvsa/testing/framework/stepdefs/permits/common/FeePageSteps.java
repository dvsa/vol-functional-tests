package org.dvsa.testing.framework.stepdefs.permits.common;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.hamcrest.core.StringContains;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class FeePageSteps extends BasePage {
    private final World world;

    public FeePageSteps(World world) {
        this.world = world;
    }

    @And("I am on the fee page")
    public void iAmOnTheFeePage() {
        world.ecmtApplicationJourney.completeApplicationUntilFeePage();
    }

    @When("I submit and pay")
    public void iSubmitAndPay() {
        PermitFeePage.saveAndContinue();
    }

    @Then("I am taken to CPMS payment page")
    public void iAmTakenToCPMSPaymentPage() {
        assertThat(getURL().getHost(), StringContains.containsString("e-paycapita"));
    }

    @Then("the page heading and alert message on the fee page is displayed correctly")
    public void thePageHeadingAndAlertMessageOnTheFeePage() {
        assertEquals("Permit fee", PermitFeePage.getPageHeading());
        assertTrue(world.permitFeePage.isAlertMessagePresent());
        assertEquals("Fee summary", world.permitFeePage.getSubHeading());
    }

    @Then("the table contents matches as per AC")
    public void theTableContentsMatchesAsPerAC() {
        world.permitFeePage.tableCheck();
        String expectedDateTime = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        String actualDate = PermitFeePage.getTableSectionValue(FeeSection.ApplicationDate);
        assertEquals(expectedDateTime, actualDate);
    }
}
