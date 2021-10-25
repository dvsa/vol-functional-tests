package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import activesupport.number.Int;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Given;
import org.dvsa.testing.framework.Journeys.licence.objects.FinancialStandingRate;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
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
        WebElement Where = getDriver().findElement(By.id("type-of-licence[operator-location]"));
        Where.click();
        clickByLinkText("Great Britain");
        findSelectAllRadioButtonsByValue("Great Britain");
        findSelectAllRadioButtonsByValue(operatorType);
        findSelectAllRadioButtonsByValue(licenceType);
    }

    @When("I click save and continue")
    public void iClickSaveAndContinue() {
        clickByLinkText("Save and continue");
    }
}
