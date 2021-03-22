package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.MissingRequiredArgument;
import activesupport.string.Str;
import apiCalls.enums.EnforcementArea;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.UserType;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.internal.SearchNavBar;
import org.junit.Assert;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;

import java.net.MalformedURLException;
import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;

public class BusRegistrationJourneySteps extends BasePage {

    private World world;
    private static final String zipFilePath = "/src/test/resources/ESBR.zip";

    public BusRegistrationJourneySteps(World world){
        this.world = world;
    }

    public void internalSearchForBusReg() throws IllegalBrowserException, MalformedURLException {
        selectValueFromDropDown("//*[@id='search-select']", SelectorType.XPATH, "Bus registrations");
        do {
            SearchNavBar.search(world.applicationDetails.getLicenceNumber());
        } while (!isLinkPresent(world.applicationDetails.getLicenceNumber(), 60));
        clickByLinkText(world.applicationDetails.getLicenceNumber());
    }

    public void internalSiteAddBusNewReg(int month) throws IllegalBrowserException, MalformedURLException {
        waitForTextToBePresent("Overview");
        clickByLinkText("Bus registrations");
        click(nameAttribute("button", "action"));
        waitForTextToBePresent("Service details");
        assertTrue(isTextPresent("Service No. & type", 5));
        enterText("serviceNo", "123", SelectorType.ID);
        enterText("startPoint", Str.randomWord(9), SelectorType.ID);
        enterText("finishPoint", Str.randomWord(11), SelectorType.ID);
        enterText("via", Str.randomWord(5), SelectorType.ID);
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        clickFirstElementFound("//*[@class=\"active-result\"]", SelectorType.XPATH);

        HashMap<String, String> dates;
        dates = world.globalMethods.date.getDateHashMap(0, 0, 0);
        replaceDateFieldsByPartialId("receivedDate", dates);

        dates = world.globalMethods.date.getDateHashMap(0, month, 0);
        enterText("effectiveDate_day", dates.get("day"), SelectorType.ID);
        enterText("effectiveDate_month", dates.get("month"), SelectorType.ID);
        enterText("effectiveDate_year", dates.get("year"), SelectorType.ID);
        click(nameAttribute("button", "form-actions[submit]"));

        long kickOutTime = System.currentTimeMillis() + 60000;

        do {
            // Refresh page
            javaScriptExecutor("location.reload(true)");
        }
        while (!isTextPresent("Service details", 2) && System.currentTimeMillis() < kickOutTime);
        if (System.currentTimeMillis() > kickOutTime) {
            throw new TimeoutException("Service details page didn't display as expected within the time limit.");
        }
    }

    public void closeBusReg() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("" + world.applicationDetails.getLicenceNumber() + "");
        click("menu-bus-registration-decisions-admin-cancel", SelectorType.ID);
        waitForTextToBePresent("Update status");
        enterText("fields[reason]", "Mistake", SelectorType.ID);
        click("form-actions[submit]", SelectorType.ID);
    }

    public void payFeesAndGrantNewBusReg() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("Fees");
        world.feeAndPaymentJourneySteps.selectFee();
        world.feeAndPaymentJourneySteps.payFee("60", "cash");
        long kickOutTime = System.currentTimeMillis() + 60000;
        do {
            javaScriptExecutor("location.reload(true)");
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

    public void createLicenceWithOpenCaseAndBusReg(String operatorType, String licenceType) throws IllegalBrowserException, MalformedURLException {
        if (licenceType.equals("standard_international")) {
            world.createApplication.setLicenceType("standard_international");
        } else {
            world.createApplication.setLicenceType("standard_national");
        }
        world.createApplication.setTrafficArea(TrafficArea.valueOf(TrafficArea.NORTH_EAST.name()));
        world. createApplication.setEnforcementArea(EnforcementArea.valueOf(EnforcementArea.NORTH_EAST.name()));
        world.createApplication.setOperatorType(operatorType);
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourneySteps.createApplication();
        world.APIJourneySteps.submitApplication();
        if (String.valueOf(operatorType).equals("public")) {
            world.APIJourneySteps.grantLicenceAndPayFees();
            System.out.println("Licence: " + world.applicationDetails.getLicenceNumber());
        } else {
            world.APIJourneySteps.grantLicenceAndPayFees();
            System.out.println("Licence: " + world.applicationDetails.getLicenceNumber());
        }
        world.APIJourneySteps.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalNavigation.urlSearchAndViewLicence();
        internalSiteAddBusNewReg(5);
        payFeesAndGrantNewBusReg();
        world.updateLicence.createCase();
    }

    public void viewESBRInExternal() throws IllegalBrowserException, MalformedURLException {

        long kickOutTime = System.currentTimeMillis() + 120000;

        do {
            // Refresh page
            javaScriptExecutor("location.reload(true)");
        } while (isTextPresent("processing", 60) && System.currentTimeMillis() < kickOutTime);

        try {
            Assert.assertTrue(isTextPresent("Successful", 60));
        } catch (Exception e) {
            throw new NotFoundException("ESBR is still displaying as 'processing' when kick out time was reached.");
        }
    }

    public void uploadAndSubmitESBR(String state, int interval) throws MissingRequiredArgument, IllegalBrowserException, MalformedURLException {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        world.genericUtils.modifyXML(state, interval);
        GenericUtils.zipFolder();
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());

        clickByLinkText("Bus registrations");
        waitAndClick("//*[contains(text(),'EBSR')]", SelectorType.XPATH);
        click(nameAttribute("button", "action"));
        String workingDir = System.getProperty("user.dir");
        uploadFile("//*[@id='fields[files][file]']", workingDir + zipFilePath, "document.getElementById('fields[files][file]').style.left = 0", SelectorType.XPATH);
        waitAndClick("//*[@name='form-actions[submit]']", SelectorType.XPATH);
    }
}
