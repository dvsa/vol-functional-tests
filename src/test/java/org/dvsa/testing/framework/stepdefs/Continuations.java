package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.config.Configuration;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.system.Properties;
import com.typesafe.config.Config;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Continuations extends BasePage implements En {

    private Dates dates = new Dates(new LocalDateCalendar());
    private LinkedHashMap<String, Integer> continuationDates;

    public Continuations(World world) {

        EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
        When("^i change my continuation date$", () -> {
            world.UIJourneySteps.urlSearchAndViewLicence();
            continuationDates = dates.getDate(10, 0, 0);
            world.UIJourneySteps.replaceContinuationAndReviewDates(continuationDates);

            // PUT A CHECK IN TO MAKE SURE IT DOES THIS!
        });
        And("^i generate a continuation$", () -> {
            world.UIJourneySteps.continueALicenceOnInternal(world.createLicence.getLicenceNumber(), world.updateLicence.getLicenceTrafficArea(), continuationDates.get("month"));
        });
        And("^fill in my continuation details on self serve$", () -> {
            world.UIJourneySteps.continueLicenceWithVerifyAndPay();
        });
        Then("^the continuation should be approved and a snapshot generated on Internal$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.urlSearchAndViewLicence();
            clickByLinkText("Docs & attachments");
            refreshPageUntilElementAppears("//*[contains(text(), 'Digital continuation snapshot')]", SelectorType.XPATH);
            Assert.assertTrue(isTextPresent("Digital continuation snapshot", 10));
        });
        And("^the users of ss should display on the continuation review details page and on the snapshot$", () -> {
            world.UIJourneySteps.navigateToNavBarPage("manage users");
            List<WebElement> userNamesElements = findElements("//tbody//td[@data-heading='Name']", SelectorType.XPATH);
            List<WebElement> userEmailElements = findElements("//tbody//td[@data-heading='Email address']", SelectorType.XPATH);
            List<WebElement> userPermissionElements = findElements("//tbody//td[@data-heading='Permission']", SelectorType.XPATH);
            String[] userNames = new String[userNamesElements.size()];
            String[] userEmails = new String[userEmailElements.size()];
            String[] userPermissions = new String[userPermissionElements.size()];
            for (int i = 0; i < userNamesElements.size(); i++){
                userNames[i] = userNamesElements.get(i).getText();
                userEmails[i] = userEmailElements.get(i).getText();
                userPermissions[i] = userPermissionElements.get(i).getText();
            }
            world.UIJourneySteps.clickContinueLicenceOnSelfServe();
            click("submit", SelectorType.ID);
            clickAllCheckboxes();
            Assert.assertTrue(isTextPresent("User access",10));
            userNamesElements = findElements("//tbody//td[@data-heading='Name']", SelectorType.XPATH);
            userEmailElements = findElements("//tbody//td[@data-heading='Email address']", SelectorType.XPATH);
            userPermissionElements = findElements("//tbody//td[@data-heading='Permission']", SelectorType.XPATH);
            for (int i = 0; i < userNamesElements.size(); i++){
                Assert.assertEquals(userNamesElements.get(i).getText(), userNames[i]);
                Assert.assertEquals(userEmailElements.get(i).getText(), userEmails[i]);
                Assert.assertEquals(userPermissionElements.get(i).getText(), userPermissions[i]);
            }
            findSelectAllRadioButtonsByValue("Y");
            click("licenceChecklistConfirmation[yesContent][submit]", SelectorType.ID);
            if (!world.createLicence.getLicenceType().equals("special_restricted")) {
                if (world.createLicence.getOperatorType().equals("public") && world.createLicence.getLicenceType().equals("restricted")
                        || (world.createLicence.getOperatorType().equals("public") && !world.createLicence.getPsvVehicleSize().equals("psvvs_medium_large"))) {
                    clickAllCheckboxes();
                    click("submit", SelectorType.ID);
                }
            }
            if (!(world.createLicence.getOperatorType().equals("public") && world.createLicence.getLicenceType().equals("special_restricted"))) {
                world.UIJourneySteps.completeContinuationFinancesPage();
            }
            click("content[signatureOptions]", SelectorType.ID);
            click("sign", SelectorType.ID);
            world.UIJourneySteps.signWithVerify();
            waitForTextToBePresent("Declaration signed through GOV.UK Verify");
            if(world.createLicence.getOperatorType().equals("goods") || world.createLicence.getLicenceType().equals("special_restricted")) {
                click("submitAndPay", SelectorType.ID);
                click("form-actions[pay]", SelectorType.ID);
                Config config = new Configuration(env.toString()).getConfig();
                world.UIJourneySteps.customerPaymentModule(config.getString("cardNumber"), config.getString("cardExpiryMonth"), config.getString("cardExpiryYear"));
            } else {
                click("submit", SelectorType.ID);
            }
            waitForTextToBePresent("Your licence has been continued");
            world.UIJourneySteps.viewContinuationSnapshotOnInternal();
            for (int i = 0; i < userNamesElements.size(); i++){
                Assert.assertEquals(userNamesElements.get(i).getText(), userNames[i]);
                Assert.assertEquals(userEmailElements.get(i).getText(), userEmails[i]);
                Assert.assertEquals(userPermissionElements.get(i).getText(), userPermissions[i]);
            }
            closeTab();
        });
        Then("^the continuation conditions and undertaking page and snapshot should display the right text$", () -> {
            world.UIJourneySteps.navigateToNavBarPage("manage users");
            world.UIJourneySteps.clickContinueLicenceOnSelfServe();
            click("submit", SelectorType.ID);
            clickAllCheckboxes();
            findSelectAllRadioButtonsByValue("Y");
            click("licenceChecklistConfirmation[yesContent][submit]", SelectorType.ID);
            if (!world.createLicence.getLicenceType().equals("special_restricted")) {
                if (world.createLicence.getOperatorType().equals("public") && world.createLicence.getLicenceType().equals("restricted")
                        || (world.createLicence.getOperatorType().equals("public") && !world.createLicence.getPsvVehicleSize().equals("psvvs_medium_large"))) {
                    if (world.createLicence.getOperatorType().equals("public") && world.createLicence.getLicenceType().equals("restricted")) {
                        waitForTextToBePresent("Conditions and undertakings");
                        Assert.assertTrue(isTextPresent("The Public Passenger Vehicles Act of 1981 states that the operation of public service vehicles cannot be your main occupation. Your PSV Restricted licence was originally granted on the basis that you meet the relevant main occupation criteria.  This must remain the case for you to retain your licence.\n" +
                                "\n" +
                                "A traffic commissioner can request evidence to prove that you still meet the criteria at any time. You are required to keep documents so you can prove that if asked.  The type of evidence can include, accounts, tax returns, P60, bank statements and financial records that separate PSV operation income and expenditure from income from your main occupation. You should also be able to demonstrate the time that you devote to both roles. \n" +
                                "\n" +
                                "If you fail to continue to meet the criteria, you must either apply for a standard licence, or surrender your restricted licence. The failure to notify of a change in your circumstances regarding the main occupation could impact on an operator's good repute", 10));
                        Assert.assertTrue(isTextPresent("The operator shall, during the life of the restricted licence, keep separate records of all time spent and the gross and net income earned monthly by them from all occupations ( with supporting detailed records from which these sums are derived)  to enable the main occupation to be determined by the Traffic Commissioner at any time.  Records shall be supported by primary evidence such as payslips, P60 documents, booking diaries, invoices and tachograph records.  Copies of the records shall be made available to the DVSA or OTC officers on request. \n" +
                                "\n" +
                                "Should the operator no longer meet the requirements to hold a restricted licence then they will either surrender it or apply for standard licence.", 10));
                    }
                    clickAllCheckboxes();
                    click("submit", SelectorType.ID);
                }
            }
            if (!(world.createLicence.getOperatorType().equals("public") && world.createLicence.getLicenceType().equals("special_restricted"))) {
                world.UIJourneySteps.completeContinuationFinancesPage();
            }
            click("content[signatureOptions]", SelectorType.ID);
            click("sign", SelectorType.ID);
            world.UIJourneySteps.signWithVerify();
            waitForTextToBePresent("Declaration signed through GOV.UK Verify");
            if(world.createLicence.getOperatorType().equals("goods") || world.createLicence.getLicenceType().equals("special_restricted")) {
                click("submitAndPay", SelectorType.ID);
                click("form-actions[pay]", SelectorType.ID);
                Config config = new Configuration(env.toString()).getConfig();
                world.UIJourneySteps.customerPaymentModule(config.getString("cardNumber"), config.getString("cardExpiryMonth"), config.getString("cardExpiryYear"));
            } else {
                click("submit", SelectorType.ID);
            }
            waitForTextToBePresent("Your licence has been continued");
            world.UIJourneySteps.viewContinuationSnapshotOnInternal();
            if (world.createLicence.getOperatorType().equals("public") && world.createLicence.getLicenceType().equals("restricted")) {
                waitForTextToBePresent("Conditions and undertakings");
                Assert.assertTrue(isTextPresent("The Public Passenger Vehicles Act of 1981 states that the operation of public service vehicles cannot be your main occupation. Your PSV Restricted licence was originally granted on the basis that you meet the relevant main occupation criteria.  This must remain the case for you to retain your licence.\n" +
                        "\n" +
                        "A traffic commissioner can request evidence to prove that you still meet the criteria at any time. You are required to keep documents so you can prove that if asked.  The type of evidence can include, accounts, tax returns, P60, bank statements and financial records that separate PSV operation income and expenditure from income from your main occupation. You should also be able to demonstrate the time that you devote to both roles. \n" +
                        "\n" +
                        "If you fail to continue to meet the criteria, you must either apply for a standard licence, or surrender your restricted licence. The failure to notify of a change in your circumstances regarding the main occupation could impact on an operator's good repute", 10));
                Assert.assertTrue(isTextPresent("The operator shall, during the life of the restricted licence, keep separate records of all time spent and the gross and net income earned monthly by them from all occupations ( with supporting detailed records from which these sums are derived)  to enable the main occupation to be determined by the Traffic Commissioner at any time.  Records shall be supported by primary evidence such as payslips, P60 documents, booking diaries, invoices and tachograph records.  Copies of the records shall be made available to the DVSA or OTC officers on request. \n" +
                        "\n" +
                        "Should the operator no longer meet the requirements to hold a restricted licence then they will either surrender it or apply for standard licence.", 10));
            }
            closeTab();
            ArrayList<String> tabs = new ArrayList<String> (getWindowHandles());
            switchToWindow(tabs.get(0));
        });
        Then("^the correct checks should display on the continuation review details page and continuation snapshot$", () -> {
            world.UIJourneySteps.clickContinueLicenceOnSelfServe();
            click("submit", SelectorType.ID);
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
                if (!world.createLicence.getOperatorType().equals("public")) {
                    // Selenium is struggling to target Vehicle title.
                    Assert.assertTrue(isTextPresent("Vehicle registration mark", 10));
                }
            }
            Assert.assertTrue(isTextPresent("User access",10));
            clickAllCheckboxes();
            findSelectAllRadioButtonsByValue("Y");
            click("licenceChecklistConfirmation[yesContent][submit]", SelectorType.ID);
            if (!world.createLicence.getLicenceType().equals("special_restricted")) {
                if (world.createLicence.getOperatorType().equals("public") && world.createLicence.getLicenceType().equals("restricted")
                        || (world.createLicence.getOperatorType().equals("public") && !world.createLicence.getPsvVehicleSize().equals("psvvs_medium_large"))) {
                    clickAllCheckboxes();
                    click("submit", SelectorType.ID);
                }
            }
            if (!(world.createLicence.getOperatorType().equals("public") && world.createLicence.getLicenceType().equals("special_restricted"))) {
                world.UIJourneySteps.completeContinuationFinancesPage();
            }
            click("content[signatureOptions]", SelectorType.ID);
            click("sign", SelectorType.ID);
            world.UIJourneySteps.signWithVerify();
            waitForTextToBePresent("Declaration signed through GOV.UK Verify");
            if(world.createLicence.getOperatorType().equals("goods") || world.createLicence.getLicenceType().equals("special_restricted")) {
                click("submitAndPay", SelectorType.ID);
                click("form-actions[pay]", SelectorType.ID);
                Config config = new Configuration(env.toString()).getConfig();
                world.UIJourneySteps.customerPaymentModule(config.getString("cardNumber"), config.getString("cardExpiryMonth"), config.getString("cardExpiryYear"));
            } else {
                click("submit", SelectorType.ID);
            }
            waitForTextToBePresent("Your licence has been continued");
            world.UIJourneySteps.viewContinuationSnapshotOnInternal();
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
                if (!world.createLicence.getOperatorType().equals("public")) {
                    // Selenium is struggling to target Vehicle title.
                    Assert.assertTrue(isTextPresent("Vehicle registration mark", 10));
                }
            }
            Assert.assertTrue(isTextPresent("User access",10));
        });
    }
}
fix issues with closing tab.