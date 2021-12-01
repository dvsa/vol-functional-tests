package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.S3;
import activesupport.string.Str;
import apiCalls.enums.EnforcementArea;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.UserType;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.SearchNavBar;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.junit.Assert;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;

import java.io.File;
import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;

public class BusRegistrationJourney extends BasePage {

    private World world;

    public BusRegistrationJourney(World world) {
        this.world = world;
    }

    public void internalSearchForBusReg() {
        selectValueFromDropDown("//*[@id='search-select']", SelectorType.XPATH, "Bus registrations");
        do {
            SearchNavBar.search(SearchType.Licence, world.applicationDetails.getLicenceNumber());
        } while (!isLinkPresent(world.applicationDetails.getLicenceNumber(), 60));
        clickByLinkText(world.applicationDetails.getLicenceNumber());
    }

    public void internalSiteAddBusNewReg(int month) {
        waitForTextToBePresent("Overview");
        clickByLinkText("Bus registrations");
        click(nameAttribute("button", "action"), SelectorType.CSS);
        waitForTextToBePresent("Service details");
        assertTrue(isTextPresent("Service No. & type"));
        enterText("serviceNo", SelectorType.ID, "123");
        enterText("startPoint", SelectorType.ID, Str.randomWord(9));
        enterText("finishPoint", SelectorType.ID, Str.randomWord(11));
        enterText("via", SelectorType.ID, Str.randomWord(5));
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        findElements("//*[@class='active-result']", SelectorType.XPATH).stream().findFirst().get().click();

        HashMap<String, String> dates;
        dates = world.globalMethods.date.getDateHashMap(0, 0, 0);
        replaceDateFieldsByPartialId("receivedDate", dates);

        dates = world.globalMethods.date.getDateHashMap(0, month, 0);
        enterText("effectiveDate_day", SelectorType.ID, dates.get("day"));
        enterText("effectiveDate_month", SelectorType.ID, dates.get("month"));
        enterText("effectiveDate_year", SelectorType.ID, dates.get("year"));
        click(nameAttribute("button", "form-actions[submit]"), SelectorType.CSS);

        long kickOutTime = System.currentTimeMillis() + 60000;

        do {
            // Refresh page
            refreshPageWithJavascript();
        }
        while (!isTextPresent("Service details") && System.currentTimeMillis() < kickOutTime);
        if (System.currentTimeMillis() > kickOutTime) {
            throw new TimeoutException("Service details page didn't display as expected within the time limit.");
        }
    }

    public void closeBusReg() {
        clickByLinkText("" + world.applicationDetails.getLicenceNumber() + "");
        click("menu-bus-registration-decisions-admin-cancel", SelectorType.ID);
        waitForTextToBePresent("Update status");
        enterText("fields[reason]", SelectorType.ID, "Mistake");
        click("form-actions[submit]", SelectorType.ID);
    }

    public void payFeesAndGrantNewBusReg() {
        clickByLinkText("Fees");
        world.feeAndPaymentJourney.selectFee();
        world.feeAndPaymentJourney.payFee("60", "cash");
        long kickOutTime = System.currentTimeMillis() + 60000;
        do {
            refreshPageWithJavascript();
        } while (!isLinkPresent("Register service", 5) && System.currentTimeMillis() < kickOutTime);
        clickByLinkText("Register service");
        findSelectAllRadioButtonsByValue("Y");
        clickByName("form-actions[submit]");
        clickByLinkText("Service details");
        clickByLinkText("TA's");
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result\"]");
        click("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result group-option\"]");
        clickByName("form-actions[submit]");
        waitAndClick("//*[contains(text(),'Grant')]", SelectorType.XPATH);
    }

    public void createLicenceWithOpenCaseAndBusReg(String operatorType, String licenceType) {
        if (licenceType.equals("standard_international")) {
            world.createApplication.setLicenceType("standard_international");
        } else {
            world.createApplication.setLicenceType("standard_national");
        }
        world.createApplication.setTrafficArea(TrafficArea.valueOf(TrafficArea.NORTH_EAST.name()));
        world.createApplication.setEnforcementArea(EnforcementArea.valueOf(EnforcementArea.NORTH_EAST.name()));
        world.createApplication.setOperatorType(operatorType);
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourney.createApplication();
        world.APIJourney.submitApplication();
        if (String.valueOf(operatorType).equals("public")) {
            world.APIJourney.grantLicenceAndPayFees();
            System.out.println("Licence: " + world.applicationDetails.getLicenceNumber());
        } else {
            world.APIJourney.grantLicenceAndPayFees();
            System.out.println("Licence: " + world.applicationDetails.getLicenceNumber());
        }
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getLicence();
        internalSiteAddBusNewReg(5);
        payFeesAndGrantNewBusReg();
        world.updateLicence.createCase();
    }

    public void viewEBSRInExternal() {

        long kickOutTime = System.currentTimeMillis() + 120000;

        do {
            // Refresh page
            refreshPageWithJavascript();
        } while (isTextPresent("processing") && System.currentTimeMillis() < kickOutTime);

        try {
            Assert.assertTrue(isTextPresent("Successful"));
        } catch (Exception e) {
            throw new NotFoundException("import EBSR is still displaying as 'processing' when kick out time was reached.");
        }
    }

    public void uploadAndSubmitEBSR(String state, int interval) throws MissingRequiredArgument {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        String ebsrFileName = world.applicationDetails.getLicenceNumber().concat("EBSR.zip");
        String path = String.format("BusReg/%s", ebsrFileName);

        world.genericUtils.modifyXML(state, interval);
        String zipFilePath = GenericUtils.createZipFolder(ebsrFileName);
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());

        clickByLinkText("Bus registrations");
        waitAndClick("//*[contains(text(),'EBSR')]", SelectorType.XPATH);
        click(nameAttribute("button", "action"), SelectorType.CSS);

        String jScript = "document.getElementById('fields[files][file]').style.left = 0";
        javaScriptExecutor(jScript);

        if (System.getProperty("platform") == null) {
            enterText("//*[@id='fields[files][file]']", SelectorType.XPATH, zipFilePath);
        } else {
            S3.uploadObject(world.configuration.getBucketName(), path, System.getProperty("user.dir").concat(zipFilePath));
            //get Pat
            if (S3.getS3Object(world.configuration.getBucketName(), path).getKey().contains(ebsrFileName)){
                System.out.println("I AM HERE+++++++++++++++++++++++++++++");
                S3.downloadObject(world.configuration.getBucketName(), path, "/var/scratch/EBSR".concat(ebsrFileName));
                System.out.println("DOWNLOADED+++++++++++++++++++++++++");
                enterText("//*[@id='fields[files][file]']", SelectorType.XPATH, "var/scratch/EBSR/".concat(ebsrFileName));
                System.out.println("ENTERED+++++++++++++");
            }
        }
        waitAndClick("//*[@name='form-actions[submit]']", SelectorType.XPATH);
    }
}
