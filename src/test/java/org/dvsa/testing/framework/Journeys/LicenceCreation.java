package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import apiCalls.enums.*;

public class LicenceCreation {

    private World world;

    public LicenceCreation(World world) {
        this.world = world;
    }

    public void createApplication(String operatorType, String licenceType) {
        world.createApplication.setOperatorType(operatorType);
        world.createApplication.setLicenceType(licenceType);
        if (licenceType.equals("special_restricted") && (world.createApplication.getApplicationId() == null)) {
            world.APIJourneySteps.createSpecialRestrictedApplication();
        } else if (world.createApplication.getApplicationId() == null) {
            world.APIJourneySteps.createApplication();
        }
    }

    public void createApplicationWithVehicles(String operatorType, String licenceType, String vehicles) {
        if(licenceType.equals("special_restricted")){
            vehicles = "2";
        }
        world.createApplication.setOperatingCentreVehicleCap(Integer.parseInt(vehicles));
        world.createApplication.setNoOfVehiclesRequested(Integer.parseInt(vehicles));
        createApplication(operatorType, licenceType);
    }

    public void createSubmittedApplication(String operatorType, String licenceType) {
        createApplication(operatorType, licenceType);
        world.APIJourneySteps.submitApplication();
    }

    public void createSubmittedApplicationWithVehicles(String operatorType, String licenceType, String vehicles) {
        createApplicationWithVehicles(operatorType, licenceType, vehicles);
        world.APIJourneySteps.submitApplication();
    }

    public void createLicence(String operatorType, String licenceType) {
        createSubmittedApplication(operatorType, licenceType);
        world.APIJourneySteps.grantLicenceAndPayFees();
    }

    public void createLicenceWithVehicles(String operatorType, String licenceType, String vehicles) {
        createSubmittedApplicationWithVehicles(operatorType, licenceType, vehicles);
        world.APIJourneySteps.grantLicenceAndPayFees();
    }

    public void createLicenceWithTrafficArea(String operatorType, String licenceType, TrafficArea trafficArea) {
        world.createApplication.setTrafficArea(trafficArea);
        world.createApplication.setEnforcementArea(EnforcementArea.valueOf(trafficArea.name()));
        createLicence(operatorType, licenceType);
    }
}
