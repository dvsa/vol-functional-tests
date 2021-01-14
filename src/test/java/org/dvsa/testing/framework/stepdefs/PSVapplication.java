package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import enums.UserRoles;

public class PSVapplication implements En {
    private World world;

    public PSVapplication(World world) {

        Given("^I have applied for a \"([^\"]*)\" \"([^\"]*)\" licence$", (String operator, String licenceType) -> {
            world.createApplication.setOperatorType(operator);
            world.createApplication.setLicenceType(licenceType);
            if(licenceType.equals("special_restricted") && (world.createApplication.getApplicationId() == null)){
                world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
                world.APIJourneySteps.createSpecialRestrictedLicence();
            }
            else if (world.createApplication.getApplicationId() == null) {
                world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
                world.APIJourneySteps.createApplication();
                world.APIJourneySteps.submitApplication();
            }
        });

        Given("^I have a \"([^\"]*)\" \"([^\"]*)\" application which is under consideration$", (String vehicleType, String typeOfLicence) -> {
            world.createApplication.setIsInterim("Y");
            world.createApplication.setOperatorType(vehicleType);
            world.createApplication.setLicenceType(typeOfLicence);
            if (world.createApplication.getApplicationId() == null) {
                world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
                world.APIJourneySteps.createApplication();
                world.APIJourneySteps.submitApplication();
            }
        });
    }
}
