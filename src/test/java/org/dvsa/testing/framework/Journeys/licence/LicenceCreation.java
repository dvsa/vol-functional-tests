package org.dvsa.testing.framework.Journeys.licence;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.*;
import org.openqa.selenium.InvalidArgumentException;

import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LicenceCreation {

    private World world;
    private static final Lock lock = new ReentrantLock();

    public LicenceCreation(World world) {
        this.world = world;
    }

    public void createApplication(String operatorType, String licenceType) throws HttpException {
        lock.lock();
        try {
            world.createApplication.setOperatorType(operatorType);
            world.createApplication.setLicenceType(licenceType);
            if (licenceType.equals(LicenceType.SPECIAL_RESTRICTED.name().toLowerCase(Locale.ROOT))) {
                world.APIJourney.createSpecialRestrictedApplication();
            } else {
                world.APIJourney.createApplication();
            }
        } finally {
            lock.unlock();
        }
    }

    public void createApplicationWithVehicles(String operatorType, String licenceType, String vehicles) throws HttpException {
        lock.lock();
        try {
            if (operatorType.equals("public") && licenceType.equals("restricted") && Integer.parseInt(vehicles) > 2) {
                throw new InvalidArgumentException("Special restricted licences cannot have more than 2 vehicles on them.");
            }
            if (operatorType.equals("public") && licenceType.equals("restricted")) {
                world.createApplication.setRestrictedVehicles(Integer.parseInt(vehicles));
            } else {
                world.createApplication.setNoOfOperatingCentreVehicleAuthorised(Integer.parseInt(vehicles));
                world.createApplication.setNoOfAddedHgvVehicles(Integer.parseInt(vehicles));
            }
            createApplication(operatorType, licenceType);
        } finally {
            lock.unlock();
        }
    }

    public void createApplicationWithTrafficArea(String operatorType, String licenceType, TrafficArea trafficArea) throws HttpException {
        lock.lock();
        try {
            world.createApplication.setTrafficArea(trafficArea);
            world.createApplication.setEnforcementArea(EnforcementArea.valueOf(trafficArea.name()));
            createApplication(operatorType, licenceType);
        } finally {
            lock.unlock();
        }
    }

    public void createSubmittedApplication(String operatorType, String licenceType) throws HttpException {
        lock.lock();
        try {
            createApplication(operatorType, licenceType);
            world.APIJourney.submitApplication();
        } finally {
            lock.unlock();
        }
    }

    public void createSubmittedApplicationWithVehicles(String operatorType, String licenceType, String vehicles) throws HttpException {
        lock.lock();
        try {
            createApplicationWithVehicles(operatorType, licenceType, vehicles);
            world.APIJourney.submitApplication();
        } finally {
            lock.unlock();
        }
    }

    public void createLicence(String operatorType, String licenceType) throws HttpException {
        lock.lock();
        try {
            createSubmittedApplication(operatorType, licenceType);
            world.APIJourney.grantLicenceAndPayFees();
        } finally {
            lock.unlock();
        }
    }

    public void createLicenceWithVehicles(String operatorType, String licenceType, String vehicles) throws HttpException {
        lock.lock();
        try {
            createSubmittedApplicationWithVehicles(operatorType, licenceType, vehicles);
            world.APIJourney.grantLicenceAndPayFees();
        } finally {
            lock.unlock();
        }
    }

    public void createLicenceWithTrafficArea(String operatorType, String licenceType, TrafficArea trafficArea) throws HttpException {
        lock.lock();
        try {
            world.createApplication.setTrafficArea(trafficArea);
            world.createApplication.setEnforcementArea(EnforcementArea.valueOf(trafficArea.name()));
            createLicence(operatorType, licenceType);
        } finally {
            lock.unlock();
        }
    }

    public void createNILicence(String operatorType, String licenceType) throws HttpException {
        lock.lock();
        try {
            world.createApplication.setNiFlag("Y");
            createLicence(operatorType, licenceType);
        } finally {
            lock.unlock();
        }
    }

    public void createLGVOnlyApplication(String NIFlag) throws HttpException {
        lock.lock();
        try {
            world.createApplication.setNiFlag(NIFlag.equals("NI") ? "Y" : "N");
            world.createApplication.setVehicleType(VehicleType.LGV_ONLY_FLEET.asString());
            world.createApplication.setTotalOperatingCentreLgvAuthority(5);
            world.createApplication.setNoOfAddedHgvVehicles(0);
            world.createApplication.setNoOfAddedLgvVehicles(5);
            world.licenceCreation.createApplication("goods", "standard_international");
        } finally {
            lock.unlock();
        }
    }

    public void createSubmittedLGVOnlyApplication(String NIFlag) throws HttpException {
        lock.lock();
        try {
            createLGVOnlyApplication(NIFlag);
            world.APIJourney.submitApplication();
        } finally {
            lock.unlock();
        }
    }

    public void createLGVOnlyLicence(String NIFlag) throws HttpException {
        lock.lock();
        try {
            createSubmittedLGVOnlyApplication(NIFlag);
            world.APIJourney.grantLicenceAndPayFees();
        } finally {
            lock.unlock();
        }
    }

    public void createLGVOnlyLicenceWithTrafficArea(String NIFlag, TrafficArea trafficArea) throws HttpException {
        lock.lock();
        try {
            world.createApplication.setTrafficArea(trafficArea);
            world.createApplication.setEnforcementArea(EnforcementArea.valueOf(trafficArea.name()));
            createLGVOnlyLicence(NIFlag);
        } finally {
            lock.unlock();
        }
    }

    public boolean isGoodsLicence() {
        lock.lock();
        try {
            return world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString());
        } finally {
            lock.unlock();
        }
    }

    public boolean isPSVLicence() {
        lock.lock();
        try {
            return world.createApplication.getOperatorType().equals(OperatorType.PUBLIC.asString());
        } finally {
            lock.unlock();
        }
    }

    public boolean isAGoodsInternationalLicence() {
        lock.lock();
        try {
            return isGoodsLicence() && isAnInternationalLicence();
        } finally {
            lock.unlock();
        }
    }

    public boolean isAnInternationalLicence() {
        lock.lock();
        try {
            return world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString());
        } finally {
            lock.unlock();
        }
    }

    public boolean isARestrictedLicence() {
        lock.lock();
        try {
            return world.createApplication.getLicenceType().equals(LicenceType.RESTRICTED.asString());
        } finally {
            lock.unlock();
        }
    }

    public boolean isLGVOnlyLicence() {
        lock.lock();
        try {
            return world.createApplication.getVehicleType().equals(VehicleType.LGV_ONLY_FLEET.asString());
        } finally {
            lock.unlock();
        }
    }
}