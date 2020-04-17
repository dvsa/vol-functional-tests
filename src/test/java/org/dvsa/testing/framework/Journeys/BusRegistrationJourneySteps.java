package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.MissingRequiredArgument;
import activesupport.string.Str;
import enums.UserRoles;
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
            SearchNavBar.search(world.createLicence.getLicenceNumber());
        } while (!isLinkPresent(world.createLicence.getLicenceNumber(), 60));
        clickByLinkText(world.createLicence.getLicenceNumber());
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

        HashMap<String, Integer> dates;
        dates = world.globalMethods.date.getDate(0, 0, 0);
        replaceDateById("receivedDate", dates);

        dates = world.globalMethods.date.getDate(0, month, 0);
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
        clickByLinkText("" + world.createLicence.getLicenceNumber() + "");
        click("menu-bus-registration-decisions-admin-cancel", SelectorType.ID);
        waitForTextToBePresent("Update status");
        enterText("fields[reason]", "Mistake", SelectorType.ID);
        click("form-actions[submit]", SelectorType.ID);
    }

    public void payFeesAndGrantNewBusReg() throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("Fees");
        world.UIJourneySteps.selectFee();
        world.UIJourneySteps.payFee("60", "cash");
        do {
            System.out.println("link not present");
            javaScriptExecutor("location.reload(true)");
        } while (!isLinkPresent("Register service", 5));
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
        if (licenceType.equals("si")) {
            world.createLicence.setLicenceType("standard_international");
        } else if (licenceType.equals("sn")) {
            world.createLicence.setLicenceType("standard_national");
        } else {
            world.createLicence.setLicenceType("standard_national");
        }
        world.createLicence.setTrafficArea("B");
        world.createLicence.setEnforcementArea("EA-B");
        world.createLicence.setOperatorType(operatorType);
        world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
        world.APIJourneySteps.createApplication();
        world.APIJourneySteps.submitApplication();
        if (String.valueOf(operatorType).equals("public")) {
            world.APIJourneySteps.grantLicenceAndPayFees();
            System.out.println("Licence: " + world.createLicence.getLicenceNumber());
        } else {
            world.APIJourneySteps.grantLicenceAndPayFees();
            System.out.println("Licence: " + world.createLicence.getLicenceNumber());
        }
        world.APIJourneySteps.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
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
        world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());

        clickByLinkText("Bus registrations");
        waitAndClick("//*[@id='main']/div[2]/ul/li[2]/a", SelectorType.XPATH);
        click(nameAttribute("button", "action"));
        String workingDir = System.getProperty("user.dir");
        uploadFile("//*[@id='fields[files][file]']", workingDir + zipFilePath, "document.getElementById('fields[files][file]').style.left = 0", SelectorType.XPATH);
        waitAndClick("//*[@name='form-actions[submit]']", SelectorType.XPATH);
    }
}
