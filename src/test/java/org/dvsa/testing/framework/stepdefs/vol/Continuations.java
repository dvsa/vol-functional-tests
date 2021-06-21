package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import io.cucumber.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashMap;
import java.util.List;

public class Continuations extends BasePage implements En {

    private Dates dates = new Dates(new LocalDateCalendar());
    private LinkedHashMap<String, String> continuationDate;

    public Continuations(World world) {

        When("^i change my continuation and review date on Internal$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalNavigation.urlSearchAndViewLicence();
            continuationDate = dates.getDateHashMap(10, 0, 0);
            world.continuationJourneySteps.replaceContinuationAndReviewDates(continuationDate, continuationDate);
        });
        And("^i generate a continuation$", () -> {
            world.continuationJourneySteps.generateContinuationOnInternal(world.applicationDetails.getLicenceNumber(), world.updateLicence.getLicenceTrafficArea(), continuationDate.get("month"));
        });
        And("^fill in my continuation details on self serve$", () -> {
            world.continuationJourneySteps.continueLicenceWithVerifyAndPay();
        });
        Then("^the continuation should be approved and a snapshot generated on Internal$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalNavigation.urlSearchAndViewLicence();
            clickByLinkText("Docs & attachments");
            refreshPageUntilElementAppears("//*[contains(text(), 'Digital continuation snapshot')]", SelectorType.XPATH);
            Assert.assertTrue(isTextPresent("Digital continuation snapshot", 10));
        });
        And("^the users of ss should display on the continuation review details page and on the snapshot$", () -> {
            world.selfServeNavigation.navigateToNavBarPage("manage users");
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
            world.continuationJourneySteps.clickContinueLicenceOnSelfServe();
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
            world.continuationJourneySteps.completeContinuationConditionsAndUndertakingsPage();
            world.continuationJourneySteps.completeContinuationFinancesPage();
            world.continuationJourneySteps.completeContinuationsSignPage();
            world.continuationJourneySteps.completeContinuationPayOrSubmit();
            world.continuationJourneySteps.viewContinuationSnapshotOnInternal();
            for (int i = 0; i < userNamesElements.size(); i++){
                Assert.assertEquals(userNamesElements.get(i).getText(), userNames[i]);
                Assert.assertEquals(userEmailElements.get(i).getText(), userEmails[i]);
                Assert.assertEquals(userPermissionElements.get(i).getText(), userPermissions[i]);
            }
            closeTabAndFocusTab(0);
        });
        Then("^the continuation conditions and undertaking page and snapshot should display the right text$", () -> {
            world.selfServeNavigation.navigateToNavBarPage("manage users");
            world.continuationJourneySteps.clickContinueLicenceOnSelfServe();
            click("submit", SelectorType.ID);
            world.continuationJourneySteps.completeContinuationsReviewPage();
            if (!world.createApplication.getLicenceType().equals("special_restricted")) {
                if (world.licenceCreation.isPSVLicence() &&
                        (world.createApplication.getLicenceType().equals("restricted") || !world.createApplication.getPsvVehicleSize().equals("psvvs_medium_large"))) {
                    waitForTextToBePresent("You must review and comply with any conditions and undertakings.");
                    if (world.createApplication.getLicenceType().equals("restricted")) {
                        world.continuationJourneySteps.checkPSVRestrictedConditionsAndUndertakingsText();
                    }
                    clickAllCheckboxes();
                    click("submit", SelectorType.ID);
                }
            }
            world.continuationJourneySteps.completeContinuationFinancesPage();
            world.continuationJourneySteps.completeContinuationsSignPage();
            world.continuationJourneySteps.completeContinuationPayOrSubmit();
            world.continuationJourneySteps.viewContinuationSnapshotOnInternal();
            if (world.licenceCreation.isPSVLicence() && world.createApplication.getLicenceType().equals("restricted")) {
                waitForTextToBePresent("Conditions and undertakings");
                world.continuationJourneySteps.checkPSVRestrictedConditionsAndUndertakingsText();
            }
            closeTabAndFocusTab(0);
        });
        Then("^the correct checks should display on the continuation review details page and continuation snapshot$", () -> {
            world.continuationJourneySteps.clickContinueLicenceOnSelfServe();
            click("submit", SelectorType.ID);
            world.continuationJourneySteps.checkContinuationReviewSections();
            world.continuationJourneySteps.completeContinuationsReviewPage();
            world.continuationJourneySteps.completeContinuationConditionsAndUndertakingsPage();
            world.continuationJourneySteps.completeContinuationFinancesPage();
            world.continuationJourneySteps.completeContinuationsSignPage();
            world.continuationJourneySteps.completeContinuationPayOrSubmit();
            world.continuationJourneySteps.viewContinuationSnapshotOnInternal();
            world.continuationJourneySteps.checkContinuationReviewSections();
            closeTabAndFocusTab(0);
        });
    }
}