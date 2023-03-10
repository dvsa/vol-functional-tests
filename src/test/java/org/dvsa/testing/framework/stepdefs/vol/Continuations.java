package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import apiCalls.enums.LicenceType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.dvsa.testing.framework.enums.SelfServeNavBar;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebElement;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Continuations extends BasePage {
    private final World world;

    private final Dates dates = new Dates(new LocalDateCalendar());
    private LinkedHashMap<String, String> continuationDate;

    public Continuations(World world) {
        this.world = world;
    }

    @When("i change my continuation and review date on Internal")
    public void iChangeMyContinuationAndReviewDateOnInternal() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        continuationDate = dates.getDateHashMap(10, 0, 0);
        world.continuationJourney.replaceContinuationAndReviewDates(continuationDate, continuationDate);
    }

    @And("i generate a continuation")
    public void iGenerateAContinuation() throws HttpException {
        world.continuationJourney.generateContinuationOnInternal(world.applicationDetails.getLicenceNumber(), world.updateLicence.getLicenceTrafficArea(), continuationDate.get("month"));
    }

    @And("fill in my continuation details on self serve")
    public void fillInMyContinuationDetailsOnSelfServe() {
        world.continuationJourney.continueLicenceWithVerifyAndPay();
    }

    @Then("the continuation should be approved and a snapshot generated on Internal")
    public void theContinuationShouldBeApprovedAndASnapshotGeneratedOnInternal() throws HttpException {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        clickByLinkText("Docs & attachments");
        refreshPageUntilElementAppears("//*[contains(text(), 'Digital continuation snapshot')]", SelectorType.XPATH);
        assertTrue(isTextPresent("Digital continuation snapshot"));
    }

    @Then("the users of ss should display on the continuation review details page and on the snapshot")
    public void theUsersOfSsShouldDisplayOnTheContinuationReviewDetailsPageAndOnTheSnapshot() throws HttpException {
        world.selfServeNavigation.navigateToNavBarPage(SelfServeNavBar.MANAGE_USERS);
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
        world.continuationJourney.clickContinueLicenceOnSelfServe();
        click("submit", SelectorType.ID);
        clickAllCheckboxes();
        assertTrue(isTextPresent("User access"));
        userNamesElements = findElements("//tbody//td[@data-heading='Name']", SelectorType.XPATH);
        userEmailElements = findElements("//tbody//td[@data-heading='Email address']", SelectorType.XPATH);
        userPermissionElements = findElements("//tbody//td[@data-heading='Permission']", SelectorType.XPATH);
        for (int i = 0; i < userNamesElements.size(); i++){
            assertEquals(userNamesElements.get(i).getText(), userNames[i]);
            assertEquals(userEmailElements.get(i).getText(), userEmails[i]);
            assertEquals(userPermissionElements.get(i).getText(), userPermissions[i]);
        }
        findSelectAllRadioButtonsByValue("Y");
        click("licenceChecklistConfirmation[yesContent][submit]", SelectorType.ID);
        world.continuationJourney.completeContinuationConditionsAndUndertakingsPage();
        world.continuationJourney.completeContinuationFinancesPage();
        world.continuationJourney.completeContinuationsSignPage();
        untilElementIsNotPresent("//*[@class='ccms-loader centre']", SelectorType.XPATH);
        waitForTextToBePresent("Sign out");
        world.continuationJourney.completeContinuationPayOrSubmit();
        world.continuationJourney.viewContinuationSnapshotOnInternal();
        for (int i = 0; i < userNamesElements.size(); i++){
            assertEquals(userNamesElements.get(i).getText(), userNames[i]);
            assertEquals(userEmailElements.get(i).getText(), userEmails[i]);
            assertEquals(userPermissionElements.get(i).getText(), userPermissions[i]);
        }
        closeTabAndFocusTab(0);
    }
    @Then("the continuation conditions and undertaking page and snapshot should display the right text")
    public void theContinuationConditionsAndUndertakingPageAndSnapshotShouldDisplayTheRightText() throws FileNotFoundException, HttpException {
        world.selfServeNavigation.navigateToNavBarPage(SelfServeNavBar.MANAGE_USERS);
        world.continuationJourney.clickContinueLicenceOnSelfServe();
        click("submit", SelectorType.ID);
        world.continuationJourney.completeContinuationsReviewPage();
        if (!world.createApplication.getLicenceType().equals(LicenceType.SPECIAL_RESTRICTED.asString())) {
            if (world.licenceCreation.isPSVLicence() &&
                    (world.createApplication.getLicenceType().equals(LicenceType.RESTRICTED.asString())) || !world.createApplication.getPsvVehicleSize().equals("psvvs_medium_large")) {
                waitForTextToBePresent("You must review and comply with any conditions and undertakings.");
                if (world.createApplication.getLicenceType().equals(LicenceType.RESTRICTED.asString())) {
                    world.continuationJourney.checkPSVRestrictedConditionsAndUndertakingsText();
                }
                clickAllCheckboxes();
                click("submit", SelectorType.ID);
            }
        }
        world.continuationJourney.completeContinuationFinancesPage();
        world.continuationJourney.completeContinuationsSignPage();
        world.continuationJourney.completeContinuationPayOrSubmit();
        world.continuationJourney.viewContinuationSnapshotOnInternal();
        if (world.licenceCreation.isPSVLicence() && world.createApplication.getLicenceType().equals(LicenceType.RESTRICTED.asString())) {
            waitForTextToBePresent("Conditions and undertakings");
            world.continuationJourney.checkPSVRestrictedConditionsAndUndertakingsText();
        }
        closeTabAndFocusTab(0);
    }

    @Then("the correct checks should display on the continuation review details page and continuation snapshot")
    public void theCorrectChecksShouldDisplayOnTheContinuationReviewDetailsPageAndContinuationSnapshot() throws HttpException {
        world.continuationJourney.clickContinueLicenceOnSelfServe();
        click("submit", SelectorType.ID);
        world.continuationJourney.checkContinuationReviewSections();
        world.continuationJourney.completeContinuationsReviewPage();
        world.continuationJourney.completeContinuationConditionsAndUndertakingsPage();
        world.continuationJourney.completeContinuationFinancesPage();
        world.continuationJourney.completeContinuationsSignPage();
        world.continuationJourney.completeContinuationPayOrSubmit();
        world.continuationJourney.viewContinuationSnapshotOnInternal();
        world.continuationJourney.checkContinuationReviewSections();
        closeTabAndFocusTab(0);
    }
}