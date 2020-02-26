package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.internal.InterimPage;
import org.joda.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class InterimLicence extends BasePage implements En {

    private static String VehicleErrorMessage = "The interim vehicle authority cannot exceed the total vehicle authority";
    private static String noDatesErrorMessage = "Value is required";
    private World world;

    public InterimLicence(World world) throws Exception {
        this.world = world;

        When("^I have an interim vehicle authority greater than my application vehicle authority$", () -> {
            clickByLinkText("add interim");
            findSelectAllRadioButtonsByValue("Y");
            InterimPage.startDate(LocalDate.now().getDayOfWeek(), LocalDate.now().getMonthOfYear(), LocalDate.now().getYear());
            InterimPage.endDate(LocalDate.now().plusDays(7).getDayOfWeek(), LocalDate.now().plusMonths(2).getMonthOfYear(), LocalDate.now().getYear());
            InterimPage.vehicleAuthority(world.createLicence.getNoOfVehiclesRequired() + 1);
        });

        When("^I have an interim vehicle authority equal to my application vehicle authority$", () -> {
            clickByLinkText("add interim");
            findSelectAllRadioButtonsByValue("Y");
            InterimPage.enterInterimDetail("Test Test");
            InterimPage.startDate(LocalDate.now().getDayOfWeek(), LocalDate.now().getMonthOfYear(), LocalDate.now().getYear());
            InterimPage.endDate(LocalDate.now().plusDays(7).getDayOfWeek(), LocalDate.now().plusMonths(2).getMonthOfYear(), LocalDate.now().getYear());
            InterimPage.vehicleAuthority(world.createLicence.getNoOfVehiclesRequired());
        });

        When("^I have an interim vehicle authority less than my application vehicle authority$", () -> {
            clickByLinkText("add interim");
            findSelectAllRadioButtonsByValue("Y");
            InterimPage.enterInterimDetail("Test Test");
            InterimPage.startDate(LocalDate.now().getDayOfWeek(), LocalDate.now().getMonthOfYear(), LocalDate.now().getYear());
            InterimPage.endDate(LocalDate.now().plusDays(7).getDayOfWeek(), LocalDate.now().plusMonths(2).getMonthOfYear(), LocalDate.now().getYear());
            InterimPage.vehicleAuthority(world.createLicence.getNoOfVehiclesRequired() - 1);
        });

        When("^I create an interim application with no start and end dates$", () -> {
            clickByLinkText("add interim");
            findSelectAllRadioButtonsByValue("Y");
            InterimPage.enterInterimDetail("Test Test");
            InterimPage.vehicleAuthority(world.createLicence.getNoOfVehiclesRequired());
            InterimPage.trailerAuthority(world.createLicence.getNoOfVehiclesRequired());
        });

        Then("^I should get an error when i save the application$", () -> {
            InterimPage.save();
            assertTrue(isTextPresent(VehicleErrorMessage, 60));
        });

        Then("^I should be able to save the application without any errors$", () -> {
            InterimPage.save();
            assertFalse(isTextPresent(VehicleErrorMessage, 60));
        });

        Then("^I should not error when i save the application$", () -> {
            InterimPage.save();
            assertFalse(isTextPresent(noDatesErrorMessage, 60));
        });

        Then("^I should error when i attempt to grant the application$", () -> {
            InterimPage.save();
            clickByLinkText("Interim details");
            waitForTextToBePresent("Interim application");
            InterimPage.grant();
            isTextPresent(noDatesErrorMessage, 60);
        });
        And("^i have logged in to internal$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        });
        And("^i search for my application", () -> {
            if (isElementPresent("//select[@id='search-select']", SelectorType.XPATH)) {
                world.UIJourneySteps.searchAndViewApplication();
            } else {
                world.APIJourneySteps.createAdminUser();
                world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
                world.UIJourneySteps.searchAndViewApplication();
            }
        });
        When("^I create an interim application with a start and no end date$", () -> {
            clickByLinkText("add interim");
            findSelectAllRadioButtonsByValue("Y");
            InterimPage.startDate(10, 8, 2017);
            InterimPage.enterInterimDetail("Interim with no dates");
            InterimPage.vehicleAuthority(world.createLicence.getNoOfVehiclesRequired());
            InterimPage.trailerAuthority(world.createLicence.getNoOfVehiclesRequired());
        });
        And("^i create a variation in internal$", () -> {
            waitAndClick("//*[@id='menu-licence-quick-actions-create-variation']",SelectorType.XPATH);
            waitForTextToBePresent("Applying to change a licence");
            waitAndClick("//*[contains(text(),'No')]", SelectorType.XPATH);
            waitAndClick("//*[contains(text(),'Phone')]", SelectorType.XPATH);
            waitAndClick("form-actions[submit]",SelectorType.ID);
            waitForTextToBePresent("Variation details");
        });
    }
}