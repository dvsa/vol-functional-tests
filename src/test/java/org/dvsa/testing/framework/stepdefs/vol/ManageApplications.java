package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.aws.s3.S3;
import apiCalls.enums.LicenceType;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.UserType;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.InvalidArgumentException;

import java.util.List;
import java.util.Locale;

import static apiCalls.enums.TrafficArea.trafficAreaList;

public class ManageApplications {
    World world;
    String fileName = "src/test/resources/";

    public ManageApplications(World world) {
        this.world = world;
    }

    @Given("I have a {string} application with {int} vehicles and a vehicleAuthority of {int}")
    public void iHaveANewApplicationWithVehiclesAndVehicleAuthorityOf(String operatorType, int numberOfVehicles, int authority) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setNoOfAddedHgvVehicles(numberOfVehicles);
        world.createApplication.setTotalOperatingCentreHgvAuthority(numberOfVehicles);
        world.createApplication.setNoOfOperatingCentreVehicleAuthorised(authority);
        world.licenceCreation.createApplication(operatorType, LicenceType.STANDARD_INTERNATIONAL.name().toLowerCase(Locale.ROOT));
    }

    @Given("I have a {string} {string} application")
    public void iHaveAnStringStringApplication(String operatorType, String licenceType) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createApplication(operatorType, licenceType);
    }

    @Given("I have a {string} {string} interim application which is under consideration")
    public void iHaveAnInterimApplicationWhichIsUnderConsideration(String operatorType, String licenceType) {
        world.createApplication.setIsInterim("Y");
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createSubmittedApplicationWithVehicles(operatorType, licenceType, "5");
    }

    @Given("I have a submitted {string} {string} application")
    public void iHaveASubmittedApplication(String operatorType, String licenceType) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createSubmittedApplication(operatorType, licenceType);
    }

    @Given("I have all {string} {string} Traffic Areas applications with an external TM")
    public void iHaveAppliedForTMApplication(String operatorType, String licenceType) throws Exception {
        String password;
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setNoOfAddedHgvVehicles(3);
        for (TrafficArea ta : trafficAreaList()) {
            world.licenceCreation.createApplicationWithTrafficArea(operatorType, licenceType, ta);
            password = S3.getTempPassword(world.createApplication.getTransportManagerEmailAddress());
            world.genericUtils.writeToFile(world.createApplication.getTransportManagerUserName(), password, fileName.concat("TM.csv"));
            world.createApplication.setApplicationId(null);
//          Need to add way to create new TMs. Values are set in the addTM method because if more than one TM is added
//          in one go, the user logins equal and the API rejects this.
//          Possibly all uses of the add tm method when used more than once, needs to reset the values before the run
//          of the addTM method.
        }
    }

    @Then("i write the licence login information to a file for use of user research")
    public void theLicenceShouldBeCreatedAndGranted() throws Exception {
        world.genericUtils.writeToFile(world.registerUser.getUserName(), world.globalMethods.getLoginPassword(), fileName.concat("Operator.csv"));
        // What does this do?
        // This writes out the user details[user + password] to a file. It was used to help the user researchers with creating logins
    }

    @Given("I have {string} {string} {string} licences")
    public void iHaveNumberLicences(String noOfLicences, String operatorType, String licenceType) {
        if (Integer.parseInt(noOfLicences) > 9) {
            throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
        }
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        for (int i = 0; i < Integer.parseInt(noOfLicences); i++) {
            world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, trafficAreaList()[i]);
        }
    }

    @Given("I have all {string} {string} traffic area licences")
    public void iHaveAllTrafficAreaForLicences(String operatorType, String licenceType) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setNoOfAddedHgvVehicles(3);
        for (TrafficArea ta : trafficAreaList()) {
            world.licenceCreation.createApplicationWithTrafficArea(operatorType, licenceType, ta);
            world.createApplication.setApplicationId(null);
        }
    }

    @Given("I have a {string} {string} application with {string} vehicles")
    public void iHaveAnApplicationWithVehicles(String operatorType, String licenceType, String vehicles) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createApplicationWithVehicles(operatorType, licenceType, vehicles);
    }

    @Given("I have a {string} {string} licence")
    public void iHaveLicence(String operatorType, String licenceType) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLicence(operatorType, licenceType);
    }

    @Given("I have a {string} {string} licence with {string} vehicle authorisation")
    public void iHaveLicenceWithVehicles(String operatorType, String licenceType, String vehicles) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLicenceWithVehicles(operatorType, licenceType, vehicles);
    }

    @Given("I have a {string} {string} NI licence")
    public void iHaveNILicence(String operatorType, String licenceType) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createNILicence(operatorType, licenceType);
    }

    @Given("I have {string} {string} {string} licences with {string} vehicles and a vehicleAuthority of {string}")
    public void iHaveLicencesWithVehiclesAndAVehicleAuthorityOf(String noOfLicences, String operatorType, String licenceType, String vehicles, String vehicleAuth) {
        if (Integer.parseInt(noOfLicences) > 9) {
            throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
        }
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setNoOfAddedHgvVehicles(Integer.parseInt(vehicles));
        world.createApplication.setTotalOperatingCentreHgvAuthority(Integer.parseInt(vehicleAuth));
        world.createApplication.setNoOfOperatingCentreVehicleAuthorised(Integer.parseInt(vehicleAuth));
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
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        for (String ta : trafficAreas) {
            TrafficArea trafficArea = TrafficArea.valueOf(ta.toUpperCase());
            world.licenceCreation.createApplicationWithTrafficArea(operatorType, licenceType, trafficArea);
        }
    }

    //TODO will need changing when lgv authority and hgv authority is split.
    @Given("I have {string} {string} {string} licences with {string} HGVs and {string} LGVs with a vehicleAuthorities of {string} and {string}")
    public void iHaveLicencesWithVehiclesAndAVehicleAuthorityOf(String noOfLicences, String operatorType, String licenceType, String hgvs, String lgvs, String hgvAuth, String lgvAuth) {
        if (Integer.parseInt(noOfLicences) > 9) {
            throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
        }
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setNoOfAddedHgvVehicles(Integer.parseInt(hgvs));
        world.createApplication.setNoOfAddedLgvVehicles(Integer.parseInt(lgvs));
        world.createApplication.setTotalOperatingCentreHgvAuthority(Integer.parseInt(hgvs));
        world.createApplication.setTotalOperatingCentreLgvAuthority(Integer.parseInt(lgvs));
//        world.createApplication.setNoOfOperatingCentreVehicleAuthorised(Integer.parseInt(OCVehicleCap));
        // Not used because functionality is not yet in place. Will use separate HGV and LGV authorisations when

        for (int i = 0; i < Integer.parseInt(noOfLicences); i ++) {
            TrafficArea ta = trafficAreaList()[i];
            world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, ta);
        }
    }

    @Given("I have a {string} {string} licence with {int} operating centres")
    public void iHaveALicenceWithOperatingCentres(String operatorType, String licenceType, int numberOfOperatingCentres) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createApplication(operatorType, licenceType);
        for (int i = 1; i < numberOfOperatingCentres; i++)
            world.createApplication.addOperatingCentre();
        world.APIJourney.submitApplication();
        world.APIJourney.grantLicenceAndPayFees();
    }

    @Given("I have a {string} lgv only application")
    public void iHaveALgvOnlyApplication(String NIFlag) {
        world.licenceCreation.createLGVOnlyApplication(NIFlag);
    }

    @Given("I have a submitted {string} lgv only application")
    public void iHaveASubmittedLgvOnlyApplication(String NIFlag) {
        world.licenceCreation.createSubmittedLGVOnlyApplication(NIFlag);
    }

    @Given("I have a valid {string} lgv only licence")
    public void iHaveAValidLgvOnlyLicence(String NIFlag) {
        world.licenceCreation.createLGVOnlyLicence(NIFlag);
    }
}