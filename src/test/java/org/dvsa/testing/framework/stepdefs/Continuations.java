package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Continuations extends BasePage implements En {

    private Dates dates = new Dates(new LocalDateCalendar());
    private LinkedHashMap<String, Integer> continuationDates;

    public Continuations(World world) {

        EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
        When("^i change my continuation and review date$", () -> {
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
            world.UIJourneySteps.completeContinuationConditionsAndUndertakingsPage();
            world.UIJourneySteps.completeContinuationFinancesPage();
            world.UIJourneySteps.completeContinuationsSignPage();
            world.UIJourneySteps.completeContinuationPayOrSubmit();
            world.UIJourneySteps.viewContinuationSnapshotOnInternal();
            for (int i = 0; i < userNamesElements.size(); i++){
                Assert.assertEquals(userNamesElements.get(i).getText(), userNames[i]);
                Assert.assertEquals(userEmailElements.get(i).getText(), userEmails[i]);
                Assert.assertEquals(userPermissionElements.get(i).getText(), userPermissions[i]);
            }
            closeTabAndFocusFirstTab();
        });
        Then("^the continuation conditions and undertaking page and snapshot should display the right text$", () -> {
            world.UIJourneySteps.navigateToNavBarPage("manage users");
            world.UIJourneySteps.clickContinueLicenceOnSelfServe();
            click("submit", SelectorType.ID);
            world.UIJourneySteps.completeContinuationsReviewPage();
            if (!world.createLicence.getLicenceType().equals("special_restricted")) {
                if (world.createLicence.getOperatorType().equals("public") &&
                        (world.createLicence.getLicenceType().equals("restricted") || !world.createLicence.getPsvVehicleSize().equals("psvvs_medium_large"))) {
                    waitForTextToBePresent("You must review and comply with any conditions and undertakings.");
                    if (world.createLicence.getLicenceType().equals("restricted")) {
                        world.UIJourneySteps.checkPSVRestrictedConditionsAndUndertakingsText();
                    }
                    clickAllCheckboxes();
                    click("submit", SelectorType.ID);
                }
            }
            world.UIJourneySteps.completeContinuationFinancesPage();
            world.UIJourneySteps.completeContinuationsSignPage();
            world.UIJourneySteps.completeContinuationPayOrSubmit();
            world.UIJourneySteps.viewContinuationSnapshotOnInternal();
            if (world.createLicence.getOperatorType().equals("public") && world.createLicence.getLicenceType().equals("restricted")) {
                waitForTextToBePresent("Conditions and undertakings");
                world.UIJourneySteps.checkPSVRestrictedConditionsAndUndertakingsText();
            }
            closeTabAndFocusFirstTab();
        });
        Then("^the correct checks should display on the continuation review details page and continuation snapshot$", () -> {
            world.UIJourneySteps.clickContinueLicenceOnSelfServe();
            click("submit", SelectorType.ID);
            world.UIJourneySteps.checkContinuationReviewSections();
            world.UIJourneySteps.completeContinuationsReviewPage();
            world.UIJourneySteps.completeContinuationConditionsAndUndertakingsPage();
            world.UIJourneySteps.completeContinuationFinancesPage();
            world.UIJourneySteps.completeContinuationsSignPage();
            world.UIJourneySteps.completeContinuationPayOrSubmit();
            world.UIJourneySteps.viewContinuationSnapshotOnInternal();
            world.UIJourneySteps.checkContinuationReviewSections();
            closeTabAndFocusFirstTab();
        });
    }

    public void closeTabAndFocusFirstTab() throws IllegalBrowserException, MalformedURLException {
        closeTab();
        ArrayList<String> tabs = new ArrayList<String>(getWindowHandles());
        switchToWindow(tabs.get(0));
    }
}