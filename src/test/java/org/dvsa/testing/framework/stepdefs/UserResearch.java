package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.string.Str;
import io.cucumber.java8.En;
import enums.TrafficArea;
import enums.UserRoles;
import org.dvsa.testing.framework.Utils.Generic.EnforcementArea;
import org.dvsa.testing.framework.Utils.Generic.PostCode;
import org.dvsa.testing.lib.pages.BasePage;

import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getRandomNumberInts;

public class UserResearch extends BasePage implements En {
    String fileName = "src/test/resources/";

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
                    String randWord = Str.randomWord(4);
                    String externalTmUserName;
                    externalTmUserName = "apiTM".concat(randWord);
                    world.createLicence.setForeName("External");
                    world.createLicence.setFamilyName("TM");
                    world.createLicence.setTmUserName(externalTmUserName);
                    world.createLicence.setTransManEmailAddress("externalTM".concat(String.valueOf(getRandomNumberInts(0, 99999))).concat("@vol.org"));
                    world.createLicence.addTransportManager();
                    password = S3.getTempPassword(world.createLicence.getTransManEmailAddress());
                    world.genericUtils.writeToFile(world.createLicence.getTmUserName(), password, fileName.concat("TM.csv"));
                    world.createLicence.setApplicationNumber(null);
                    i++;
                }
            }
        });
        Then("^the licence should be created and granted$", () -> {
            world.genericUtils.writeToFile(world.createLicence.getLoginId(), world.UIJourneySteps.getPassword(), fileName.concat("Operator.csv"));
        });
        Then("^accounts should be created$", () -> {

        });
    }
    private String[] trafficAreaList() {
        return new String[]{"B", "C", "D", "F", "G", "H", "K", "M"};
    }
}