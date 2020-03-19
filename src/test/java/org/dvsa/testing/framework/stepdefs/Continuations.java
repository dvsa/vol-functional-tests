package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

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
            waitForTextToBePresent("Continuation date");
            replaceDateById("details[continuationDate]", continuationDates);
            replaceDateById("details[reviewDate]", continuationDates);
            click("form-actions[submit]", SelectorType.ID);
            waitForElementToBeClickable("form-actions[submit]", SelectorType.ID);
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
        And("^the users of ss should display on the continuation review details page$", () -> {
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
            world.UIJourneySteps.navigateToSelfServePage("licence","view");
            refreshPageUntilElementAppears("//*[contains(@class,'info-box--pink')]", SelectorType.XPATH);
            click("//a[contains(text(),'Continue licence')]", SelectorType.XPATH);
            click("submit", SelectorType.ID);
            clickAllCheckboxes();
            userNamesElements = findElements("//tbody//td[@data-heading='Name']", SelectorType.XPATH);
            userEmailElements = findElements("//tbody//td[@data-heading='Email address']", SelectorType.XPATH);
            userPermissionElements = findElements("//tbody//td[@data-heading='Permission']", SelectorType.XPATH);
            for (int i = 0; i < userNamesElements.size(); i++){
                Assert.assertEquals(userNamesElements.get(i).getText(), userNames[i]);
                Assert.assertEquals(userEmailElements.get(i).getText(), userEmails[i]);
                Assert.assertEquals(userPermissionElements.get(i).getText(), userPermissions[i]);
            }
            Assert.assertTrue(isTextPresent("Type of licence", 10));
            Assert.assertTrue(isTextPresent("Business type", 10));
            Assert.assertTrue(isTextPresent("Business details", 10));
            Assert.assertTrue(isTextPresent("Business details", 10));
            Assert.assertTrue(isTextPresent("Addresses", 10));
            Assert.assertTrue(isTextPresent("Directors", 10));
            Assert.assertTrue(isTextPresent("Operating centres and authorisation", 10));
            Assert.assertTrue(isTextPresent("Transport managers", 10));
            // Selenium is struggling to target Vehicle title.
            Assert.assertTrue(isTextPresent("Vehicle registration mark", 10));
            Assert.assertTrue(isTextPresent("Safety and compliance", 10));
            Assert.assertTrue(isTextPresent("User access",10));
        });
        Then("^the continuation for a \"([^\"]*)\" \"([^\"]*)\" conditions and undertaking page should display the right text$", (String opType, String licType) -> {
            world.UIJourneySteps.navigateToNavBarPage("manage users");
            world.UIJourneySteps.navigateToSelfServePage("licence","view");
            refreshPageUntilElementAppears("//*[contains(@class,'info-box--pink')]", SelectorType.XPATH);
            click("//a[contains(text(),'Continue licence')]", SelectorType.XPATH);
            click("submit", SelectorType.ID);
            clickAllCheckboxes();
            findSelectAllRadioButtonsByValue("Y");
            click("licenceChecklistConfirmation[yesContent][submit]", SelectorType.ID);
            if (opType == "public" && licType == "restricted") {
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
            // Need to add check for licences who don't have this page or do have the page but the text isn't the same.
        });
    }
}
