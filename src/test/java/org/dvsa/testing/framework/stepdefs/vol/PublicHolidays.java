package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PublicHolidays extends BasePage {
    private final World world;

    public PublicHolidays(World world) {this.world = world;}

    @When("I am on the public holidays page")
    public void iAmOnThePublicHolidaysPage() throws HttpException {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.adminNavigation(AdminOption.PUBLIC_HOLIDAYS);
    }

    @Given("an admin adds a public holiday")
    public void anAdminAddsAPublicHoliday(){
        world.publicHolidayJourney.addPublicHoliday();
    }

    @Then("that holiday should be displayed")
    public void thatHolidayShouldBeDisplayed() {
        String actualDate = getValue("(//input[@type='submit'])[2]", SelectorType.XPATH);
        String expectedDate = LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        waitForTitleToBePresent("Public holidays");
        assertEquals(expectedDate, actualDate);
    }

    @Given("an admin edits a public holiday")
    public void anAdminEditsAPublicHoliday() {
        world.publicHolidayJourney.editPublicHoliday();
    }

    @Then("that edited holiday should be displayed")
    public void thatEditedHolidayShouldBeDisplayed() {
        String actualDate = getValue("(//input[@type='submit'])[2]", SelectorType.XPATH);
        String expectedDate = LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        assertEquals(expectedDate, actualDate);
    }

    @Given("an admin deletes a public holiday")
    public void anAdminDeletesAPublicHoliday() {
        world.publicHolidayJourney.deletePublicHoliday();
    }

    @Then("that holiday should not be displayed")
    public void thatHolidayShouldNotBeDisplayed() {
        assertTrue(isElementPresent("//p[text()='The public holiday is removed']", SelectorType.XPATH));
    }
}