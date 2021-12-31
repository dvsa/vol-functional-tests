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
import org.openqa.selenium.WebElement;
import java.util.List;

public class LgvOnly extends BasePage {
    World world;

    public LgvOnly(World world){
        this.world = world;
    }

    public String greatBritain = "//input[@id='type-of-licence[operator-location]']";
    public String northernIreland = "//input[@name='type-of-licence[operator-location]'][@value='Y']";
    public String lgvDeclarationCheckbox = "//input[@id='lgv-declaration-confirmation']";
    String[] expectedNonLgvOnlyStatusArray = new String[]{
            "Type of licence\nCOMPLETE",
            "Business type\nNOT STARTED",
            "Business details\nCAN'T START YET",
            "Addresses\nCAN'T START YET",
            "Directors\nCAN'T START YET",
            "Operating centres and authorisation\nNOT STARTED",
            "Financial evidence\nCAN'T START YET",
            "Transport Managers\nCAN'T START YET",
            "Vehicles\nCAN'T START YET",
            "Safety and compliance\nNOT STARTED",
            "Financial history\nNOT STARTED",
            "Licence history\nNOT STARTED",
            "Convictions and penalties\nNOT STARTED",
            "Review and declarations\nCAN'T START YET"};
    String[] expectedLgvOnlyStatusArray = new String[]{
            "Type of licence\nCOMPLETE",
            "Business type\nNOT STARTED",
            "Business details\nCAN'T START YET",
            "Addresses\nCAN'T START YET",
            "Directors\nCAN'T START YET",
            "Licence authorisation\nNOT STARTED",
            "Financial evidence\nCAN'T START YET",
            "Transport Managers\nCAN'T START YET",
            "Vehicles\nCAN'T START YET",
            "Safety and compliance\nNOT STARTED",
            "Financial history\nNOT STARTED",
            "Licence history\nNOT STARTED",
            "Convictions and penalties\nNOT STARTED",
            "Review and declarations\nCAN'T START YET"};

    @Given("I am applying for a {string} {string} {string} {string} {string} licence")
    public void iWantToApplyForALicence(String licenceWhere, String operatorType, String licenceType, String vehicleType, String lgvUndertaking) {
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
                clickByXPath("//input[@value='standard_international']");
                clickByXPath("//input[@value='" + VehicleType.valueOf(vehicleType.toUpperCase()).asString() + "']");
                if (lgvUndertaking.equals("checked")) {
                    clickByXPath(lgvDeclarationCheckbox);
                }
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
    public void iUpdateVehicleTypeOnLicence(String newType) {
        clickByLinkText("Type of licence");
        if (newType.equals("mixed_fleet") || newType.equals("lgv_only_fleet")) {
            clickByXPath("//input[@value='" + VehicleType.valueOf(newType.toUpperCase()).asString() + "']");
            if (newType.equals("lgv_only_fleet")) {
                clickByXPath(lgvDeclarationCheckbox);
            }
        } else {
            clickByXPath("//input[@value='" + LicenceType.valueOf(newType.toUpperCase()).asString() + "']");
            if (newType.equals("standard_international")) {
            if (!"no_selection".equals(newType)) {
                clickByXPath("//input[@value='" + VehicleType.valueOf(newType.toUpperCase()).asString() + "']");
                }
            }
        }
        UIJourney.clickSaveAndContinue();
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

    @Then("A change licence type warning message is displayed")
    public void changeLicenceWarningMessage() {
        Assert.assertTrue(isTextPresent("Are you sure you want to make this change?"));
    }

    @When("I confirm the warning message")
    public void iConfirmWarningMessage() {
        waitAndClick("form-actions[submit]", SelectorType.NAME);
    }

    @When("each section on the application overview page has the correct status for the {string} licence")
    public void changeLicenceTypeOverviewSectionsStatus(String newType) {
        Assert.assertTrue(isTextPresent("Apply for a new licence"));
        List<WebElement> applicationOverviewStatusElements = findElements("//ol[@class='overview__list']/li", SelectorType.XPATH);

        if (newType.equals("lgv_only_fleet")) {
            for (int i = 0; i < applicationOverviewStatusElements.size(); i++) {
                Assert.assertEquals(expectedLgvOnlyStatusArray[i], applicationOverviewStatusElements.get(i).getText());
            }
        } else {
            for (int i = 0; i < applicationOverviewStatusElements.size(); i++) {
                Assert.assertEquals(expectedNonLgvOnlyStatusArray[i], applicationOverviewStatusElements.get(i).getText());
            }
        }
    }
}
