package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import java.io.IOException;

public class EBSR {
    World world;

    public EBSR(World world){
        this.world = world;
    }
    @Given("I have a psv application with traffic area {string} and enforcement area {string} which has been granted")
    public void iHaveAPsvApplicationWithTrafficAreaAndEnforcementAreaWhichHasBeenGranted(String trafficArea, String enforcementArea) {
        world.APIJourney.generateAndGrantPsvApplicationPerTrafficArea(trafficArea, enforcementArea);
    }

    @When("I upload an ebsr file with {string} days notice")
    public void iUploadAnEbsrFileWithDaysNotice(String days) throws IOException {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        world.busRegistrationJourney.uploadAndSubmitEBSR("futureDay", Integer.parseInt(days));
    }
}