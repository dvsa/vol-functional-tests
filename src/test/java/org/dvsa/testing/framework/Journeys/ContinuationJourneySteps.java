package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.config.Configuration;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import com.typesafe.config.Config;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ContinuationJourneySteps extends BasePage {

    private World world;
    private EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    public ContinuationJourneySteps(World world) {
        this.world = world;
    }

    public void generateContinuationOnInternal(String licenceNo, String licenceTrafficArea, int month) throws IllegalBrowserException, MalformedURLException {
        click("//*[contains(text(),'Admin')]", SelectorType.XPATH);
        click("menu-admin-dashboard/continuations", SelectorType.ID);
        waitForElementToBePresent("//*[@id='generate-continuation-type']");
        selectValueFromDropDownByIndex("details[date][month]", SelectorType.NAME, month - 1); // Minus one in the month because of indexing.
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
        world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
        clickContinueLicenceOnSelfServe();
        click("submit", SelectorType.ID);
        completeContinuationsReviewPage();
        completeContinuationConditionsAndUndertakingsPage();
        completeContinuationFinancesPage();
        completeContinuationsSignPage();
        completeContinuationPayOrSubmit();
    }

    public void clickContinueLicenceOnSelfServe() throws IllegalBrowserException, MalformedURLException {
        world.UIJourneySteps.navigateToSelfServePage("licence", "view");
        refreshPageUntilElementAppears("//*[@class='info-box info-box--pink']", SelectorType.XPATH);
        click("//a[contains(text(),'Continue licence')]", SelectorType.XPATH);
    }

    public void completeContinuationFinancesPage() throws IllegalBrowserException, MalformedURLException {
        if (!(world.createLicence.getOperatorType().equals("public") && world.createLicence.getLicenceType().equals("special_restricted"))) {
            String necessaryIncome = Browser.navigate().findElement(By.xpath("//strong[contains(text(),'£')]")).getText().replaceAll("[£,]","");
            enterText("averageBalance", necessaryIncome, SelectorType.ID);
            findSelectAllRadioButtonsByValue("N");
            click("submit", SelectorType.ID);
        }
    }

    public void viewContinuationSnapshotOnInternal() throws IllegalBrowserException, MalformedURLException {
        world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        world.UIJourneySteps.urlSearchAndViewApplication();
        clickByLinkText("Docs & attachments");
        refreshPageUntilElementAppears("//*[contains(text(), 'Digital continuation snapshot')]", SelectorType.XPATH);
        Assert.assertTrue(isTextPresent("Digital continuation snapshot", 10));
        clickByLinkText("Digital continuation snapshot");
        waitForTabsToLoad(2, 60);
        ArrayList<String> tabs = new ArrayList<String> (getWindowHandles());
        switchToWindow(tabs.get(1));
    }

    public void replaceContinuationAndReviewDates(LinkedHashMap<String, Integer> continuationDates, LinkedHashMap<String, Integer> reviewDates) throws IllegalBrowserException, MalformedURLException {
        waitForTextToBePresent("Continuation date");
        replaceDateById("details[continuationDate]", continuationDates);
        replaceDateById("details[reviewDate]", reviewDates);
        click("form-actions[submit]", SelectorType.ID);
        waitForElementToBeClickable("form-actions[submit]", SelectorType.ID);
    }

    public void completeContinuationPayOrSubmit() throws IllegalBrowserException, MalformedURLException {
        if (world.createLicence.getOperatorType().equals("goods") || world.createLicence.getLicenceType().equals("special_restricted")) {
            click("submitAndPay", SelectorType.ID);
            click("form-actions[pay]", SelectorType.ID);
            Config config = new Configuration(env.toString()).getConfig();
            world.UIJourneySteps.customerPaymentModule(config.getString("cardNumber"), config.getString("cardExpiryMonth"), config.getString("cardExpiryYear"));
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
        if (!world.createLicence.getLicenceType().equals("special_restricted")) {
            if (world.createLicence.getOperatorType().equals("public") &&
                    (world.createLicence.getLicenceType().equals("restricted") || !world.createLicence.getPsvVehicleSize().equals("psvvs_medium_large"))) {
                waitForTextToBePresent("You must review and comply with any conditions and undertakings.");
                clickAllCheckboxes();
                click("submit", SelectorType.ID);
            }
        }
    }

    public void checkPSVRestrictedConditionsAndUndertakingsText() {
        Assert.assertTrue(isTextPresent("The Public Passenger Vehicles Act of 1981 states that the operation of public service vehicles cannot be your main occupation. Your PSV Restricted licence was originally granted on the basis that you meet the relevant main occupation criteria.  This must remain the case for you to retain your licence.\n" +
                "\n" +
                "A traffic commissioner can request evidence to prove that you still meet the criteria at any time. You are required to keep documents so you can prove that if asked.  The type of evidence can include, accounts, tax returns, P60, bank statements and financial records that separate PSV operation income and expenditure from income from your main occupation. You should also be able to demonstrate the time that you devote to both roles. \n" +
                "\n" +
                "If you fail to continue to meet the criteria, you must either apply for a standard licence, or surrender your restricted licence. The failure to notify of a change in your circumstances regarding the main occupation could impact on an operator's good repute", 10));
        Assert.assertTrue(isTextPresent("The operator shall, during the life of the restricted licence, keep separate records of all time spent and the gross and net income earned monthly by them from all occupations ( with supporting detailed records from which these sums are derived)  to enable the main occupation to be determined by the Traffic Commissioner at any time.  Records shall be supported by primary evidence such as payslips, P60 documents, booking diaries, invoices and tachograph records.  Copies of the records shall be made available to the DVSA or OTC officers on request. \n" +
                "\n" +
                "Should the operator no longer meet the requirements to hold a restricted licence then they will either surrender it or apply for standard licence.", 10));
    }

    public void checkContinuationReviewSections() throws IllegalBrowserException {
        Assert.assertTrue(isTextPresent("Type of licence", 10));
        Assert.assertTrue(isTextPresent("Business type", 10));
        Assert.assertTrue(isTextPresent("Business details", 10));
        Assert.assertTrue(isTextPresent("Addresses", 10));
        Assert.assertTrue(isTextPresent("Directors", 10));
        if (!world.createLicence.getLicenceType().equals("special_restricted")){
            Assert.assertTrue(isTextPresent("Operating centres and authorisation", 10));
            Assert.assertTrue(isTextPresent("Safety and compliance", 10));
            if (!world.createLicence.getLicenceType().equals("restricted")) {
                Assert.assertTrue(isTextPresent("Transport managers", 10));
            }
        }
        if (world.createLicence.getOperatorType().equals("goods")) {
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
