package org.dvsa.testing.framework.stepdefs.vol;

import apiCalls.enums.EnforcementArea;
import apiCalls.enums.OperatorType;
import io.cucumber.java.en.And;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.aws.s3.S3;
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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


import static apiCalls.enums.TrafficArea.trafficAreaList;
import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ManageApplications extends BasePage {
    World world;
    Initialisation initialisation;
    String fileName = "src/test/resources/";
    public static String existingLicenceNumber;

    ReadWriteLock lock = new ReentrantReadWriteLock();

    public ManageApplications(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("i have a self serve account")
    public synchronized void iHaveASelfServeAccount() {
        world.userRegistrationJourney.navigateAndLogIntoSelfServiceWithExistingUser();
    }

    @Given("I have a {string} {string} application with {string} vehicles and a vehicleAuthority of {string}")
    public synchronized void iHaveANewApplicationWithVehiclesAndVehicleAuthorityOf(String operatorType, String licenceType, String numberOfVehicles, String authority) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.createApplication.setNoOfAddedHgvVehicles(Integer.parseInt(numberOfVehicles));
            world.createApplication.setTotalOperatingCentreHgvAuthority(Integer.parseInt(authority));
            world.createApplication.setNoOfOperatingCentreVehicleAuthorised(Integer.parseInt(authority));
            world.licenceCreation.createApplication(operatorType, licenceType);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have a {string} {string} application")
    public synchronized void iHaveAnStringStringApplication(String operatorType, String licenceType) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.licenceCreation.createApplication(operatorType, licenceType);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have a submitted {string} {string} application")
    public synchronized void iHaveASubmittedApplication(String operatorType, String licenceType) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.licenceCreation.createSubmittedApplication(operatorType, licenceType);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("i have a valid {string} {string} licence with an open case and bus reg")
    public synchronized void iHaveAValidLicenceWithAnOpenCaseAndBusReg(String operatorType, String licenceType) throws HttpException {
        lock.writeLock().lock();
        try {
            world.busRegistrationJourney.createLicenceWithOpenCaseAndBusReg(operatorType, licenceType);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have all {string} {string} Traffic Areas applications with an external TM")
    public synchronized void iHaveAppliedForTMApplication(String operatorType, String licenceType) throws Exception {
        String password;
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.createApplication.setNoOfAddedHgvVehicles(3);
            for (TrafficArea ta : trafficAreaList()) {
                world.licenceCreation.createApplicationWithTrafficArea(operatorType, licenceType, ta);
                password = S3.getTempPassword(world.createApplication.getTransportManagerEmailAddress());
                world.genericUtils.writeToFile(world.createApplication.getTransportManagerUserName(), password, fileName.concat("TM.csv"));
                world.createApplication.setApplicationId(null);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Then("i write the licence login information to a file for use of user research")
    public synchronized void theLicenceShouldBeCreatedAndGranted() throws Exception {
        lock.writeLock().lock();
        try {
            world.genericUtils.writeToFile(world.registerUser.getUserName(), world.globalMethods.getLoginPassword(), fileName.concat("Operator.csv"));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have {string} {string} {string} licences")
    public synchronized void iHaveNumberLicences(String noOfLicences, String operatorType, String licenceType) throws HttpException {
        lock.writeLock().lock();
        try {
            if (Integer.parseInt(noOfLicences) > 9) {
                throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
            }
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            for (int i = 0; i < Integer.parseInt(noOfLicences); i++) {
                world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, trafficAreaList()[i]);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have all {string} {string} traffic area licences")
    public synchronized void iHaveAllTrafficAreaForLicences(String operatorType, String licenceType) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.createApplication.setNoOfAddedHgvVehicles(3);
            for (TrafficArea ta : trafficAreaList()) {
                world.licenceCreation.createApplicationWithTrafficArea(operatorType, licenceType, ta);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have a {string} {string} licence in {string} traffic area")
    public synchronized void iHaveALicenceInTrafficArea(String operatorType, String licenceType, String trafficArea) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.createApplication.setNoOfAddedHgvVehicles(3);
            world.licenceCreation.createApplicationWithTrafficArea(operatorType, licenceType, TrafficArea.valueOf(trafficArea));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have a {string} {string} application with {string} vehicles")
    public synchronized void iHaveAnApplicationWithVehicles(String operatorType, String licenceType, String vehicles) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.licenceCreation.createApplicationWithVehicles(operatorType, licenceType, vehicles);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have a {string} {string} licence")
    public synchronized void iHaveLicence(String operatorType, String licenceType) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.licenceCreation.createLicence(operatorType, licenceType);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have a {string} {string} licence with {string} vehicle authorisation")
    public synchronized void iHaveLicenceWithVehicles(String operatorType, String licenceType, String vehicles) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.licenceCreation.createLicenceWithVehicles(operatorType, licenceType, vehicles);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have a {string} {string} NI licence")
    public synchronized void iHaveNILicence(String operatorType, String licenceType) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.licenceCreation.createNILicence(operatorType, licenceType);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("I have {string} {string} {string} licences with {string} vehicles and a vehicleAuthority of {string}")
    public synchronized void iHaveLicencesWithVehiclesAndAVehicleAuthorityOf(String noOfLicences, String operatorType, String licenceType, String vehicles, String vehicleAuth) throws HttpException {
        lock.writeLock().lock();
        try {
            if (Integer.parseInt(noOfLicences) > 9) {
                throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
            }
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.createApplication.setNoOfAddedHgvVehicles(Integer.parseInt(vehicles));
            world.createApplication.setTotalOperatingCentreHgvAuthority(Integer.parseInt(vehicleAuth));
            world.createApplication.setNoOfOperatingCentreVehicleAuthorised(Integer.parseInt(vehicleAuth));
            for (int i = 0; i < Integer.parseInt(noOfLicences); i++) {
                world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, trafficAreaList()[i]);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("i have a {string} {string} {string} application in traffic area")
    public synchronized void iHaveAnApplicationInTrafficArea(String operatorType, String licenceType, String Region, DataTable trafficAreaTable) throws HttpException {
        if (Region.equalsIgnoreCase("NI")) {
            Region = "Y";
        } else {
            Region = "N";
        }
        world.createApplication.setNiFlag(Region);

        List<String> trafficAreas = trafficAreaTable.asList(String.class);
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());

        for (String ta : trafficAreas) {
            try {
                lock.writeLock().lock();
                TrafficArea trafficArea = TrafficArea.valueOf(ta.toUpperCase());
                world.licenceCreation.createApplicationWithTrafficArea(operatorType, licenceType, trafficArea);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid TrafficArea: " + ta);
                throw e;
            } catch (HttpException e) {
                System.err.println("HTTP Exception: " + e.getMessage());
                throw e;
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    //TODO will need changing when lgv authority and hgv authority is split.
    @Given("I have {string} {string} {string} licences with {string} HGVs and {string} LGVs with a vehicleAuthorities of {string} and {string}")
    public synchronized void iHaveLicencesWithVehiclesAndAVehicleAuthorityOf(String noOfLicences, String operatorType, String licenceType, String hgvs, String lgvs, String hgvAuth, String lgvAuth) throws HttpException {
        if (Integer.parseInt(noOfLicences) > 9) {
            throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
        }
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.createApplication.setNoOfAddedHgvVehicles(Integer.parseInt(hgvs));
        world.createApplication.setNoOfAddedLgvVehicles(Integer.parseInt(lgvs));
        world.createApplication.setTotalOperatingCentreHgvAuthority(Integer.parseInt(hgvs));
        world.createApplication.setTotalOperatingCentreLgvAuthority(Integer.parseInt(lgvs));

        for (int i = 0; i < Integer.parseInt(noOfLicences); i++) {
            TrafficArea ta = trafficAreaList()[i];
            world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, ta);
        }
    }

    @Given("I have a {string} {string} licence with {int} operating centres")
    public synchronized void iHaveALicenceWithOperatingCentres(String operatorType, String licenceType, int numberOfOperatingCentres) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createApplication(operatorType, licenceType);
        for (int i = 1; i < numberOfOperatingCentres; i++)
            world.createApplication.addOperatingCentre();
        world.APIJourney.submitApplication();
        world.APIJourney.grantLicenceAndPayFees();
    }

    @Given("I have a {string} lgv only application")
    public synchronized void iHaveALgvOnlyApplication(String NIFlag) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLGVOnlyApplication(NIFlag);
    }

    @Given("I have a submitted {string} lgv only application")
    public synchronized void iHaveASubmittedLgvOnlyApplication(String NIFlag) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createSubmittedLGVOnlyApplication(NIFlag);
    }

    @Given("I have a submitted {string} lgv only application with interim")
    public synchronized void iHaveASubmittedLgvOnlyApplicationWithInterim(String NIFlag) throws HttpException {
        world.createApplication.setIsInterim("Y");
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createSubmittedLGVOnlyApplication(NIFlag);
    }

    @Given("I have a valid {string} lgv only licence")
    public synchronized void iHaveAValidLgvOnlyLicence(String NIFlag) throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLGVOnlyLicence(NIFlag);
    }

    @Given("i have a valid {string} {string} licence")
    public synchronized void iHaveAValidLicence(String operatorType, String licenceType) throws HttpException {
        lock.writeLock().lock();
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLicence(operatorType, licenceType);
        lock.writeLock().unlock();
    }

    @Given("i have a {string} application in progress")
    public synchronized void iHaveAnApplicationInProgress(String operatorType) throws HttpException {
        lock.writeLock().lock();
        try {
            if (operatorType.equals("Goods")) {
                operatorType = OperatorType.GOODS.name();
            } else {
                operatorType = OperatorType.PUBLIC.name();
            }
            world.createApplication.setOperatorType(operatorType);
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.APIJourney.createApplication();
            refreshPageWithJavascript();
            world.selfServeNavigation.navigateToPage("application", SelfServeSection.TYPE_OF_LICENCE);
            world.selfServeNavigation.navigateThroughApplication();
            if (!world.configuration.env.equals("local")) {
                world.selfServeUIJourney.signDeclaration();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @And("the licence status is {string}")
    public synchronized void theLicenceStatusIs(String arg0) throws HttpException {
        world.updateLicence.updateLicenceStatus(arg0);
    }

    @Given("as a {string} I have a psv application with traffic area {string} and enforcement area {string} which has been granted")
    public synchronized void iHaveAPsvApplicationWithTrafficAreaAndEnforcementAreaAndUserTypeWhichHasBeenGranted(String userType, String trafficArea, String enforcementArea) throws HttpException {
        // Validate trafficArea
        try {
            TrafficArea.valueOf(trafficArea.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid traffic area: " + trafficArea, e);
        }
        // Validate enforcementArea
        try {
            EnforcementArea.valueOf(enforcementArea.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enforcement area: " + enforcementArea, e);
        }
        world.APIJourney.generateAndGrantPsvApplicationPerTrafficArea(trafficArea, enforcementArea, userType);
    }

    @Given("i have an interim {string} {string} application")
    public synchronized void iHaveAnInterimApplication(String operatorType, String licenceType) throws Exception {
        if (operatorType.equals("public")) {
            throw new Exception("PSV licences cannot have interim applications.");
        }
        world.createApplication.setOperatorType(operatorType);
        world.createApplication.setLicenceType(licenceType);
        world.createApplication.setIsInterim("Y");
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        if (licenceType.equals("special_restricted") && (world.createApplication.getApplicationId() == null)) {
            world.APIJourney.createSpecialRestrictedLicence();
        } else if (world.createApplication.getApplicationId() == null) {
            world.APIJourney.createApplication();
            world.APIJourney.submitApplication();
        }
    }

    @Given("i have an application with a transport manager")
    public synchronized void iHaveAnApplicationWithATransportManager() throws HttpException {
        lock.writeLock().lock();
        if (world.createApplication.getOperatorType() == null) {
            world.createApplication.setOperatorType("public");
        }
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourney.createApplication();
        world.APIJourney.submitApplication();
        lock.writeLock().unlock();
    }

    @And("the application status should be {string}")
    public void theApplicationStatusShouldBe(String status) throws InterruptedException {
        waitForTextToBePresent(status);
        String internalStatus = getText("//*[@class='govuk-tag govuk-tag--orange']", SelectorType.XPATH);
        assertEquals(status.toUpperCase(), internalStatus);
        withDrawApplication();
    }

    @Given("I have an existing licence {string}")
    public void iHaveAnExistingLicence(String licenceNumber) {
        world.userRegistrationJourney.navigateAndLogIntoSelfServiceWithExistingUser();
        existingLicenceNumber = licenceNumber;
        clickByLinkText(existingLicenceNumber);
    }

    public static void withDrawApplication() {
        clickByLinkText("Not taken up");
        waitForTextToBePresent("Not taken up");
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }

    @Then("the application should be under consideration")
    public void theApplicationShouldBeUnderConsideration() {
        waitForTitleToBePresent("Application overview");
        assertTrue(isTextPresent("Under Consideration"));
    }



    @Given("as a {string} I have a valid {string} {string} licence")
    public synchronized void iHaveALicenceAs(String userType, String operatorType, String licenceType) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(userType);
            world.licenceCreation.createLicence(operatorType, licenceType);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Given("as a {string} I have a {string} {string} application")
    public synchronized void iHaveAnApplicationAs(String userType, String operatorType, String licenceType) throws HttpException {
        lock.writeLock().lock();
        try {
            world.APIJourney.registerAndGetUserDetails(userType);
            world.licenceCreation.createApplication(operatorType, licenceType);
        } finally {
            lock.writeLock().unlock();
        }
    }


    @Given("as a {string} I have {string} {string} {string} licences with {string} vehicles and a vehicleAuthority of {string}")
    public synchronized void asAIHaveLicencesWithVehiclesAndAVehicleAuthorityOf(String userType, String noOfLicences, String operatorType, String licenceType, String vehicles, String vehicleAuth) throws HttpException {
        lock.writeLock().lock();
        try {
            if (Integer.parseInt(noOfLicences) > 9) {
                throw new InvalidArgumentException("You cannot have more than 9 licences because there are only 9 traffic areas.");
            }
            world.APIJourney.registerAndGetUserDetails(userType);
            world.createApplication.setNoOfAddedHgvVehicles(Integer.parseInt(vehicles));
            world.createApplication.setTotalOperatingCentreHgvAuthority(Integer.parseInt(vehicleAuth));
            world.createApplication.setNoOfOperatingCentreVehicleAuthorised(Integer.parseInt(vehicleAuth));
            for (int i = 0; i < Integer.parseInt(noOfLicences); i++) {
                world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, trafficAreaList()[i]);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}