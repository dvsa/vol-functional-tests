package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.internal.InterimPage;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.joda.time.LocalDate;

import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class InterimLicence extends BasePage implements En {

    private static String VehicleErrorMessage = "The interim vehicle authority cannot exceed the total vehicle authority";
    private static String HgvVehicleErrorMessage = "The interim Heavy Goods Vehicle Authority cannot exceed the total Heavy Goods Vehicle Authority";
    private static String LgvVehicleErrorMessage = "The interim Light Goods Vehicle Authority cannot exceed the total Light Goods Vehicle Authority";
    private static String noDatesErrorMessage = "Value is required";
    private World world;

    public InterimLicence(World world) {
        this.world = world;

        When("^I have an interim vehicle authority greater than my application vehicle authority$", () -> {
            InterimPage.addInterimValues();
            InterimPage.vehicleHgvAuthority(world.createApplication.getNoOfAddedHgvVehicles() + 1);
        });

        When("^I have an interim vehicle authority equal to my application vehicle authority$", () -> {
            InterimPage.addInterimValues();
            InterimPage.vehicleHgvAuthority(world.createApplication.getNoOfAddedHgvVehicles());
        });

        When("^I have an interim vehicle authority less than my application vehicle authority$", () -> {
            InterimPage.addInterimValues();
            InterimPage.vehicleHgvAuthority(world.createApplication.getNoOfAddedHgvVehicles() - 1);
        });

        When("^I have an interim vehicle authority with \"([^\"]*)\" hgvs and \"([^\"]*)\" lgvs$", (String intHGVs, String intLGVs) -> {
            InterimPage.addInterimValues();
            InterimPage.vehicleHgvAuthority(Integer.parseInt(intHGVs));
            InterimPage.vehicleLgvAuthority(Integer.parseInt(intLGVs));
        });

        When("^I create an interim application with no start and end dates$", () -> {
            clickByLinkText("add interim");
            findSelectAllRadioButtonsByValue("Y");
            InterimPage.enterInterimDetail("Test Test");
            InterimPage.vehicleHgvAuthority(world.createApplication.getTotalOperatingCentreHgvAuthority());
            InterimPage.trailerAuthority(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        });

        Then("^A \"([^\"]*)\" error appears when i save the interim licence$", (String errorType) -> {
            InterimPage.save();
            String errorMes = (errorType.equals("HGV") ? (HgvVehicleErrorMessage):(LgvVehicleErrorMessage));
            assertTrue(isTextPresent(errorMes));
        });

        Then("^I should get an error when i save the application$", () -> {
            InterimPage.save();
            assertTrue(isTextPresent(VehicleErrorMessage));
        });

        Then("^I should be able to save the application without any errors$", () -> {
            InterimPage.save();
            assertFalse(isTextPresent(VehicleErrorMessage));
            assertFalse(isTextPresent(HgvVehicleErrorMessage));
            assertFalse(isTextPresent(LgvVehicleErrorMessage));
        });

        Then("^I should not error when i save the application$", () -> {
            InterimPage.save();
            assertFalse(isTextPresent(noDatesErrorMessage));
        });

        Then("^I should error when i attempt to grant the application$", () -> {
            InterimPage.save();
            clickByLinkText("Interim details");
            waitForTextToBePresent("Interim application");
            InterimPage.grant();
            isTextPresent(noDatesErrorMessage);
        });
        And("^i have logged in to internal$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
        });
        When("^I create an interim application with a start and no end date$", () -> {
            clickByLinkText("add interim");
            findSelectAllRadioButtonsByValue("Y");
            InterimPage.startDate(10, 8, 2017);
            InterimPage.enterInterimDetail("Interim with no dates");
            InterimPage.vehicleHgvAuthority((world.createApplication.getTotalOperatingCentreHgvAuthority()));
            InterimPage.trailerAuthority(world.createApplication.getTotalOperatingCentreTrailerAuthority());
            // Not sure why this is setting authorities.
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