package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import activesupport.number.Int;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Given;
import org.dvsa.testing.framework.Journeys.licence.objects.FinancialStandingRate;
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

    @Given("I want to apply for a {string} {string} licence")
    public void iWantToApplyForALicence(String operatorType, String licenceType) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        clickByLinkText("Apply for a new licence");
        clickByXPath("//input[@id='type-of-licence[operator-location]']");
        clickByXPath("//input[@value='lcat_gv']");
        clickByXPath("//input[@value='ltyp_si']");
    }

    @Given("I am on the LGV only undertakings page for a LGV only licence application")
    public void iAmOnLGVOnlyUndertakingsPageForLGVOnlyLicenceApplication() {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        clickByLinkText("Apply for a new licence");
        clickByXPath("//input[@id='type-of-licence[operator-location]']");
        clickByXPath("//input[@value='lcat_gv']");
        clickByXPath("//input[@value='ltyp_si']");
        clickByXPath("//input[@value='app_veh_type_lgv']");
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

