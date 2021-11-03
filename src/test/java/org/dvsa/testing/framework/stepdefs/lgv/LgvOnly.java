package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Given;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import apiCalls.enums.*;

public class LgvOnly extends BasePage {
    World world;

    public LgvOnly(World world){
        this.world = world;
    }

    public String greatBritain = "//input[@id='type-of-licence[operator-location]']";
    public String northernIreland = "61790589c0c15";

    @Given("I am applying for a {string} {string} {string} {string} licence")
    public void iWantToApplyForALicence(String licenceWhere, String operatorType, String licenceType, String vehicleType) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        clickByLinkText("Apply for a new licence");

        if (licenceWhere.equals("great_britain")) clickByXPath(greatBritain); else clickByXPath(northernIreland);
        clickByXPath(OperatorType.valueOf(operatorType.toUpperCase()).toString());
        clickByXPath(LicenceType.valueOf(licenceType.toUpperCase()).toString());

        if (licenceType.equals("standard_international")){
            if (!"no_selection".equals(vehicleType)){
                clickByXPath(VehicleType.valueOf(vehicleType.toUpperCase()).toString());
            }
        }

    }

    @When("I click save and continue")
    public void iClickSaveAndContinue() {
        UIJourney.clickSaveAndContinue();
    }

    @Then("A LGV only error message should be displayed")
    public void lgvOnlyErrorMessage() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        isTextPresent("Will you only be operating Light goods vehicles on this licence?");
    }

    @Then("A LGV undertakings error message should be displayed")
    public void lgvUndertakingsErrorMessage() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        isTextPresent("You must confirm you have read and agree to the undertaking to apply for this licence type.");
    }
}

