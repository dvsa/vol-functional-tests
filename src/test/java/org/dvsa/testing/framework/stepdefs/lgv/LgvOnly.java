package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import activesupport.number.Int;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Given;
import org.dvsa.testing.framework.enums.LicenceFleet;
import org.dvsa.testing.framework.enums.LicenceWhere;
import org.dvsa.testing.framework.enums.LicenceVehicles;
import org.dvsa.testing.framework.enums.LicenceType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import apiCalls.enums.*;

public class LgvOnly extends BasePage {
    World world;

    public LgvOnly(World world){
        this.world = world;
    }

    @Given("I am applying for a {string} {string} {string} {string} licence")
    public void iWantToApplyForALicence(String licenceWhere, String licenceVehicles, String licenceType, String licenceFleet) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        clickByLinkText("Apply for a new licence");
        clickByXPath(LicenceWhere.valueOf(licenceWhere.toUpperCase()).toString());
        clickByXPath(LicenceVehicles.valueOf(licenceVehicles.toUpperCase()).toString());
        clickByXPath(LicenceType.valueOf(licenceType.toUpperCase()).toString());
        if (licenceType.equals("Standard_International")){
            if (!"No_selection".equals(licenceFleet)){
                clickByXPath(LicenceFleet.valueOf(licenceFleet.toUpperCase()).toString());
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

