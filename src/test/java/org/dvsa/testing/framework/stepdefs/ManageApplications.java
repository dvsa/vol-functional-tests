package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.UserType;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import enums.UserRoles;
import org.openqa.selenium.InvalidArgumentException;

public class ManageApplications {
    World world;
    String fileName = "src/test/resources/";
    FakerUtils faker = new FakerUtils();

    public ManageApplications(World world) {
        this.world = world;
    }

    @Given("I have applied for a {string} {string} licence")
    public void iHaveAppliedForALicence(String operator, String licenceType) {
        world.createLicence.setNoOfVehiclesRequired(13);
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
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setOperatingCentreVehicleCap(6);
        for (TrafficArea ta : trafficAreaList()) {
            world.APIJourneySteps.createLicenceWithTrafficArea(licenceType, operator, ta);
        }
    }

    @Given("I have applied for {string} {string} {string} licences")
    public void iHaveAppliedForLicences(String noOfLicences, String licenceType, String operator) {
        if (Integer.parseInt(noOfLicences) > 9) {
            throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
        }
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setOperatingCentreVehicleCap(6);
        world.createApplication.setNoOfVehiclesRequested(2);
        for (int i = 0; i < Integer.parseInt(noOfLicences); i ++) {
            TrafficArea ta = trafficAreaList()[i];
            world.APIJourneySteps.createLicenceWithTrafficArea(licenceType, operator, ta);
        }
    }

    @Given("I have applied for {string} {string} {string} licences with {string} vehicles and a cap of {string}")
    public void iHaveAppliedForLicencesWithVehicles(String noOfLicences, String licenceType, String operator, String vehicles, String OCVehicleCap) {
        if (Integer.parseInt(noOfLicences) > 9) {
            throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
        }
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setOperatingCentreVehicleCap(Integer.parseInt(OCVehicleCap));
        world.createApplication.setNoOfVehiclesRequested(Integer.parseInt(vehicles));
        for (int i = 0; i < Integer.parseInt(noOfLicences); i ++) {
            apiCalls.enums.TrafficArea ta = trafficAreaList()[i];
            world.APIJourneySteps.createLicenceWithTrafficArea(licenceType, operator, ta);
        }
    }

    @Given("I have applied for {string} {string} TM application")
    public void iHaveAppliedForTMApplication(String licenceType, String operator) throws Exception {
        String password;
        world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
        world.createLicence.setNoOfVehiclesRequired(3);
        
        // Don't think both for loops are required but not sure what this test is for.
        for (TrafficArea ta : trafficAreaList()) {
//            world.createLicence.setPostcode(ta);
            world.createLicence.setOperatorType(operator);
            world.createLicence.setLicenceType(licenceType);
//            world.createLicence.setTrafficArea(ta);
//            world.createLicence.setEnforcementArea(EnforcementArea.getEnforcementArea(ta));
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
        }
    }

    @Then("the licence should be created and granted")
    public void theLicenceShouldBeCreatedAndGranted() throws Exception {
        world.genericUtils.writeToFile(world.createLicence.getLoginId(), world.globalMethods.getLoginPassword(), fileName.concat("Operator.csv"));
    }

    private TrafficArea[] trafficAreaList() {
        return new TrafficArea[]{TrafficArea.NORTH_EAST, TrafficArea.NORTH_WEST, TrafficArea.MIDLANDS,
                TrafficArea.EAST, TrafficArea.WALES, TrafficArea.WEST, TrafficArea.LONDON,
                TrafficArea.SCOTLAND, TrafficArea.NORTHERN_IRELAND};
    }
}
