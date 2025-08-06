package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.UserType;
import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class PSVApplication extends BasePage {
    World world;
    Initialisation initialisation;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public PSVApplication(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("I have applied for a {string} licence")
    public void iHaveAppliedForALicence(String operator, String licenceType) throws HttpException {
        lock.writeLock().lock();
        try {
            world.createApplication.setOperatorType(operator);
            world.createApplication.setLicenceType(licenceType);
            if (licenceType.equals("special_restricted") && (world.createApplication.getApplicationId() == null)) {
                world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
                world.APIJourney.createSpecialRestrictedLicence();
            } else if (world.createApplication.getApplicationId() == null) {
                world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
                world.APIJourney.createApplication();
                world.APIJourney.submitApplication();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have a {string} {string} application which is under consideration")
    public void iHaveAApplicationWhichIsUnderConsideration(String vehicleType, String typeOfLicence) throws HttpException {
        lock.writeLock().lock();
        try {
            world.createApplication.setIsInterim("Y");
            world.createApplication.setOperatorType(vehicleType);
            world.createApplication.setLicenceType(typeOfLicence);
            if (world.createApplication.getApplicationId() == null) {
                world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
                world.APIJourney.createApplication();
                world.APIJourney.submitApplication();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @When("i select the Vehicle size section")
    public void iSelectTheVehicleSizeSection() {
        world.psvJourney.selectVehicleSizeSection();
    }

    @And("i select Small vehicles - less than 9 seats")
    public void iSelectSmallVehiclesLessThan9Seats() {
        waitForTitleToBePresent("Vehicles size");
        world.psvJourney.smallVehiclesLessThan9Seats();
    }

    @And("i complete the Small vehicles conditions page")
    public void iCompleteTheSmallVehiclesConditionsPage() {
        waitForTitleToBePresent("Small vehicle conditions and undertakings");
        world.psvJourney.completeSmallVehicleConditionsPage();
    }

    @And("i complete the Documentary evidence - small vehicles page")
    public void iCompleteTheDocumentaryEvidenceSmallVehiclesPage() {
        waitForTitleToBePresent("Documentary evidence - small vehicles");
        world.psvJourney.completeDocumentaryEvidenceSmallVehiclesPage();
    }

    @And("i complete the Limousines and novelty vehicles on the small vehicles journey")
    public void iCompleteTheLimousinesAndNoveltyVehiclesOnTheSmallVehiclesJourney() {
        waitForTitleToBePresent("Limousines and novelty vehicles");
        world.psvJourney.completeLimousinesSmallVehiclesJourney();
    }

    @Then("the completed Small vehicle sections should be marked Updated")
    public void theCompletedSmallVehicleSectionsShouldBeMarkedUpdated() {
        waitForTitleToBePresent("Apply to change a licence");
        assertTrue(world.psvJourney.isVehicleSizeUpdated());
        assertTrue(world.psvJourney.isSmallVehiclesConditionsUpdated());
        assertTrue(world.psvJourney.isDocumentaryEvidenceSmallVehiclesUpdated());
        assertTrue(world.psvJourney.isLimousinesUpdated());
    }
}
