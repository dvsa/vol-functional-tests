package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.EnforcementArea;
import apiCalls.enums.UserType;
import io.cucumber.java8.En;
import org.dvsa.testing.lib.pages.BasePage;

import static apiCalls.enums.TrafficArea.trafficAreaList;
import static apiCalls.enums.EnforcementArea.enforcementAreaList;

public class UserResearch extends BasePage implements En {
    String fileName = "src/test/resources/";
    FakerUtils faker = new FakerUtils();

    public UserResearch(World world) {

        Given("^^I have applied for \"([^\"]*)\" \"([^\"]*)\" licences$", (String licenceType, String operator) -> {
            world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
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

        Given("^I have applied for \"([^\"]*)\" \"([^\"]*)\" TM application$", (String licenceType, String operatorType) -> {
            String password;
            world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.createApplication.setNoOfVehiclesRequested(3);
            for (TrafficArea ta : trafficAreaList()) {
                world.licenceCreation.createApplicationWithTrafficArea(operatorType, licenceType, ta);
                password = S3.getTempPassword(world.createApplication.getTransportManagerEmailAddress());
                world.genericUtils.writeToFile(world.createApplication.getTransportManagerUserName(), password, fileName.concat("TM.csv"));
                world.createApplication.setApplicationId(null);
            }
        });
    }
}