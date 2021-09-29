package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.InterimPage;
import org.joda.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InterminLicence extends BasePage implements En {
    private final World world;

    private static String VehicleErrorMessage = "The interim vehicle authority cannot exceed the total vehicle authority";
    private static String noDatesErrorMessage = "Value is required";

    public InterminLicence(World world) {this.world = world;}

    @And("i create a variation in internal")
    public void iCreateAVariationInInternal() {
        waitAndClick("//*[@id='menu-licence-quick-actions-create-variation']", SelectorType.XPATH);
        waitForTextToBePresent("Applying to change a licence");
        waitAndClick("//*[contains(text(),'No')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Phone')]", SelectorType.XPATH);
        waitAndClick("form-actions[submit]",SelectorType.ID);
        waitForTextToBePresent("Variation details");
    }

    @When("I have an interim vehicle authority greater than my application vehicle authority")
    public void iHaveAnInterimVehicleAuthorityGreaterThanMyApplicationVehicleAuthority() {
        clickByLinkText("add interim");
        findSelectAllRadioButtonsByValue("Y");
        InterimPage.enterInterimDetail("Test Test");
        InterimPage.startDate(LocalDate.now().getDayOfWeek(), LocalDate.now().getMonthOfYear(), LocalDate.now().getYear());
        InterimPage.endDate(LocalDate.now().plusDays(7).getDayOfWeek(), LocalDate.now().plusMonths(2).getMonthOfYear(), LocalDate.now().getYear());
        InterimPage.vehicleAuthority(world.createApplication.getNoOfVehiclesRequested() - 1);
    }

    @Then("I should get an error when i save the application")
    public void iShouldGetAnErrorWhenISaveTheApplication() {
        InterimPage.save();
        assertTrue(isTextPresent(VehicleErrorMessage));
    }

    @When("I have an interim vehicle authority equal to my application vehicle authority")
    public void iHaveAnInterimVehicleAuthorityEqualToMyApplicationVehicleAuthority() {
        clickByLinkText("add interim");
        findSelectAllRadioButtonsByValue("Y");
        InterimPage.enterInterimDetail("Test Test");
        InterimPage.startDate(LocalDate.now().getDayOfWeek(), LocalDate.now().getMonthOfYear(), LocalDate.now().getYear());
        InterimPage.endDate(LocalDate.now().plusDays(7).getDayOfWeek(), LocalDate.now().plusMonths(2).getMonthOfYear(), LocalDate.now().getYear());
        InterimPage.vehicleAuthority(world.createApplication.getNoOfVehiclesRequested());
    }

    @Then("I should be able to save the application without any errors")
    public void iShouldBeAbleToSaveTheApplicationWithoutAnyErrors() {
        InterimPage.save();
        assertFalse(isTextPresent(VehicleErrorMessage));
    }

    @When("I have an interim vehicle authority less than my application vehicle authority")
    public void iHaveAnInterimVehicleAuthorityLessThanMyApplicationVehicleAuthority() {
        clickByLinkText("add interim");
        findSelectAllRadioButtonsByValue("Y");
        InterimPage.enterInterimDetail("Test Test");
        InterimPage.startDate(LocalDate.now().getDayOfWeek(), LocalDate.now().getMonthOfYear(), LocalDate.now().getYear());
        InterimPage.endDate(LocalDate.now().plusDays(7).getDayOfWeek(), LocalDate.now().plusMonths(2).getMonthOfYear(), LocalDate.now().getYear());
        InterimPage.vehicleAuthority(world.createApplication.getNoOfVehiclesRequested() - 1);
    }

    @When("I create an interim application with no start and end dates")
    public void iCreateAnInterimApplicationWithNoStartAndEndDates() {
        clickByLinkText("add interim");
        findSelectAllRadioButtonsByValue("Y");
        InterimPage.enterInterimDetail("Test Test");
        InterimPage.vehicleAuthority(world.createApplication.getNoOfVehiclesRequested());
        InterimPage.trailerAuthority(world.createApplication.getNoOfVehiclesRequested());
    }

    @Then("I should not error when i save the application")
    public void iShouldNotErrorWhenISaveTheApplication() {
        InterimPage.save();
        assertFalse(isTextPresent(noDatesErrorMessage));
    }

    @When("I create an interim application with a start and no end date")
    public void iCreateAnInterimApplicationWithAStartAndNoEndDate() {
        clickByLinkText("add interim");
        findSelectAllRadioButtonsByValue("Y");
        InterimPage.startDate(10, 8, 2017);
        InterimPage.enterInterimDetail("Interim with no dates");
        InterimPage.vehicleAuthority(world.createApplication.getNoOfVehiclesRequested());
        InterimPage.trailerAuthority(world.createApplication.getNoOfVehiclesRequested());
    }

    @Then("I should error when i attempt to grant the application")
    public void iShouldErrorWhenIAttemptToGrantTheApplication() {
        InterimPage.save();
        clickByLinkText("Interim details");
        waitForTextToBePresent("Interim application");
        InterimPage.grant();
        isTextPresent(noDatesErrorMessage);
    }
}
