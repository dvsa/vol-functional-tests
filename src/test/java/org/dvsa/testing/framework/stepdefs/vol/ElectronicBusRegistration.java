package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import scanner.AXEScanner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ElectronicBusRegistration extends BasePage {
    World world;
    public ElectronicBusRegistration(World world) {
        this.world = world;
    }

    @When("I upload an ebsr file with {string} days notice")
    public void iUploadAnEbsrFileWithDaysNotice(String days) throws IllegalBrowserException, IOException, InterruptedException {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        world.busRegistrationJourney.uploadAndSubmitEBSR("futureDay", Integer.parseInt(days));
        AXEScanner axeScanner = AccessibilitySteps.scanner;
        axeScanner.scan(true);
    }

    @Then("I should be able to view to bus registration details")
    public void iShouldBeAbleToViewToBusRegistrationDetails() {
        clickByLinkText("EBSR uploads");
        List<String> expectedHeaders = Arrays.asList(
                "Organisation", "Reg. number", "Status", "Variation", "Service numbers", "Uploaded");
        for(String header : expectedHeaders){
            assertTrue(isTextPresent(header));
        }
    }

    @And("I should also be able to view EBSR uploads")
    public void iShouldAlsoBeAbleToViewEBSRUploads() {
        clickByLinkText("Registrations");
        List<String> expectedHeaders = Arrays.asList(
                "Organisation", "Bus registration No.", "Status", "Variation No.", "Service No.", "First registered / cancelled",
                "Starting point", "Finishing point");
        for(String header : expectedHeaders){
            assertTrue(isTextPresent(header));
        }
    }
}