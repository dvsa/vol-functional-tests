package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.S3;
import activesupport.string.Str;
import apiCalls.enums.EnforcementArea;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.UserType;
import com.amazonaws.services.s3.AmazonS3;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.SearchNavBar;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;

public class BusRegistrationJourney extends BasePage {
    private static AmazonS3 client = null;
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
        enterDateFieldsByPartialId("receivedDate", dates);

        dates = world.globalMethods.date.getDateHashMap(0, month, 0);
        enterDateFieldsByPartialId("effectiveDate", dates);
        world.UIJourney.clickSubmit();

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
        world.UIJourney.clickSubmit();
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
        world.UIJourney.clickSubmit();
        clickByLinkText("Service details");
        clickByLinkText("TA's");
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result\"]");
        click("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result group-option\"]");
        world.UIJourney.clickSubmit();
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
        } else {
            world.APIJourney.grantLicenceAndPayFees();
        }
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        internalSiteAddBusNewReg(5);
        payFeesAndGrantNewBusReg();
        world.updateLicence.createCase();
    }

    public void viewEBSRInExternal() {
        long kickOutTime = System.currentTimeMillis() + 250000;
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
        refreshPageWithJavascript();
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        String ebsrFileName = world.applicationDetails.getLicenceNumber().concat("EBSR.zip");
        world.genericUtils.modifyXML(state, interval);
        String zipFilePath = GenericUtils.createZipFolder(ebsrFileName);

        clickByLinkText("Bus");
        waitAndClick("//*[contains(text(),'EBSR')]", SelectorType.XPATH);
        click(nameAttribute("button", "action"), SelectorType.CSS);

        String jScript = "document.getElementById('fields[files][file]').style.left = 0";
        javaScriptExecutor(jScript);

        if (System.getProperty("platform") == null) {
            enterText("//*[@id='fields[files][file]']", SelectorType.XPATH, System.getProperty("user.dir").concat("/"+zipFilePath));
        } else {
            WebElement addFile = getDriver().findElement(By.xpath("//*[@id='fields[files][file]']"));
            ((RemoteWebElement)addFile).setFileDetector(new LocalFileDetector());
            addFile.sendKeys(System.getProperty("user.dir").concat("/"+zipFilePath));
        }
        world.UIJourney.clickSubmit();
    }
}