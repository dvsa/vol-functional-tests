package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import activesupport.file.TestResourceReader;
import activesupport.system.Properties;
import apiCalls.enums.LicenceType;
import apiCalls.enums.OperatorType;
import com.typesafe.config.Config;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ContinuationJourneySteps extends BasePage {

    private World world;
    private EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    public ContinuationJourneySteps(World world) {
        this.world = world;
    }

    public void generateContinuationOnInternal(String licenceNo, String licenceTrafficArea, String month) throws IllegalBrowserException, MalformedURLException {
        click("//*[contains(text(),'Admin')]", SelectorType.XPATH);
        click("menu-admin-dashboard/continuations", SelectorType.ID);
        waitForElementToBePresent("//*[@id='generate-continuation-type']");
        selectValueFromDropDownByIndex("details[date][month]", SelectorType.NAME, Integer.parseInt(month) - 1); // Minus one in the month because of indexing.
        selectValueFromDropDown("generate-continuation-trafficArea", SelectorType.ID, licenceTrafficArea);
        click("form-actions[generate]", SelectorType.ID);
        enterText("filters[licenceNo]", licenceNo,  SelectorType.ID);
        click("main", SelectorType.ID);
        waitForTextToBePresent("1 licence(s)");
        waitAndClick("id[]", SelectorType.NAME);
        click("generate", SelectorType.ID);
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("The selected licence(s) have been queued");
    }

    public void continueLicenceWithVerifyAndPay() throws IllegalBrowserException, MalformedURLException {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
        clickContinueLicenceOnSelfServe();
        click("submit", SelectorType.ID);
        completeContinuationsReviewPage();
        completeContinuationConditionsAndUndertakingsPage();
        completeContinuationFinancesPage();
        completeContinuationsSignPage();
        completeContinuationPayOrSubmit();
    }

    public void clickContinueLicenceOnSelfServe() throws IllegalBrowserException, MalformedURLException {
        world.selfServeNavigation.navigateToPage("licence", "View");
        refreshPageUntilElementAppears("//*[@class='info-box info-box--pink']", SelectorType.XPATH);
        click("//a[contains(text(),'Continue licence')]", SelectorType.XPATH);
    }

    public void completeContinuationFinancesPage() throws IllegalBrowserException, MalformedURLException {
        if (!(world.createApplication.getOperatorType().equals(OperatorType.PUBLIC.asString()) && world.createApplication.getLicenceType().equals(LicenceType.SPECIAL_RESTRICTED.asString()))) {
            String necessaryIncome = Browser.navigate().findElement(By.xpath("//strong[contains(text(),'£')]")).getText().replaceAll("[£,]","");
            enterText("averageBalance", necessaryIncome, SelectorType.ID);
            findSelectAllRadioButtonsByValue("N");
            click("submit", SelectorType.ID);
        }
    }

    public void viewContinuationSnapshotOnInternal() throws IllegalBrowserException, MalformedURLException {
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalNavigation.urlSearchAndViewApplication();
        clickByLinkText("Docs & attachments");
        refreshPageUntilElementAppears("//*[contains(text(), 'Digital continuation snapshot')]", SelectorType.XPATH);
        Assert.assertTrue(isTextPresent("Digital continuation snapshot", 10));
        clickByLinkText("Digital continuation snapshot");
        waitForTabsToLoad(2, 60);
        ArrayList<String> tabs = new ArrayList<String> (getWindowHandles());
        switchToWindow(tabs.get(1));
    }

    public void replaceContinuationAndReviewDates(LinkedHashMap<String, String> continuationDates, LinkedHashMap<String, String> reviewDates) throws IllegalBrowserException, MalformedURLException {
        waitForTextToBePresent("Continuation date");
        replaceDateFieldsByPartialId("details[continuationDate]", continuationDates);
        replaceDateFieldsByPartialId("details[reviewDate]", reviewDates);
        click("form-actions[submit]", SelectorType.ID);
        waitForElementToBeClickable("form-actions[submit]", SelectorType.ID);
    }

    public void completeContinuationPayOrSubmit() throws IllegalBrowserException, MalformedURLException {
        if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString()) || world.createApplication.getLicenceType().equals(LicenceType.SPECIAL_RESTRICTED.asString())) {
            click("submitAndPay", SelectorType.ID);
            click("form-actions[pay]", SelectorType.ID);
            world.feeAndPaymentJourneySteps.customerPaymentModule();
        } else {
            click("submit", SelectorType.ID);
        }
        waitForTextToBePresent("Your licence has been continued");
    }

    public void completeContinuationsReviewPage() throws MalformedURLException, IllegalBrowserException {
        clickAllCheckboxes();
        findSelectAllRadioButtonsByValue("Y");
        click("licenceChecklistConfirmation[yesContent][submit]", SelectorType.ID);
    }

    public void completeContinuationsSignPage() throws IllegalBrowserException, MalformedURLException {
        click("content[signatureOptions]", SelectorType.ID);
        click("sign", SelectorType.ID);
        world.UIJourneySteps.signWithVerify();
        waitForTextToBePresent("Declaration signed through GOV.UK Verify");
    }

    public void completeContinuationConditionsAndUndertakingsPage() throws MalformedURLException, IllegalBrowserException {
        if (!world.createApplication.getLicenceType().equals(LicenceType.SPECIAL_RESTRICTED.asString())) {
            if (world.createApplication.getOperatorType().equals(OperatorType.PUBLIC.asString()) &&
                    (world.createApplication.getLicenceType().equals(LicenceType.RESTRICTED.asString()) || !world.createApplication.getPsvVehicleSize().equals("psvvs_medium_large"))) {
                waitForTextToBePresent("You must review and comply with any conditions and undertakings.");
                clickAllCheckboxes();
                click("submit", SelectorType.ID);
            }
        }
    }

    public void checkPSVRestrictedConditionsAndUndertakingsText() throws FileNotFoundException {
        Config testFile = new TestResourceReader("testResources/PSV/PSVRestrictedConditionsAndUndertakings.properties").getFile();
        Assert.assertTrue(isTextPresent(testFile.getString("conditionsOne"), 10));
        Assert.assertTrue(isTextPresent(testFile.getString("conditionsTwo"), 10));
        Assert.assertTrue(isTextPresent(testFile.getString("conditionsThree"), 10));
        Assert.assertTrue(isTextPresent(testFile.getString("undertakingsOne"), 10));
        Assert.assertTrue(isTextPresent(testFile.getString("undertakingsTwo"), 10));
    }

    public void checkContinuationReviewSections() throws IllegalBrowserException {
        Assert.assertTrue(isTextPresent("Type of licence", 10));
        Assert.assertTrue(isTextPresent("Business type", 10));
        Assert.assertTrue(isTextPresent("Business details", 10));
        Assert.assertTrue(isTextPresent("Addresses", 10));
        Assert.assertTrue(isTextPresent("Directors", 10));
        if (!world.createApplication.getLicenceType().equals(LicenceType.SPECIAL_RESTRICTED.asString())){
            Assert.assertTrue(isTextPresent("Operating centres and authorisation", 10));
            Assert.assertTrue(isTextPresent("Safety and compliance", 10));
            if (!world.createApplication.getLicenceType().equals(LicenceType.RESTRICTED.asString())) {
                Assert.assertTrue(isTextPresent("Transport managers", 10));
            }
        }
        if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString())) {
            // 'Vehicle' targeting is fine on snapshot.
            if (isTextPresent("Print this page", 10)) {
                Assert.assertTrue(isTextPresent("Vehicles", 10));
            } else {
                // Selenium is struggling to target Vehicle title on Self Serve.
                Assert.assertTrue(isElementPresent("vehiclesCheckbox", SelectorType.ID));
            }
        }
        Assert.assertTrue(isTextPresent("User access",10));
    }
}
