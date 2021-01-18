package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.UserType;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.InvalidArgumentException;

import java.util.List;

import static apiCalls.enums.EnforcementArea.enforcementAreaList;
import static apiCalls.enums.TrafficArea.trafficAreaList;

public class ManageApplications {
    World world;
    String fileName = "src/test/resources/";
    FakerUtils faker = new FakerUtils();

    public ManageApplications(World world) {
        this.world = world;
    }

    @Given("I have a {string} application")
    public void iHaveANewApplication(String operatorType) {
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createApplication(operatorType, "standard_national");
    }

    @Given("I have a {string} {string} application")
    public void iHaveAnStringStringApplication(String operatorType, String licenceType) {
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createApplication(operatorType, licenceType);
    }

    @Given("I have a {string} {string} interim application which is under consideration")
    public void iHaveAnInterimApplicationWhichIsUnderConsideration(String operatorType, String licenceType) {
        world.createApplication.setIsInterim("Y");
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createSubmittedApplicationWithVehicles(operatorType, licenceType, "5");
    }

    @Given("I have submitted a {string} {string} application")
    public void iHaveSubmittedApplication(String operatorType, String licenceType) {
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createSubmittedApplication(operatorType, licenceType);
    }

    @Given("I have all {string} {string} Traffic Areas applications with an external TM")
    public void iHaveAppliedForTMApplication(String licenceType, String operator) throws Exception {
        String password;
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setNoOfVehiclesRequested(3);
        
        for (int i = 0; i < 9; i++) {
            world.createApplication.setPostCodeByTrafficArea(TrafficArea.getPostCode(trafficAreaList()[i]));
            world.createApplication.setOperatorType(operator);
            world.createApplication.setLicenceType(licenceType);
            world.createApplication.setTrafficArea(trafficAreaList()[i]);
            world.createApplication.setEnforcementArea(enforcementAreaList()[i]);
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
//            Need to think of new way of setting TM names now it is included in the addTM method.
            password = S3.getTempPassword(world.createApplication.getTransportManagerEmailAddress());
            world.genericUtils.writeToFile(world.createApplication.getTransportManagerUserName(), password, fileName.concat("TM.csv"));
            world.createApplication.setApplicationId(null);
        }
    }

    @Then("the licence should be created and granted")
    public void theLicenceShouldBeCreatedAndGranted() throws Exception {
        world.genericUtils.writeToFile(world.registerUser.getUserName(), world.globalMethods.getLoginPassword(), fileName.concat("Operator.csv"));
        // What does this do?
    }

    @Given("I have {string} {string} {string} licences")
    public void iHaveNumberLicences(String noOfLicences, String operatorType, String licenceType) {
        if (Integer.parseInt(noOfLicences) > 9) {
            throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
        }
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        for (TrafficArea ta : trafficAreaList()) {
            world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, ta);
        }
    }

    @Given("I have all {string} {string} traffic area licences")
    public void iHaveAllTrafficAreaForLicences(String operatorType, String licenceType) {
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        for (TrafficArea ta : trafficAreaList()) {
            world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, ta);
        }
    }

    @Given("I have a {string} {string} application with {string} vehicles")
    public void iHaveAnApplicationWithVehicles(String operatorType, String licenceType, String vehicles) {
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createApplicationWithVehicles(operatorType, licenceType, vehicles);
    }

    @Given("I have a {string} {string} licence")
    public void iHaveLicenceWithVehicles(String operatorType, String licenceType) {
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLicence(operatorType, licenceType);
    }

    @Given("I have a {string} {string} licence with {string} vehicles")
    public void iHaveLicenceWithVehicles(String operatorType, String licenceType, String vehicles) {
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLicenceWithVehicles(operatorType, licenceType, vehicles);
    }

    @Given("I have {string} {string} {string} licences with {string} vehicles and a cap of {string}")
    public void iHaveLicencesWithVehiclesAndCap(String noOfLicences, String operatorType, String licenceType, String vehicles, String OCVehicleCap) {
        if (Integer.parseInt(noOfLicences) > 9) {
            throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
        }
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setOperatingCentreVehicleCap(Integer.parseInt(OCVehicleCap));
        world.createApplication.setNoOfVehiclesRequested(Integer.parseInt(vehicles));
        for (int i = 0; i < Integer.parseInt(noOfLicences); i ++) {
            TrafficArea ta = trafficAreaList()[i];
            world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, ta);
        }
    }

    @Given("i have a {string} {string} {string} application in traffic area")
    public void iHaveAnApplicationInTrafficArea(String operatorType, String licenceType, String Region, DataTable trafficAreaTable) {
        if (Region.equals("NI".toUpperCase())) {
            Region = "Y";
        } else {
            Region = "N";
        }
        world.createApplication.setNiFlag(Region);
        List<String> trafficAreas = trafficAreaTable.asList(String.class);
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        for (String ta : trafficAreas) {
            TrafficArea trafficArea = TrafficArea.valueOf(ta.toUpperCase());
            world.licenceCreation.createApplicationWithTrafficArea(operatorType, licenceType, trafficArea);
        }
    }
}