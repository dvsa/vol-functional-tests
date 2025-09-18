package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.SecretsManager;
import activesupport.string.Str;
import activesupport.system.Properties;
import apiCalls.enums.EnforcementArea;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.UserType;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.BatchCommands;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.SearchNavBar;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.dvsa.testing.framework.stepdefs.vol.AccessibilitySteps;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import scanner.AXEScanner;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;

import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.dvsa.testing.framework.stepdefs.vol.ManageApplications.existingLicenceNumber;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BusRegistrationJourney extends BasePage {
    World world;
    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    public BusRegistrationJourney(World world) {
        this.world = world;
    }

    public void internalSearchForBusReg() {
        selectValueFromDropDown("//*[@id='search-select']", SelectorType.XPATH, "Bus registrations");
        do {
            SearchNavBar.search(SearchType.Licence, world.applicationDetails.getLicenceNumber());
        } while (!isLinkPresent(world.applicationDetails.getLicenceNumber(), 60));
        waitAndClickByLinkText(world.applicationDetails.getLicenceNumber());
    }

    public void internalSiteAddBusNewReg(Integer plusOrMinusDay, Integer plusOrMinusMonth, Integer plusOrMinusYear) {
        waitForTextToBePresent("Overview");
        waitAndClickByLinkText("Bus registrations");
        waitAndClick(nameAttribute("button", "action"), SelectorType.CSS);
        waitForTextToBePresent("Service details");
        assertTrue(isTextPresent("Service No. & type"));
        waitAndEnterText("serviceNo", SelectorType.ID, "123");
        waitAndEnterText("startPoint", SelectorType.ID, Str.randomWord(9));
        waitAndEnterText("finishPoint", SelectorType.ID, Str.randomWord(11));
        waitAndEnterText("via", SelectorType.ID, Str.randomWord(5));
        waitAndClick("//*[@class='chosen-choices']", SelectorType.XPATH);
        findElements("//*[@class='active-result']", SelectorType.XPATH).stream().findFirst().get().click();

        plusOrMinusDay = plusOrMinusDay == null ? 0 : plusOrMinusDay;
        plusOrMinusMonth = plusOrMinusMonth == null ? 0 : plusOrMinusMonth;
        plusOrMinusYear = plusOrMinusYear == null ? 0 : plusOrMinusYear;

        HashMap<String, String> dates;
        dates = world.globalMethods.date.getDateHashMap(0, 0, 0);
        enterDateFieldsByPartialId("receivedDate", dates);

        dates = world.globalMethods.date.getDateHashMap(plusOrMinusDay, plusOrMinusMonth, plusOrMinusYear);
        enterDateFieldsByPartialId("effectiveDate", dates);

        dates = world.globalMethods.date.getDateHashMap(-2, 0, 0);
        enterDateFieldsByPartialId("endDate", dates);

        dates = world.globalMethods.date.getDateHashMap(0, 0, 0);
        enterDateFieldsByPartialId("applicationCompleteDate", dates);

        UniversalActions.clickSubmit();

    }

    public void closeBusReg() {
        waitAndClickByLinkText("" + world.applicationDetails.getLicenceNumber() + "");
        click("menu-bus-registration-decisions-admin-cancel", SelectorType.ID);
        waitForTextToBePresent("Update status");
        enterText("fields[reason]", SelectorType.ID, "Mistake");
        UniversalActions.clickSubmit();
    }

    public void payFeesAndGrantNewBusReg() {
        waitAndClickByLinkText("Fees");
        world.feeAndPaymentJourney.selectFee();
        world.feeAndPaymentJourney.payFee("60", "cash");
        long kickOutTime = System.currentTimeMillis() + 60000;
        do {
            refreshPageWithJavascript();
        } while (!isLinkPresent("Register service", 5) && System.currentTimeMillis() < kickOutTime);
        waitAndClickByLinkText("Register service");
        findSelectAllRadioButtonsByValue("Y");
        UniversalActions.clickSubmit();
        waitAndClickByLinkText("Service details");
        waitAndClickByLinkText("TA's");
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result\"]");
        click("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result group-option\"]");
        UniversalActions.clickSubmit();
        waitAndClick("//*[contains(text(),'Grant')]", SelectorType.XPATH);
    }

    public void createLicenceWithOpenCaseAndBusReg(String operatorType, String licenceType) throws HttpException {
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
        internalSiteAddBusNewReg(0, 5, 0);
        payFeesAndGrantNewBusReg();
        world.updateLicence.createCase();
    }

    public void viewEBSRInExternal() throws IllegalBrowserException, IOException, InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMinutes(2));
        try {
            boolean isSuccessful = wait.until(driver -> {
                refreshPageWithJavascript();
                boolean isProcessing = isTextPresent("processing");
                if (!isProcessing) {
                    return isTextPresent("Successful");
                }
                return false;
            });
            assertTrue(isSuccessful, "The status is not 'Successful' after processing.");
        } catch (TimeoutException e) {
            throw new NotFoundException("import EBSR is still displaying as 'processing' when the timeout was reached.");
        }
    }

    public void uploadAndSubmitEBSR(String state, int interval) throws MissingRequiredArgument, IOException {
        refreshPageWithJavascript();
        String ebsrFileName;

        // Set the EBSR file name based on environment
        if (world.configuration.env.equals(EnvironmentType.PREPRODUCTION)) {
            ebsrFileName = existingLicenceNumber.concat("EBSR.zip");
        } else {
            ebsrFileName = world.applicationDetails.getLicenceNumber().concat("EBSR.zip");
        }

        String existingXmlContent = GenericUtils.readXML();
        world.genericUtils.modifyXML(state, interval);
        String zipFilePath = GenericUtils.createZipFolder(ebsrFileName);

        waitAndClickByLinkText("Bus");
        waitAndClick("//*[contains(text(),'EBSR')]", SelectorType.XPATH);
        click(nameAttribute("button", "action"), SelectorType.CSS);

        waitForElementToBePresent("//*[@id='files']");
        String jScript = "document.getElementById('fields[files][file]').style.left = 0";
        javaScriptExecutor(jScript);

        String fullFilePath = System.getProperty("user.dir").concat("/" + zipFilePath);
        if (System.getProperty("platform") == null) {
            enterText("//*[@id='fields[files][file]']", SelectorType.XPATH, fullFilePath);
        } else {
            WebElement addFile = getDriver().findElement(By.xpath("//*[@id='fields[files][file]']"));
            ((RemoteWebElement) addFile).setFileDetector(new LocalFileDetector());
            addFile.sendKeys(fullFilePath);
        }

        waitForTextToBePresent("File name");
        UniversalActions.clickSubmit();
        waitForPageLoadComplete();
        GenericUtils.writeXmlStringToFile(existingXmlContent, "src/test/resources/org/dvsa/testing/framework/EBSR/EBSR.xml");
    }

    public void internalSiteEditBusReg() {
        enterText("serviceNo", SelectorType.ID, "4");
        enterText("startPoint", SelectorType.ID, Str.randomWord(2));
        enterText("finishPoint", SelectorType.ID, Str.randomWord(1));
        enterText("via", SelectorType.ID, Str.randomWord(3));
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        findElements("//*[@class='active-result']", SelectorType.XPATH).stream().findFirst().get().click();
        HashMap<String, String> dates;
        dates = world.globalMethods.date.getDateHashMap(0, 0, -1);
        enterDateFieldsByPartialId("receivedDate", dates);
        dates = world.globalMethods.date.getDateHashMap(0, 0, 1);
        enterDateFieldsByPartialId("effectiveDate", dates);
        dates = world.globalMethods.date.getDateHashMap(0, 0, 2);
        enterDateFieldsByPartialId("endDate", dates);
        UniversalActions.clickSubmit();
    }

    public static void waitForPageLoadComplete() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(8)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }
}
