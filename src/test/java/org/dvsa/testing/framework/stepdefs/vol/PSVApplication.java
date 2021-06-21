package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.UserType;
import io.cucumber.java8.En;

public class PSVApplication implements En {
    private World world;

    public PSVApplication(World world) {

        Given("^I have applied for a \"([^\"]*)\" \"([^\"]*)\" licence$", (String operator, String licenceType) -> {
            world.createApplication.setOperatorType(operator);
            world.createApplication.setLicenceType(licenceType);
            if(licenceType.equals("special_restricted") && (world.createApplication.getApplicationId() == null)){
                world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
                world.APIJourneySteps.createSpecialRestrictedLicence();
            }
            else if (world.createApplication.getApplicationId() == null) {
                world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
                world.APIJourneySteps.createApplication();
                world.APIJourneySteps.submitApplication();
            }
        });

        Given("^I have a \"([^\"]*)\" \"([^\"]*)\" application which is under consideration$", (String vehicleType, String typeOfLicence) -> {
            world.createApplication.setIsInterim("Y");
            world.createApplication.setOperatorType(vehicleType);
            world.createApplication.setLicenceType(typeOfLicence);
            if (world.createApplication.getApplicationId() == null) {
                world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
                world.APIJourneySteps.createApplication();
                world.APIJourneySteps.submitApplication();
            }
        });
    }
}