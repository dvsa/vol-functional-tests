package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.number.Int;
import apiCalls.Utils.builders.VehiclesBuilder;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.Journeys.DVLAJourneySteps;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import java.net.MalformedURLException;

import static org.junit.Assert.assertTrue;

public class OldVehicleDetailsJourney extends BasePage {

    World world;

    public OldVehicleDetailsJourney(World world) {
        this.world = world;
    }

    @When("^i am on the vehicle details page$")
    public void iAmOnTheVehicleDetailsPage() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Vehicles");
    };

    @When("i add a vehicle to my licence")
    public void iAddAVehicleToMyLicence() throws MalformedURLException, IllegalBrowserException {
        click("add", SelectorType.ID);
        waitForTitleToBePresent("Add vehicle");
        String VRM = VehiclesBuilder.generateRandomVRM();
        int weight = Int.random(1000, 9999);
        enterText("//*[@name='data[vrm]']", VRM, SelectorType.XPATH);
        enterText("//*[@name='data[platedWeight]']", weight, SelectorType.XPATH);
        click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
    }

    @When("i remove a vehicle from my licence")
    public void iRemoveAVehicleFromMyLicence() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.VRM = getAttribute("//input[@type='submit' and contains(@name,'table[action][edit]')]", SelectorType.XPATH, "value");
        click("//*[contains(@name,'table[action][delete]')]", SelectorType.NAME);
        waitForTextToBePresent("Are you sure you want to remove these vehicle(s)?");
        click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
    }

    @When("i change a vehicle on my licence")
    public void iChangeAVehicleOnMyLicence() {
    }

    @Then("the {string} alert should appear")
    public void theAlertShouldAppear(String alertText) {
        assertTrue(isTextPresent("The vehicle has been added", 10));
    }
}
