package org.dvsa.testing.framework.stepdefs.permits.common;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.hamcrest.core.StringContains;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeePageSteps extends BasePage implements En {

    public FeePageSteps(World world) {

        And("^I am on the fee page$", () -> {
            EcmtApplicationJourney.completeApplicationUntilFeePage(world);
        });
        When("^I submit and pay$", PermitFeePage::saveAndContinue);
        Then("^I am taken to CPMS payment page$", () -> {
            assertThat(getURL().getHost(), StringContains.containsString("e-paycapita"));
        });
        Then("^the page heading and alert message on the fee page is displayed correctly$", () -> {
            assertEquals("Permit fee", PermitFeePage.getPageHeading());
            assertTrue(PermitFeePage.isAlertMessagePresent());
            assertEquals("Fee summary", PermitFeePage.getSubHeading());
        });
        Then("^the table contents matches as per AC$", () -> {
            PermitFeePage.tableCheck();
            String expectedDateTime = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            String actualDate = PermitFeePage.getTableSectionValue(FeeSection.ApplicationDate);
            assertEquals(expectedDateTime, actualDate);
        });
    }
}