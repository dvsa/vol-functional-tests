package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;

public class NiApplication {
    private final World world;

    public NiApplication(World world) {
        this.world = world;
        if (world.niApplicationJourney == null) {
            new Initialisation(world);
        }
    }

    @Given("an operator has an outstanding Northern Ireland application")
    public void createOperatorWithOutstandingNiApplication() throws HttpException {
        world.niApplicationJourney.createOperatorWithOutstandingNiApplication();
    }

    @When("the operator starts a {string} {string} application with light goods vehicles only {string} and undertaking confirmation {word}")
    public void startApplication(String region, String licenceType, String vehicleChoice, String confirmUndertaking) {
        world.niApplicationJourney.startApplication(region, licenceType, vehicleChoice, Boolean.parseBoolean(confirmUndertaking));
    }

    @Then("the {string} overview allows reopening the saved {string} {string} application with light goods vehicles only {string} and undertaking confirmation {word}")
    public void verifySavedApplication(String overviewTitle, String region, String licenceType,
                                       String vehicleChoice, String confirmUndertaking) {
        world.niApplicationJourney.verifySavedApplication(overviewTitle, region, licenceType, vehicleChoice,
                Boolean.parseBoolean(confirmUndertaking));
    }

    @Given("an NI goods licence with operating centre postcode {string} has an operating centre variation")
    public void beginNiOperatingCentreVariation(String postcode) throws HttpException {
        world.niApplicationJourney.beginNiOperatingCentreVariation(postcode);
    }

    @When("the operator attempts to add an operating centre outside NI")
    public void attemptOperatingCentreOutsideNi(DataTable details) {
        world.niApplicationJourney.attemptOperatingCentreOutsideNi(details.asMap(String.class, String.class));
    }

    @Then("postcode {string} is rejected with {string} and the operating centres are unchanged")
    public void verifyRejectedOperatingCentre(String postcode, String expectedError) {
        world.niApplicationJourney.verifyRejectedOperatingCentre(postcode, expectedError);
    }
}
