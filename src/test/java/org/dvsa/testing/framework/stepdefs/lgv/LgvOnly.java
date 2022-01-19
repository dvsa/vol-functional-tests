package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import com.amazonaws.services.dynamodbv2.xspec.S;
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
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LgvOnly extends BasePage {
    World world;

    public LgvOnly(World world){
        this.world = world;
    }

    public String greatBritain = "//input[@id='type-of-licence[operator-location]']";
    public String northernIreland = "//input[@name='type-of-licence[operator-location]'][@value='Y']";
    public static String lgvDeclarationCheckbox = "//input[@id='lgv-declaration-confirmation']";
    String[] expectedStandardNationalOrMixedFleetStatusArray = new String[]{
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
        UIJourney.inputLicenceAndVehicleType(licenceType, vehicleType, lgvUndertaking);
    }

    @Given("I {string} the LGV undertaking declaration checkbox")
    public void iCheckTheLGVUndertakingDeclaration(String checkBoxAction) {
        if (checkBoxAction.equals("select")){
            clickByXPath(lgvDeclarationCheckbox);
        }
    }

    @Given("I update the vehicle type on the licence to {string} {string} {string}")
    public void iUpdateVehicleTypeOnLicence(String newLicenceType, String newVehicleType, String newLgvUndertaking) {
        clickByLinkText("Type of licence");
        UIJourney.inputLicenceAndVehicleType(newLicenceType, newVehicleType, newLgvUndertaking);
        UIJourney.clickSaveAndContinue();
    }

    @When("I click save and continue")
    public void iClickSaveAndContinue() {
        UIJourney.clickSaveAndContinue();
    }

    @Then("A LGV only error message should be displayed")
    public void lgvOnlyErrorMessage() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        assertTrue(isElementPresent("//a[contains(text(),'Will you only be operating Light goods vehicles on this licence?')]", SelectorType.XPATH));
    }

    @Then("A LGV undertakings error message should be displayed")
    public void lgvUndertakingsErrorMessage() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        assertTrue(isTextPresent("You must confirm you have read and agree to the undertaking to apply for this licence type."));
    }

    @Then("A change licence type warning message is displayed")
    public void changeLicenceWarningMessage() {
        assertTrue(isTextPresent("Are you sure you want to make this change?"));
    }

    @Then("there is no reference of trailers on the safety and compliance page")
    public void thereIsNoReferenceOfTrailersOnTheSafetyAndCompliancePage() {
        assertTrue(isTextPresent("Maximum number of weeks before safety inspections on your vehicles"));
        assertTrue(isElementPresent("licence[safetyInsVehicles]", SelectorType.ID));

        assertFalse(isTextPresent("Maximum number of weeks before safety inspections on your trailers"));
        assertFalse(isElementPresent("licence[safetyInsTrailers]", SelectorType.ID));

        assertTrue(isTextPresent("Are some of your vehicles inspected more often than this"));
        assertTrue(isTextPresent("This can sometimes be the case with older vehicles"));
        assertFalse(isTextPresent("Are some of your vehicles or trailers inspected more often than this"));
        List<String> actualRadioButtons = findAll("licence[safetyInsVaries]", SelectorType.NAME).stream().map(x -> x.getAttribute("value")).collect(Collectors.toList());
        String[] expectedRadioButtons = new String[] {"Y", "N"};
        for (int i = 0; i < actualRadioButtons.size(); i++) {
            assertTrue(actualRadioButtons.get(i).equals(expectedRadioButtons[i]));
        }
        assertTrue(isTextPresent("Yes"));
        assertTrue(isTextPresent("No"));

        assertTrue(isTextPresent("How do you analyse the information from your digital tachographs"));
        actualRadioButtons = findAll("licence[tachographIns]", SelectorType.NAME).stream().map(x -> x.getAttribute("value")).collect(Collectors.toList());
        expectedRadioButtons = new String[] {"tach_internal", "tach_external", "tach_na"};
        for (int i=0; i < actualRadioButtons.size(); i++) {
            assertTrue(actualRadioButtons.get(i).equals(expectedRadioButtons[i]));
        }
        assertTrue(isTextPresent("In-house, using software on your own PC"));
        assertTrue(isTextPresent("By sending to an external analysis bureau/company"));
        assertTrue(isTextPresent("Not applicable"));
    }

    @Then("there is trailer related information on the safety and compliance page")
    public void thereIsTrailerRelatedInformationOnTheSafetyAndCompliancePage() {
        assertTrue(isTextPresent("Maximum number of weeks before safety inspections on your vehicles"));
        assertTrue(isElementPresent("licence[safetyInsVehicles]", SelectorType.ID));

        assertTrue(isTextPresent("Maximum number of weeks before safety inspections on your trailers"));
        assertTrue(isElementPresent("licence[safetyInsTrailers]", SelectorType.ID));

        assertTrue(isTextPresent("Are some of your vehicles or trailers inspected more often than this"));
        assertTrue(isTextPresent("This can sometimes be the case with older vehicles"));
        assertFalse(isTextPresent("Are some of your vehicles inspected more often than this"));
        List<String> actualRadioButtons = findAll("licence[safetyInsVaries]", SelectorType.NAME).stream().map(x -> x.getAttribute("value")).collect(Collectors.toList());
        String[] expectedRadioButtons = new String[] {"Y", "N"};
        for (int i = 0; i < actualRadioButtons.size(); i++) {
            assertTrue(actualRadioButtons.get(i).equals(expectedRadioButtons[i]));
        }
        assertTrue(isTextPresent("Yes"));
        assertTrue(isTextPresent("No"));

        assertTrue(isTextPresent("How do you analyse the information from your digital tachographs"));
        actualRadioButtons = findAll("licence[tachographIns]", SelectorType.NAME).stream().map(x -> x.getAttribute("value")).collect(Collectors.toList());
        expectedRadioButtons = new String[] {"tach_internal", "tach_external", "tach_na"};
        for (int i=0; i < actualRadioButtons.size(); i++) {
            assertTrue(actualRadioButtons.get(i).equals(expectedRadioButtons[i]));
        }
        assertTrue(isTextPresent("In-house, using software on your own PC"));
        assertTrue(isTextPresent("By sending to an external analysis bureau/company"));
        assertTrue(isTextPresent("Not applicable"));
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
                Assert.assertEquals(expectedStandardNationalOrMixedFleetStatusArray[i], applicationOverviewStatusElements.get(i).getText());
            }
        }
    }
}
