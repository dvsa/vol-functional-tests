package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PublicHolidays extends BasePage {
    private final World world;

    public PublicHolidays(World world) {this.world = world;}

    @When("I am on the public holidays page")
    public void iAmOnThePublicHolidaysPage() {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.AdminNavigation(AdminOption.PUBLIC_HOLIDAY);
    }

    @Given("an admin adds a public holiday")
    public void anAdminAddsAPublicHoliday(){
        world.publicHolidayJourney.addPublicHoliday();
    }

    @Then("that holiday should be displayed")
    public void thatHolidayShouldBeDisplayed() {
        String actualDate = getValue("(//input[@type='submit'])[2]", SelectorType.XPATH);
        String expectedDate = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Assert.assertEquals(expectedDate, actualDate);
    }

    @Given("an admin edits a public holiday")
    public void anAdminEditsAPublicHoliday() {
        world.publicHolidayJourney.editPublicHoliday();
    }

    @Then("that edited holiday should be displayed")
    public void thatEditedHolidayShouldBeDisplayed() {
        String actualDate = getValue("(//input[@type='submit'])[2]", SelectorType.XPATH);
        String expectedDate = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Assert.assertEquals(expectedDate, actualDate);
    }

    @Given("an admin deletes a public holiday")
    public void anAdminDeletesAPublicHoliday() {
        world.publicHolidayJourney.deletePublicHoliday();
    }

    @Then("that holiday should not be displayed")
    public void thatHolidayShouldNotBeDisplayed() {
        Assert.assertTrue(isElementPresent("//p[text()='The public holiday is removed']", SelectorType.XPATH));
    }
}
