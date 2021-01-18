package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.EnforcementArea;
import cucumber.api.java8.En;
import enums.UserRoles;
import org.dvsa.testing.lib.pages.BasePage;

import static apiCalls.enums.TrafficArea.trafficAreaList;
import static apiCalls.enums.EnforcementArea.enforcementAreaList;

public class UserResearch extends BasePage implements En {
    String fileName = "src/test/resources/";
    FakerUtils faker = new FakerUtils();

    public UserResearch(World world) {

        Given("^^I have applied for \"([^\"]*)\" \"([^\"]*)\" licences$", (String licenceType, String operator) -> {
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.createApplication.setNoOfVehiclesRequested(5);
            for (int i = 0; i < trafficAreaList().length - 1; ) {
                for (TrafficArea TA : trafficAreaList()) {
                    world.createApplication.setCorrespondencePostCode(TrafficArea.getPostCode(TA));
                    world.createApplication.setOperatorType(operator);
                    world.createApplication.setLicenceType(licenceType);
                    world.createApplication.setTrafficArea(TA);
                    world.createApplication.setEnforcementArea(EnforcementArea.valueOf(TA.name()));
                    world.APIJourneySteps.createApplication();
                    world.APIJourneySteps.submitApplication();
                    world.APIJourneySteps.grantLicenceAndPayFees();
                    world.createApplication.setApplicationId(null);
                    i++;
                }
            }
        });

        Given("^I have applied for \"([^\"]*)\" \"([^\"]*)\" TM application$", (String licenceType, String operator) -> {
            String password;
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.createApplication.setNoOfVehiclesRequested(3);
            for (int i = 0; i < trafficAreaList().length - 1; ) {
                TrafficArea TA = trafficAreaList()[i];
                EnforcementArea EA = enforcementAreaList()[i];
                world.createApplication.setPostCodeByTrafficArea(TrafficArea.getPostCode(TA));
                world.createApplication.setOperatorType(operator);
                world.createApplication.setLicenceType(licenceType);
                world.createApplication.setTrafficArea(TA);
                world.createApplication.setEnforcementArea(EA);
                world.APIJourneySteps.createApplication();
                String externalFirstName = faker.generateFirstName();
                String externalLastName = faker.generateLastName();
                String randomInt = String.valueOf(Int.random(1000, 9999));
                String externalTmUserName = String.format("UserResearchTM-%s%s%s", externalFirstName, externalLastName, randomInt);
                world.createApplication.setTransportManagerFirstName(externalFirstName);
                world.createApplication.setTransportManagerLastName(externalLastName);
                world.createApplication.setTransportManagerUserName(externalTmUserName);
                world.createApplication.setTransportManagerEmailAddress(String.format("UserResearchTM%s%s%s@vol.org", externalFirstName, externalLastName, randomInt));
                world.createApplication.addTransportManager();
                password = S3.getTempPassword(world.createApplication.getTransportManagerEmailAddress());
                world.genericUtils.writeToFile(world.createApplication.getTransportManagerUserName(), password, fileName.concat("TM.csv"));
                world.createApplication.setApplicationId(null);
                i++;
            }
        });
        Then("^the licence should be created and granted$", () -> {
            world.genericUtils.writeToFile(world.registerUser.getUserName(), world.globalMethods.getLoginPassword(), fileName.concat("Operator.csv"));
        });
    }
}