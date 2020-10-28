package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import enums.TrafficArea;
import enums.UserRoles;
import org.dvsa.testing.framework.Utils.Generic.EnforcementArea;
import org.dvsa.testing.framework.Utils.Generic.PostCode;

public class ManageApplications {
    World world;
    String fileName = "src/test/resources/";
    FakerUtils faker = new FakerUtils();

    public ManageApplications(World world) {
        this.world = world;
    }

    @Given("I have applied for a {string} {string} licence")
    public void iHaveAppliedForALicence(String operator, String licenceType) {
        world.createLicence.setOperatorType(operator);
        world.createLicence.setLicenceType(licenceType);
        if (licenceType.equals("special_restricted") && (world.createLicence.getApplicationNumber() == null)) {
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.APIJourneySteps.createSpecialRestrictedLicence();
        } else if (world.createLicence.getApplicationNumber() == null) {
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.APIJourneySteps.createApplication();
            world.APIJourneySteps.submitApplication();
        }
    }

    @Given("I have a {string} {string} application which is under consideration")
    public void iHaveAApplicationWhichIsUnderConsideration(String vehicleType, String typeOfLicence) {
        world.createLicence.setIsInterim("Y");
        world.createLicence.setOperatorType(vehicleType);
        world.createLicence.setLicenceType(typeOfLicence);
        if (world.createLicence.getApplicationNumber() == null) {
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.APIJourneySteps.createApplication();
            world.APIJourneySteps.submitApplication();
        }
    }

    @Given("I have partially applied for a {string} {string} licence")
    public void iHavePartiallyAppliedForALicence(String operator, String licenceType) {
        world.createLicence.setOperatorType(operator);
        world.createLicence.setLicenceType(licenceType);
        if (licenceType.equals("special_restricted") && (world.createLicence.getApplicationNumber() == null)) {
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.APIJourneySteps.createSpecialRestrictedLicence();
        } else if (world.createLicence.getApplicationNumber() == null) {
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.APIJourneySteps.createApplication();
        }
    }

    @Given("I have applied for {string} {string} licences")
    public void iHaveAppliedForLicences(String licenceType, String operator) {
        world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
        world.createLicence.setNoOfVehiclesRequired(6);
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
    }

    @Given("I have applied for {string} {string} TM application")
    public void iHaveAppliedForTMApplication(String licenceType, String operator) throws Exception {
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
    }

    @Then("the licence should be created and granted")
    public void theLicenceShouldBeCreatedAndGranted() throws Exception {
        world.genericUtils.writeToFile(world.createLicence.getLoginId(), world.globalMethods.getLoginPassword(), fileName.concat("Operator.csv"));
    }

    private String[] trafficAreaList() {
        return new String[]{"B", "C", "D", "F", "G", "H", "K", "M"};
    }
}
