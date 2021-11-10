package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Given;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import apiCalls.enums.*;
import org.junit.Assert;

public class LgvOnly extends BasePage {
    World world;

    public LgvOnly(World world){
        this.world = world;
    }

    public String greatBritain = "//input[@id='type-of-licence[operator-location]']";
    public String northernIreland = "//input[@name='type-of-licence[operator-location]'][@value='Y']";
    public String lgvDeclarationCheckbox = "//input[@id='lgv-declaration-confirmation']";

    @Given("I am applying for a {string} {string} {string} {string} licence")
    public void iWantToApplyForALicence(String licenceWhere, String operatorType, String licenceType, String vehicleType) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        clickByLinkText("Apply for a new licence");
        if (licenceWhere.equals("great_britain")) clickByXPath(greatBritain); else clickByXPath(northernIreland);
        if (licenceWhere.equals("great_britain")){
            clickByXPath("//input[@value='" + OperatorType.valueOf(operatorType.toUpperCase()).asString() + "']");
        }
        clickByXPath("//input[@value='" + LicenceType.valueOf(licenceType.toUpperCase()).asString() + "']");
        if (licenceType.equals("standard_international")){
            if (!"no_selection".equals(vehicleType)){
                clickByXPath("//input[@value='" + VehicleType.valueOf(vehicleType.toUpperCase()).asString() + "']");
            }
        }
    }

    @Given("I {string} the LGV undertaking declaration checkbox")
    public void iCheckTheLGVUndertakingDeclaration(String checkBoxAction) {
        if (checkBoxAction.equals("select")){
            clickByXPath(lgvDeclarationCheckbox);
        }
    }

    @Given("I update the vehicle type on the licence to {string}")
    public void iUpdateVehicleTypeOnLicence(String vehicleType) {
        clickByLinkText("Type of licence");
        clickByXPath("//input[@value='" + VehicleType.valueOf(vehicleType.toUpperCase()).asString() + "']");
        if (vehicleType.equals("lgv_only_fleet")){
            clickByXPath(lgvDeclarationCheckbox);
        }
    }

    @When("I click save and continue")
    public void iClickSaveAndContinue() {
        UIJourney.clickSaveAndContinue();
    }

    @Then("A LGV only error message should be displayed")
    public void lgvOnlyErrorMessage() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        Assert.assertTrue(isElementPresent("//a[contains(text(),'Will you only be operating Light goods vehicles on this licence?')]", SelectorType.XPATH));
    }

    @Then("A LGV undertakings error message should be displayed")
    public void lgvUndertakingsErrorMessage() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        Assert.assertTrue(isTextPresent("You must confirm you have read and agree to the undertaking to apply for this licence type."));
    }

    @Then("A change licence type warning message should be displayed")
    public void changeLicenceWarningMessage() {
        Assert.assertTrue(isTextPresent("Are you sure you want to make this change?"));
    }
}

