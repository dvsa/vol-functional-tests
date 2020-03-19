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
            long kickoutTime = System.currentTimeMillis() + 120000;
            do {
                javaScriptExecutor("location.reload(true)");
            } while (!isTextPresent("Digital continuation snapshot", 10) && System.currentTimeMillis() < kickoutTime);
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
    }
}
