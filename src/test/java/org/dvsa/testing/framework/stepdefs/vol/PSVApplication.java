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

import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @And("i complete the Small vehicles conditions page")
    public void iCompleteTheSmallVehiclesConditionsPage() {
        waitForTitleToBePresent("Small vehicle conditions and undertakings");
        assertTrue(world.psvJourney.areSmallVehicleConditionsPresent());
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

    @Then("the completed Small vehicle sections should be marked {string}")
    public void theCompletedSmallVehicleSectionsShouldBeMarked(String status) {
        if (status.equals("Updated")) {
            waitForTitleToBePresent("Apply to change a licence");
        } else if (status.equals("Complete")) {
            waitForTitleToBePresent("Apply for a new licence");
        }
        assertTrue(world.psvJourney.vehicleSizeStatus(status));
        assertTrue(world.psvJourney.smallVehiclesConditionsStatus(status));
        assertTrue(world.psvJourney.documentaryEvidenceSmallVehiclesStatus(status));
        assertTrue(world.psvJourney.limousinesStatus(status));
    }

    @And("i complete the Vehicles with nine seats or more page")
    public void iCompleteTheVehiclesWithNineSeatsOrMorePage() {
        waitForTitleToBePresent("Vehicles with nine seats or more");
        world.psvJourney.completeVehiclesWith9SeatsOrMorePage();
    }

    @And("i complete the Limousines page selecting {string}")
    public void iCompleteTheLimousinesPageSelectingYes(String limousines) {
        waitForTitleToBePresent("Limousines and novelty vehicles");
        world.psvJourney.completeLimousinesVehicles(limousines);
    }

    @Then("the completed 9 vehicles and above sections should be marked {string}")
    public void theCompletedVehiclesAndAboveSectionsShouldBeMarkedUpdated(String status) {
        if (status.equals("Updated")) {
            waitForTitleToBePresent("Apply to change a licence");
        } else if (status.equals("Complete")) {
            waitForTitleToBePresent("Apply for a new licence");
        }
        assertTrue(world.psvJourney.vehicleSizeStatus(status));
        assertTrue(world.psvJourney.vehicles9SeatsOrMoreStatus(status));
        assertTrue(world.psvJourney.limousinesStatus(status));
    }

    @And("i select operating centres and add a PSV operating centre for {string} vehicles")
    public void iSelectOperatingCentresAndAddAPSVOperatingCentreForVehicles(String numberOfVehicles) {
        world.operatingCentreJourney.selectOperatingCentreAndAuthorisationSection();
        world.operatingCentreJourney.addPsvOperatingCentre(numberOfVehicles);
        world.operatingCentreJourney.savePsvAuthorisation(numberOfVehicles);
    }

    @And("i select Vehicle Size {string}")
    public void iSelectVehicleSize(String vehicleSize) {
        waitForTitleToBePresent("Vehicles size");
        world.psvJourney.selectVehicleSize(vehicleSize);
    }

    @And("i answer {string} to the Operating small vehicles question")
    public void iAnswerToTheOperatingSmallVehiclesQuestion(String answer) {
        waitForTitleToBePresent("Operating small vehicles");
        world.psvJourney.answerOperatingSmallVehiclesQuestion(answer);
    }

    @And("i complete the Small vehicles conditions page after answering Yes")
    public void iCompleteTheSmallVehiclesConditionsPageAfterAnsweringYes() {
        waitForTitleToBePresent("Small vehicle conditions and undertakings");
        assertFalse(world.psvJourney.areSmallVehicleConditionsPresent());
        world.psvJourney.completeSmallVehicleConditionsPage();
    }

    @And("i complete Written explanation small vehicles")
    public void iCompleteWrittenExplanationSmallVehicles() {
        waitForTitleToBePresent("Written explanation (small vehicles)");
        world.psvJourney.completeWrittenExplanationSmallVehiclesPage();
    }

    @And("i complete the Documentary evidence - main occupation page")
    public void iCompleteTheDocumentaryEvidenceMainOccupationPage() {
        waitForTitleToBePresent("Documentary evidence - main occupation");
        world.psvJourney.completeDocumentaryEvidenceMainOccupationPage();
    }

    @And("i complete the Main occupation undertakings page")
    public void iCompleteTheMainOccupationUndertakingsPage() {
        waitForTitleToBePresent("Main occupation undertakings");
        world.psvJourney.completeMainOccupationUndertakingsPage();
    }

    @Then("the completed Both Yes sections should be marked {string}")
    public void theCompletedBothYesSectionsShouldBeMarkedComplete(String status) {
        if (status.equals("Updated")) {
            waitForTitleToBePresent("Apply to change a licence");
        } else if (status.equals("Complete")) {
            waitForTitleToBePresent("Apply for a new licence");
        }
        assertTrue(world.psvJourney.vehicleSizeStatus(status));
        assertTrue(world.psvJourney.smallVehiclesStatus(status));
        assertTrue(world.psvJourney.writtenExplanationSmallVehiclesStatus(status));
        assertTrue(world.psvJourney.documentaryEvidenceMainOccupationStatus(status));
        assertTrue(world.psvJourney.mainOccupationUndertakingsStatus(status));
        assertTrue(world.psvJourney.limousinesStatus(status));
    }
}
