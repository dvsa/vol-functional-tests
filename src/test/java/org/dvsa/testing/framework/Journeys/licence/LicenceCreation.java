package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import apiCalls.enums.*;
import org.openqa.selenium.InvalidArgumentException;

import java.util.Locale;

public class LicenceCreation {

    private World world;

    public LicenceCreation(World world) {
        this.world = world;
    }

    public void createApplication(String operatorType, String licenceType) {
        world.createApplication.setOperatorType(operatorType);
        world.createApplication.setLicenceType(licenceType);
        if (licenceType.equals(LicenceType.SPECIAL_RESTRICTED.name().toLowerCase(Locale.ROOT))) {
            world.APIJourney.createSpecialRestrictedApplication();
        } else {
            world.APIJourney.createApplication();
        }
    }

    public void createApplicationWithVehicles(String operatorType, String licenceType, String vehicles) {
        if(licenceType.equals("special_restricted") && Integer.parseInt(vehicles) > 2){
            throw new InvalidArgumentException("Special restricted licences can not have more than 2 vehicles on them.");
        }
        world.createApplication.setOperatingCentreVehicleCap(Integer.parseInt(vehicles));
        world.createApplication.setNoOfVehiclesRequested(Integer.parseInt(vehicles));
        createApplication(operatorType, licenceType);
    }

    public void createApplicationWithTrafficArea(String operatorType, String licenceType, TrafficArea trafficArea) {
        world.createApplication.setTrafficArea(trafficArea);
        world.createApplication.setEnforcementArea(EnforcementArea.valueOf(trafficArea.name()));
        createApplication(operatorType, licenceType);
    }

    public void createSubmittedApplication(String operatorType, String licenceType) {
        createApplication(operatorType, licenceType);
        world.APIJourney.submitApplication();
    }

    public void createSubmittedApplicationWithVehicles(String operatorType, String licenceType, String vehicles) {
        createApplicationWithVehicles(operatorType, licenceType, vehicles);
        world.APIJourney.submitApplication();
    }

    public void createLicence(String operatorType, String licenceType) {
        createSubmittedApplication(operatorType, licenceType);
        world.APIJourney.grantLicenceAndPayFees();
    }

    public void createLicenceWithVehicles(String operatorType, String licenceType, String vehicles) {
        createSubmittedApplicationWithVehicles(operatorType, licenceType, vehicles);
        world.APIJourney.grantLicenceAndPayFees();
    }

    public void createLicenceWithTrafficArea(String operatorType, String licenceType, TrafficArea trafficArea) {
        world.createApplication.setTrafficArea(trafficArea);
        world.createApplication.setEnforcementArea(EnforcementArea.valueOf(trafficArea.name()));
        createLicence(operatorType, licenceType);
    }

    public void createNILicence(String operatorType, String licenceType) {
        world.createApplication.setNiFlag("Y");
        createLicence(operatorType, licenceType);
    }

    public boolean isGoodsLicence() {
        return world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString());
    }

    public boolean isPSVLicence() {
        return world.createApplication.getOperatorType().equals(OperatorType.PUBLIC.asString());
    }
}
