package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.When;
import scanner.AXEScanner;

import java.io.IOException;

public class ElectronicBusRegistration {
    World world;

    public ElectronicBusRegistration(World world) {
        this.world = world;
    }

    @When("I upload an ebsr file with {string} days notice")
    public void iUploadAnEbsrFileWithDaysNotice(String days) throws IllegalBrowserException, IOException {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        world.busRegistrationJourney.uploadAndSubmitEBSR("futureDay", Integer.parseInt(days));
        AXEScanner axeScanner = AccessibilitySteps.scanner;
        axeScanner.scan(true);
    }
}