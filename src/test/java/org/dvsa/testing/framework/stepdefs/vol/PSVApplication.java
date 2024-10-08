package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.UserType;
import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


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
}