package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import cucumber.api.java8.En;;
import enums.TrafficArea;
import enums.UserRoles;
import org.dvsa.testing.framework.Utils.Generic.EnforcementArea;
import org.dvsa.testing.framework.Utils.Generic.PostCode;
import org.dvsa.testing.lib.pages.BasePage;

public class UserResearch extends BasePage implements En {
    String fileName = "src/test/resources/";
    FakerUtils faker = new FakerUtils();

    public UserResearch(World world) {

        Given("^^I have applied for \"([^\"]*)\" \"([^\"]*)\" licences$", (String licenceType, String operator) -> {
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.createLicence.setNoOfVehiclesRequired(5);
            for (int i = 0; i < trafficAreaList().length - 1; ) {
                for (String ta : trafficAreaList()) {
                    world.createLicence.setPostcode(PostCode.getPostCode(TrafficArea.valueOf(ta)));
                    world.createLicence.setOperatorType(operator);
                    world.createLicence.setLicenceType(licenceType);
                    world.createLicence.setTrafficArea(String.valueOf(TrafficArea.valueOf(ta)));
                    world.createLicence.setEnforcementArea(EnforcementArea.getEnforcementArea(TrafficArea.valueOf(ta)));
                    world.APIJourneySteps.createApplication();
                    world.APIJourneySteps.submitApplication();
                    world.APIJourneySteps.grantLicenceAndPayFees();
                    world.createLicence.setApplicationNumber(null);
                    i++;
                }
            }
        });

        Given("^I have applied for \"([^\"]*)\" \"([^\"]*)\" TM application$", (String licenceType, String operator) -> {
            String password;
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.createLicence.setNoOfVehiclesRequired(3);
            for (int i = 0; i < trafficAreaList().length - 1; ) {
                for (String ta : trafficAreaList()) {
                    world.createLicence.setPostcode(PostCode.getPostCode(TrafficArea.valueOf(ta)));
                    world.createLicence.setOperatorType(operator);
                    world.createLicence.setLicenceType(licenceType);
                    world.createLicence.setTrafficArea(String.valueOf(TrafficArea.valueOf(ta)));
                    world.createLicence.setEnforcementArea(EnforcementArea.getEnforcementArea(TrafficArea.valueOf(ta)));
                    world.APIJourneySteps.createApplication();
                    String externalFirstName = faker.generateFirstName();
                    String externalLastName = faker.generateLastName();
                    String randomInt = String.valueOf(Int.random(1000, 9999));
                    String externalTmUserName = String.format("UserResearchTM-%s%s%s", externalFirstName, externalLastName, randomInt);
                    world.createLicence.setForeName(externalFirstName);
                    world.createLicence.setFamilyName(externalLastName);
                    world.createLicence.setTmUserName(externalTmUserName);
                    world.createLicence.setTransManEmailAddress(String.format("UserResearchTM%s%s%s@vol.org", externalFirstName, externalLastName, randomInt));
                    world.createLicence.addTransportManager();
                    password = S3.getTempPassword(world.createLicence.getTransManEmailAddress());
                    world.genericUtils.writeToFile(world.createLicence.getTmUserName(), password, fileName.concat("TM.csv"));
                    world.createLicence.setApplicationNumber(null);
                    i++;
                }
            }
        });
        Then("^the licence should be created and granted$", () -> {
            world.genericUtils.writeToFile(world.createLicence.getLoginId(), world.globalMethods.getLoginPassword(), fileName.concat("Operator.csv"));
        });
    }
    private String[] trafficAreaList() {
        return new String[]{"B", "C", "D", "F", "G", "H", "K", "M"};
    }
}