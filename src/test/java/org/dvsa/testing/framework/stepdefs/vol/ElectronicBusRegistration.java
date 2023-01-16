package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;


public class ElectronicBusRegistration {
    World world;

    public ElectronicBusRegistration(World world) {
        this.world = world;
    }

    @Before
    public void getScenarioName(Scenario scenario) {
        System.out.println("Testing Scenario:" + scenario.getName());
    }

    @Given("I have a psv application with traffic area {string} and enforcement area {string} which has been granted")
    public void iHaveAPsvApplicationWithTrafficAreaAndEnforcementAreaWhichHasBeenGranted(String trafficArea, String enforcementArea) {
        world.APIJourney.generateAndGrantPsvApplicationPerTrafficArea(trafficArea, enforcementArea);
    }

    @When("I upload an ebsr file with {string} days notice")
    public void iUploadAnEbsrFileWithDaysNotice(String days) {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        world.busRegistrationJourney.uploadAndSubmitEBSR("futureDay", Integer.parseInt(days));
    }
}