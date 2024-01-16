package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.number.Int;
import apiCalls.Utils.volBuilders.VehiclesBuilder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.*;

public class OldVehicleDetailsJourney extends BasePage {

    World world;

    //deprecated
    public OldVehicleDetailsJourney(World world) {
        this.world = world;
    }

    String firstVehicleInTable = "//input[@type='submit' and contains(@name,'table[action][edit]')]";
    String firstVehicleRemoveButton = "//*[contains(@name,'table[action][delete]')]";
    String firstVehicleCheckbox = "//tbody//input[@type='checkbox']";
    String VRMField = "//*[@name='data[vrm]']";
    String weightField = "//*[@name='data[platedWeight]']";
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
    public void iAmOnTheVehicleDetailsPage() {
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.VEHICLES);
    }

    @When("i add a vehicle to my application")
    public void iAddAVehicleToMyApplication() {
        click("add", SelectorType.ID);
        waitForTitleToBePresent("Add vehicle");
        enterText(VRMField, SelectorType.XPATH, generatedVRM);
        enterText(weightField, SelectorType.XPATH, generatedWeight);
        world.UIJourney.clickSubmit();
    }

    @Then("the vehicle should be appear")
    public void theVehicleShouldBeAppear() {
        String valueOfFirstVRMInTable = getText("button.action-button-link:first-of-type", SelectorType.CSS).trim();
        assertEquals(generatedVRM, valueOfFirstVRMInTable);
    }

    @When("i remove a vehicle from my application")
    public void iRemoveAVehicleFromApplication() {
        firstVRM = getText("button.action-button-link:first-of-type", SelectorType.CSS).trim();
        click(firstVehicleRemoveButton, SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to remove these vehicle(s)?");
        world.UIJourney.clickSubmit();
    }

    @Then("the vehicle no longer appears")
    public void theVehicleNoLongerAppears() {
        waitForTitleToBePresent("Vehicle details");
        assertFalse(findElements("//tr",SelectorType.XPATH).get(0).getText().contains(firstVRM));
    }

    @When("i change a vehicle on my licence")
    public void iChangeAVehicleOnMyLicence() {
        firstVRM = getAttribute(firstVehicleInTable, SelectorType.XPATH, "value");
        click(firstVehicleInTable, SelectorType.XPATH);
        replaceText(weightField, SelectorType.XPATH, generatedWeight);
        world.UIJourney.clickSubmit();
    }

    @Then("the vehicle should have changed")
    public void theVehicleShouldHaveChanged() {
        String weightOfSelectedVRM = String.format("//*[contains(@value,'%s')]/../../td[2]", firstVRM);
        String firstWeightInTable = getText(weightOfSelectedVRM, SelectorType.XPATH);
        assertEquals(generatedWeight.concat(" kg"), firstWeightInTable);
    }

    @When("i transfer a vehicle to another licence")
    public void iTransferAVehicleToAnotherLicence() {
        firstVRM = getAttribute(firstVehicleInTable, SelectorType.XPATH, "value");
        click(firstVehicleCheckbox, SelectorType.XPATH);
        click(moreActionsButton, SelectorType.XPATH);
        click(transferButton, SelectorType.XPATH);
        transferLicence = getText(firstLicenceInTransferDropdown, SelectorType.XPATH);
        selectValueFromDropDown(transferDropdown, SelectorType.XPATH, transferLicence);
        world.UIJourney.clickSubmit();
    }

    @And("the other licence contains that vehicle")
    public void theOtherLicenceContainsThatVehicle() {
        clickByLinkText("GOV.UK");
        waitForTextToBePresent("You must keep your records up to date");
        clickByLinkText(transferLicence);
        clickByLinkText("Vehicles");
        String valueOfFirstVRMInTable = getAttribute(firstVehicleInTable, SelectorType.XPATH, "value");
        assertEquals(firstVRM, valueOfFirstVRMInTable);
    }


    //deprecated
    @Then("the {string} alert should appear")
    public void theAlertShouldAppear(String alertText) {
        assertTrue(isTextPresent(alertText));
    }
}