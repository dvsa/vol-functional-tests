package org.dvsa.testing.framework.stepdefs.vol;

import apiCalls.enums.OperatorType;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.aws.s3.S3;
import apiCalls.enums.LicenceType;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.UserType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.InvalidArgumentException;

import java.util.List;
import java.util.Locale;

import static apiCalls.enums.TrafficArea.trafficAreaList;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManageApplications extends BasePage {
    World world;
    Initialisation initialisation;
    String fileName = "src/test/resources/";

    public ManageApplications(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("I have a {string} application with {int} vehicles and a vehicleAuthority of {int}")
    public void iHaveANewApplicationWithVehiclesAndVehicleAuthorityOf(String operatorType, int numberOfVehicles, int authority) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setNoOfAddedHgvVehicles(numberOfVehicles);
        world.createApplication.setTotalOperatingCentreHgvAuthority(numberOfVehicles);
        world.createApplication.setNoOfOperatingCentreVehicleAuthorised(authority);
        world.licenceCreation.createApplication(operatorType, LicenceType.STANDARD_INTERNATIONAL.name().toLowerCase(Locale.ROOT));
    }

    @Given("I have a {string} {string} application")
    public void iHaveAnStringStringApplication(String operatorType, String licenceType) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createApplication(operatorType, licenceType);
    }

    @Given("I have a submitted {string} {string} application")
    public void iHaveASubmittedApplication(String operatorType, String licenceType) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createSubmittedApplication(operatorType, licenceType);
    }

    @Given("i have a valid {string} {string} licence with an open case and bus reg")
    public void iHaveAValidLicenceWithAnOpenCaseAndBusReg(String operatorType, String licenceType) throws HttpException {
        world.busRegistrationJourney.createLicenceWithOpenCaseAndBusReg(operatorType, licenceType);
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
    }

    @Given("I have {string} {string} {string} licences")
    public void iHaveNumberLicences(String noOfLicences, String operatorType, String licenceType) throws HttpException {
        if (Integer.parseInt(noOfLicences) > 9) {
            throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
        }
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        for (int i = 0; i < Integer.parseInt(noOfLicences); i++) {
            world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, trafficAreaList()[i]);
        }
    }

    @Given("I have all {string} {string} traffic area licences")
    public void iHaveAllTrafficAreaForLicences(String operatorType, String licenceType) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setNoOfAddedHgvVehicles(3);
        for (TrafficArea ta : trafficAreaList()) {
            world.licenceCreation.createApplicationWithTrafficArea(operatorType, licenceType, ta);
            world.createApplication.setApplicationId(null);
        }
    }

    @Given("I have a {string} {string} application with {string} vehicles")
    public void iHaveAnApplicationWithVehicles(String operatorType, String licenceType, String vehicles) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createApplicationWithVehicles(operatorType, licenceType, vehicles);
    }

    @Given("I have a {string} {string} licence")
    public void iHaveLicence(String operatorType, String licenceType) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLicence(operatorType, licenceType);
    }

    @Given("I have a {string} {string} licence with {string} vehicle authorisation")
    public void iHaveLicenceWithVehicles(String operatorType, String licenceType, String vehicles) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLicenceWithVehicles(operatorType, licenceType, vehicles);
    }

    @Given("I have a {string} {string} NI licence")
    public void iHaveNILicence(String operatorType, String licenceType) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createNILicence(operatorType, licenceType);
    }

    @Given("I have {string} {string} {string} licences with {string} vehicles and a vehicleAuthority of {string}")
    public void iHaveLicencesWithVehiclesAndAVehicleAuthorityOf(String noOfLicences, String operatorType, String licenceType, String vehicles, String vehicleAuth) throws HttpException {
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
    public void iHaveAnApplicationInTrafficArea(String operatorType, String licenceType, String Region, DataTable trafficAreaTable) throws HttpException {
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
    public void iHaveLicencesWithVehiclesAndAVehicleAuthorityOf(String noOfLicences, String operatorType, String licenceType, String hgvs, String lgvs, String hgvAuth, String lgvAuth) throws HttpException {
        if (Integer.parseInt(noOfLicences) > 9) {
            throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
        }
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setNoOfAddedHgvVehicles(Integer.parseInt(hgvs));
        world.createApplication.setNoOfAddedLgvVehicles(Integer.parseInt(lgvs));
        world.createApplication.setTotalOperatingCentreHgvAuthority(Integer.parseInt(hgvs));
        world.createApplication.setTotalOperatingCentreLgvAuthority(Integer.parseInt(lgvs));

        for (int i = 0; i < Integer.parseInt(noOfLicences); i ++) {
            TrafficArea ta = trafficAreaList()[i];
            world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, ta);
        }
    }

    @Given("I have a {string} {string} licence with {int} operating centres")
    public void iHaveALicenceWithOperatingCentres(String operatorType, String licenceType, int numberOfOperatingCentres) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createApplication(operatorType, licenceType);
        for (int i = 1; i < numberOfOperatingCentres; i++)
            world.createApplication.addOperatingCentre();
        world.APIJourney.submitApplication();
        world.APIJourney.grantLicenceAndPayFees();
    }

    @Given("I have a {string} lgv only application")
    public void iHaveALgvOnlyApplication(String NIFlag) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLGVOnlyApplication(NIFlag);
    }

    @Given("I have a submitted {string} lgv only application")
    public void iHaveASubmittedLgvOnlyApplication(String NIFlag) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createSubmittedLGVOnlyApplication(NIFlag);
    }

    @Given("I have a submitted {string} lgv only application with interim")
    public void iHaveASubmittedLgvOnlyApplicationWithInterim(String NIFlag) throws HttpException {
        world.createApplication.setIsInterim("Y");
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createSubmittedLGVOnlyApplication(NIFlag);
    }

    @Given("I have a valid {string} lgv only licence")
    public void iHaveAValidLgvOnlyLicence(String NIFlag) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLGVOnlyLicence(NIFlag);
    }

    @Given("i have a valid {string} {string} licence")
    public void iHaveAValidLicence(String operatorType, String licenceType) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLicence(operatorType, licenceType);
    }

    @Given("i have a {string} application in progress")
    public void iHaveAnApplicationInProgress(String operatorType) throws HttpException {
        if(operatorType.equals("Goods")){
            operatorType = OperatorType.GOODS.name();
        }else operatorType = OperatorType.PUBLIC.name();
        world.createApplication.setOperatorType(operatorType);
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourney.createApplication();
        refreshPageWithJavascript();
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.TYPE_OF_LICENCE);
        world.selfServeNavigation.navigateThroughApplication();
        world.UIJourney.signDeclaration();
    }

    @And("the licence status is {string}")
    public void theLicenceStatusIs(String arg0) throws HttpException {
        world.updateLicence.updateLicenceStatus(arg0);
    }

    @Given("I have a psv application with traffic area {string} and enforcement area {string} which has been granted")
    public void iHaveAPsvApplicationWithTrafficAreaAndEnforcementAreaWhichHasBeenGranted(String trafficArea, String enforcementArea) throws HttpException {
        world.APIJourney.generateAndGrantPsvApplicationPerTrafficArea(trafficArea, enforcementArea);
    }

    @Given("i have an interim {string} {string} application")
    public void iHaveAnInterimApplication(String operatorType, String licenceType) throws Exception {
        if (operatorType.equals("public")){
            throw new Exception("PSV licences cannot have interim applications.");
        }
        world.createApplication.setOperatorType(operatorType);
        world.createApplication.setLicenceType(licenceType);
        world.createApplication.setIsInterim("Y");
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        if(licenceType.equals("special_restricted") && (world.createApplication.getApplicationId() == null)){
            world.APIJourney.createSpecialRestrictedLicence();
        }
        else if (world.createApplication.getApplicationId() == null) {
            world.APIJourney.createApplication();
            world.APIJourney.submitApplication();
        }
    }

    @Given("i have an application with a transport manager")
    public void iHaveAnApplicationWithATransportManager() throws HttpException {
        if (world.createApplication.getOperatorType() == null) {
            world.createApplication.setOperatorType("public");
        }
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourney.createApplication();
        world.APIJourney.submitApplication();
    }

    @And("the application status should be {string}")
    public void theApplicationStatusShouldBe(String status) throws InterruptedException {
        waitForTextToBePresent(status);
        String internalStatus = getText("//*[@class='govuk-tag govuk-tag--orange']", SelectorType.XPATH);
        assertEquals(status.toUpperCase(),internalStatus);
    }

    @After
    public void withDrawApplication(){
        clickByLinkText("Not taken up");
        waitForTextToBePresent("Not taken up");
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }
}