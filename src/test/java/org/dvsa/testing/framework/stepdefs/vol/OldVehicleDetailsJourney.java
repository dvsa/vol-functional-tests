package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.number.Int;
import apiCalls.Utils.volBuilders.VehiclesBuilder;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

public class OldVehicleDetailsJourney extends BasePage {

    World world;

    public OldVehicleDetailsJourney(World world) {
        this.world = world;
    }

    String firstVehicleInTable = "//input[@type='submit' and contains(@name,'table[action][edit]')]";
    String firstVehicleRemoveButton = "//*[contains(@name,'table[action][delete]')]";
    String firstVehicleCheckbox = "//tbody//input[@type='checkbox']";
    String VRMField = "//*[@name='data[vrm]']";
    String weightField = "//*[@name='data[platedWeight]']";
    String submitButton = "//*[@name='form-actions[submit]']";
    String moreActionsButton = "//*[@id='more-actions__button__container']";
    String transferButton = "//*[@id='transfer']";
    String transferDropdown = "//*[@id='data[licence]']";
    String transferSubmitButton = "//*[@name='form-actions[transfer]']";
    String firstLicenceInTransferDropdown = "//*[@id='data[licence]']/option[2]";
    String generatedWeight = String.valueOf(Int.random(1000, 9999));
    String generatedVRM = VehiclesBuilder.generateRandomVRM();
    String firstVRM;
    String transferLicence;

    @When("^i am on the vehicle details page$")
    public void iAmOnTheVehicleDetailsPage() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Vehicles");
    };

    @When("i add a vehicle to my licence")
    public void iAddAVehicleToMyLicence() throws MalformedURLException, IllegalBrowserException {
        click("add", SelectorType.ID);
        waitForTitleToBePresent("Add vehicle");
        enterText(VRMField, generatedVRM, SelectorType.XPATH);
        enterText(weightField, generatedWeight, SelectorType.XPATH);
        click(submitButton, SelectorType.XPATH);
    }

    @Then("the vehicle should be appear")
    public void theVehicleShouldBeAppear() throws MalformedURLException, IllegalBrowserException {
        String valueOfFirstVRMInTable = getAttribute(firstVehicleInTable, SelectorType.XPATH, "value");
        assertEquals(generatedVRM, valueOfFirstVRMInTable);
    }

    @When("i remove a vehicle from my licence")
    public void iRemoveAVehicleFromMyLicence() throws MalformedURLException, IllegalBrowserException {
        firstVRM = getAttribute(firstVehicleInTable, SelectorType.XPATH, "value");
        click(firstVehicleRemoveButton, SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to remove these vehicle(s)?");
        click(submitButton, SelectorType.XPATH);
    }

    @Then("the vehicle no longer appears")
    public void theVehicleNoLongerAppears() {
        assertFalse(isTextPresent(firstVRM, 10));
    }

    @When("i change a vehicle on my licence")
    public void iChangeAVehicleOnMyLicence() throws MalformedURLException, IllegalBrowserException {
        firstVRM = getAttribute(firstVehicleInTable, SelectorType.XPATH, "value");
        click(firstVehicleInTable, SelectorType.XPATH);
        replaceText(weightField, SelectorType.XPATH, generatedWeight);
        click(submitButton, SelectorType.XPATH);
    }

    @Then("the vehicle should have changed")
    public void theVehicleShouldHaveChanged() throws MalformedURLException, IllegalBrowserException {
        String weightOfSelectedVRM = String.format("//*[contains(@value,'%s')]/../../td[2]", firstVRM);
        String firstWeightInTable = getText(weightOfSelectedVRM, SelectorType.XPATH);
        assertEquals(generatedWeight.concat(" kg"), firstWeightInTable);
    }

    @When("i transfer a vehicle to another licence")
    public void iTransferAVehicleToAnotherLicence() throws MalformedURLException, IllegalBrowserException {
        firstVRM = getAttribute(firstVehicleInTable, SelectorType.XPATH, "value");
        click(firstVehicleCheckbox, SelectorType.XPATH);
        click(moreActionsButton, SelectorType.XPATH);
        click(transferButton, SelectorType.XPATH);
        transferLicence = getText(firstLicenceInTransferDropdown, SelectorType.XPATH);
        selectValueFromDropDown(transferDropdown, SelectorType.XPATH, transferLicence);
        click(transferSubmitButton, SelectorType.XPATH);
    }

    @And("the other licence contains that vehicle")
    public void theOtherLicenceContainsThatVehicle() throws MalformedURLException, IllegalBrowserException {
        clickByLinkText("GOV.UK");
        waitForTextToBePresent("You must keep your records up to date");
        clickByLinkText(transferLicence);
        clickByLinkText("Vehicles");
        String valueOfFirstVRMInTable = getAttribute(firstVehicleInTable, SelectorType.XPATH, "value");
        assertEquals(firstVRM, valueOfFirstVRMInTable);
    }

    @Then("the {string} alert should appear")
    public void theAlertShouldAppear(String alertText) {
        assertTrue(isTextPresent(alertText, 10));
    }
}
