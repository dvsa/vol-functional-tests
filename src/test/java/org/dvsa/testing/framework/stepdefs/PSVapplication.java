package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import enums.UserRoles;
import org.dvsa.testing.framework.Journeys.APIJourneySteps;

public class PSVapplication implements En {
    private World world;

    public PSVapplication(World world) {
        world.APIJourneySteps = new APIJourneySteps(world);

        Given("^I have applied for a \"([^\"]*)\" \"([^\"]*)\" licence$", (String operator, String licenceType) -> {
            world.createLicence.setOperatorType(operator);
            world.createLicence.setLicenceType(licenceType);
            if(licenceType.equals("special_restricted") && (world.createLicence.getApplicationNumber() == null)){
                world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
                world.APIJourneySteps.createSpecialRestrictedLicence();
            }
            else if (world.createLicence.getApplicationNumber() == null) {
                world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
                world.APIJourneySteps.createApplication();
                world.APIJourneySteps.submitApplication();
            }
        });

        Given("^I have a \"([^\"]*)\" \"([^\"]*)\" application which is under consideration$", (String vehicleType, String typeOfLicence) -> {
            world.createLicence.setIsInterim("Y");
            world.createLicence.setOperatorType(vehicleType);
            world.createLicence.setLicenceType(typeOfLicence);
            if (world.createLicence.getApplicationNumber() == null) {
                world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
                world.APIJourneySteps.createApplication();
                world.APIJourneySteps.submitApplication();
            }
        });
    }
}
