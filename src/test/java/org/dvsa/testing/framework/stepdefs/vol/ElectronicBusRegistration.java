package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.When;

import java.io.IOException;

public class ElectronicBusRegistration {
    World world;

    public ElectronicBusRegistration(World world) {
        this.world = world;
    }

    @When("I upload an ebsr file with {string} days notice")
    public void iUploadAnEbsrFileWithDaysNotice(String days) throws IOException {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        world.busRegistrationJourney.uploadAndSubmitEBSR("futureDay", Integer.parseInt(days));
    }
}