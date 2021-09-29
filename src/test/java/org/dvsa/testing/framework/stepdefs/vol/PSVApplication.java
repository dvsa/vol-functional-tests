package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.UserType;
import cucumber.api.java.en.Given;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;


public class PSVApplication extends BasePage implements En {
    private World world;

    public PSVApplication(World world) {this.world = world;}

        @Given("I have applied for a {string} licence")
        public void iHaveAppliedForALicence(String operator, String licenceType) {
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
        }

        @Given("I have a {string} application which is under consideration")
        public void iHaveAApplicationWhichIsUnderConsideration(String vehicleType, String typeOfLicence) {
            world.createApplication.setIsInterim("Y");
            world.createApplication.setOperatorType(vehicleType);
            world.createApplication.setLicenceType(typeOfLicence);
            if (world.createApplication.getApplicationId() == null) {
                world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
                world.APIJourney.createApplication();
                world.APIJourney.submitApplication();
            }
        }
    }
