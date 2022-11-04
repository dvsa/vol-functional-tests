package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import apiCalls.enums.LicenceType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.InvalidArgumentException;

import java.util.HashMap;
import java.util.List;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.*;

public class SurrenderLogic extends BasePage {
    private final World world;
    private final FakerUtils faker = new FakerUtils();
    private final HashMap<String, String> address = faker.generateAddress();

    public SurrenderLogic(World word) {
        this.world = word;
    }

    @Given("i update my address details on my licence")
    public void iUpdateMyAddressDetailsOnMyLicence() {
        world.UIJourney.clickSubmit();
        clickByLinkText("Home");
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        clickByLinkText("Addresses");
        world.UIJourney.addNewAddressDetails(address, world.createApplication.getPostCodeByTrafficArea(), "correspondence_address");
        String contactNumber = "07123465976";
        replaceText("phone_primary", SelectorType.ID, contactNumber);
        UIJourney.clickSaveAndReturn();
    }

    @Then("continue with application link is displayed")
    public void continueWithApplicationLinkIsDisplayed() {
        assertFalse(isLinkPresent("Apply to surrender licence", 5));
        assertTrue(isLinkPresent("Continue with application to surrender licence", 5));
    }

    @And("user is taken to information change page on clicking continue application")
    public void userIsTakenToInformationChangePageOnClickingContinueApplication() {
        clickByLinkText("Continue with");
        assertTrue(Browser.navigate().getCurrentUrl().contains("information-changed"));
        String expectedChangedText = "Warning\n" +
                "Since starting your application to surrender your licence, you have made changes to your licence information.";
        String actualChangeText = getText("//*[@class='govuk-warning-text__text']", SelectorType.XPATH);
        assertEquals(expectedChangedText, actualChangeText);
    }

    @Given("i remove a disc to my licence")
    public void iRemoveADiscToMyLicence() {
        world.surrenderJourney.removeDisc();
    }

    @And("the new correspondence details are displayed on correspondence page")
    public void theNewCorrespondenceDetailsAreDisplayedOnCorrespondencePage() {
        click("//*[contains(text(),'Review')]", SelectorType.XPATH);
        assertEquals(world.surrenderJourney.getSurrenderAddressLine1(), String.format("%s\n%s\n%s\n%s",
                address.get("addressLine1"), address.get("addressLine2"), address.get("addressLine3"), address.get("addressLine4")));
    }

    @Given("i add a disc to my licence")
    public void iAddADiscToMyLicence() {
        world.surrenderJourney.addDiscInformation();
    }

    @Given("i am on the surrenders review contact details page")
    public void iAmOnTheSurrendersReviewContactPage() {
        assertTrue(Browser.navigate().getCurrentUrl().contains("review-contact-details"));
    }

    @And("i leave the surrenders journey")
    public void iLeaveTheSurrendersJourney() {
        clickByLinkText("Home");
        clickByLinkText(world.applicationDetails.getLicenceNumber());
    }

    @And("user is taken to review contact page on clicking continue application")
    public void userIsTakenToContactPage() {
        clickByLinkText("Continue");
        assertTrue(Browser.navigate().getCurrentUrl().contains("review-contact-details"));
        assertEquals(world.surrenderJourney.getSurrenderAddressLine1(), String.format("%s\n%s\n%s\n%s", world.createApplication.getCorrespondenceAddressLine1(), world.createApplication.getCorrespondenceAddressLine2(), world.createApplication.getCorrespondenceAddressLine3(), world.createApplication.getCorrespondenceAddressLine4()));
    }

    @Given("i am on the surrenders current discs page")
    public void iAmOnTheSurrendersCurrentDiscsPage() {
        world.UIJourney.clickSubmit();
        assertTrue(Browser.navigate().getCurrentUrl().contains("current-discs"));
    }

    @And("user is taken to the surrenders current discs on clicking continue application")
    public void userIsTakenToCurrentDiscs() {
        clickByLinkText("Continue");
        assertTrue(Browser.navigate().getCurrentUrl().contains("current-discs"));
    }

    @And("i am on the operator licence page")
    public void iAmOnTheOperatorLicencePage() {
        world.UIJourney.clickSubmit();
        world.surrenderJourney.addDiscInformation();
        waitForTextToBePresent("In your possession");
        assertTrue(Browser.navigate().getCurrentUrl().contains("operator-licence"));
    }

    @And("user is taken to the operator licence page on clicking continue application")
    public void userIsTakenToContinueApplication() {
        clickByLinkText("Continue");
        assertTrue(Browser.navigate().getCurrentUrl().contains("operator-licence"));
    }

    @And("i am on the community licence page")
    public void iAmOnTheCommunityLicencePage() {
        if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
            world.UIJourney.clickSubmit();
            world.surrenderJourney.addDiscInformation();
            waitForTextToBePresent("In your possession");
            world.surrenderJourney.addOperatorLicenceDetails();
            assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
        } else {
            throw new InvalidArgumentException("Only a goods standard international licence has community pages");
        }
    }

    @And("user is taken to the community licence page on clicking continue application")
    public void userIsTakenToCommunityLicencePageOnClickingContinueApplication() {
        clickByLinkText("Continue");
        assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
    }

    @And("i am on the disc and doc review page")
    public void iAmOnTheDiscAndDocReviewPage() {
        world.UIJourney.clickSubmit();
        world.surrenderJourney.addDiscInformation();
        waitForTextToBePresent("In your possession");
        world.surrenderJourney.addOperatorLicenceDetails();
        if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
            assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
            world.surrenderJourney.addCommunityLicenceDetails();
        }
        assertTrue(Browser.navigate().getCurrentUrl().contains("review"));
    }

    @And("user is taken to the disc and doc review page on clicking continue application")
    public void userIsTakenToTheDiscReviewPage() {
        clickByLinkText("Continue");
        assertTrue(Browser.navigate().getCurrentUrl().contains("review"));
    }

    @And("i am on the destroy disc page")
    public void iAmOnTheDestroyDiscPage() {
        world.UIJourney.clickSubmit();
        world.surrenderJourney.addDiscInformation();
        waitForTextToBePresent("In your possession");
        world.surrenderJourney.addOperatorLicenceDetails();
        if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
            assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
            world.surrenderJourney.addCommunityLicenceDetails();
        }
        world.UIJourney.clickSubmit();
        assertTrue(Browser.navigate().getCurrentUrl().contains("destroy"));
    }

    @And("i am on the declaration page")
    public void iAmOnTheDeclarationPage() {
        world.UIJourney.clickSubmit();
        world.surrenderJourney.addDiscInformation();
        waitForTextToBePresent("In your possession");
        world.surrenderJourney.addOperatorLicenceDetails();
        if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
            assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
            world.surrenderJourney.addCommunityLicenceDetails();
        }
        world.UIJourney.clickSubmit();
        waitForTitleToBePresent("Now securely destroy your licence documentation");
        world.UIJourney.clickSubmit();
        assertTrue(Browser.navigate().getCurrentUrl().contains("declaration"));
    }

    @And("my application to surrender is under consideration")
    public void myApplicationToSurrenderIsUnderConsideration() {
        world.updateLicence.printLicenceDiscs();
        world.surrenderJourney.submitSurrender();
    }

    @When("the caseworker approves the surrender")
    public void theCaseWorkerApprovesTheSurrender() {
        world.surrenderJourney.caseworkManageSurrender();
        // Refresh page
        refreshPageWithJavascript();
        waitAndClick("actions[surrender]", SelectorType.ID);
    }

    @Then("the licence status should be {string}")
    public void theLicenceStatusShouldBe(String status) {
        world.UIJourney.checkLicenceStatus(status);
    }

    @And("the surrender menu should be hidden in internal")
    public void theSurrenderMenuShouldBeHiddenInInternal() {
        assertFalse(isElementPresent("//*[contains(@id,'menu-licence_surrender", SelectorType.XPATH));
    }

    @And("the {string} page should display")
    public void thePageShouldDisplay(String page) {
        assertTrue(isTextPresent(page));
    }

    @When("the caseworker attempts to withdraw the surrender")
    public void theCaseworkerAttemptsToWithdrawTheSurrender() {
        world.surrenderJourney.caseworkManageSurrender();
        waitForElementToBeClickable("actions[surrender]", SelectorType.ID);
        refreshPageWithJavascript();
        waitAndClick("//*[contains(text(),'Withdraw')]", SelectorType.XPATH);
    }

    @Then("a modal box is displayed")
    public void aModalBoxIsDisplayed() {
        assertTrue(isElementPresent("//*[contains(text(),'Continue')]", SelectorType.XPATH));
    }

    @Then("the modal box is hidden")
    public void theModalBoxIsHidden() {
        assertFalse(isElementPresent("//*[@class='modal']", SelectorType.XPATH));
    }

    @And("the caseworker cancels the withdraw")
    public void theCaseworkerCancelsTheWithdraw() {
        waitAndClick("cancel", SelectorType.ID);
    }

    @And("the surrender menu should be displayed")
    public void theSurrenderMenuShouldBeDisplayed() {
        assertTrue(isElementPresent("//*[contains(text(),'Surrender')]", SelectorType.XPATH));
    }

    @Then("the user should remain on the surrender details page")
    public void theUserShouldRemainOnTheSurrenderDetailsPage() {
        assertTrue(Browser.navigate().getCurrentUrl().contains("surrender-details"));
        assertTrue(isLinkPresent("Surrender", 30));
    }

    @And("the licence should not displayed in selfserve")
    public void theLicenceShouldNotDisplayedInSelfserve() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        assertFalse(isLinkPresent(world.applicationDetails.getLicenceNumber(), 30));
    }

    @And("the caseworker confirms the withdraw")
    public void theCaseworkerConfirmsTheWithdraw() {
        waitAndClick("continue", SelectorType.ID);
    }

    @And("the change history shows the surrender and its withdrawal")
    public void theChangeHistoryShowsTheSurrenderAndItsWithdrawal() {
        world.UIJourney.navigateToChangeHistory();
        checkForPartialMatch("Surrender Application Withdrawn");
        checkForPartialMatch("Surrender Under Consideration");
    }

    @And("the user should be able to re apply for a surrender in internal")
    public void theUserShouldBeAbleToReApplyForASurrenderInInternal() {
        world.surrenderJourney.submitSurrender();
    }

    @And("the case worker undoes the surrender")
    public void theCaseWorkerUndoesTheSurrender() {
        waitAndClick("//*[contains(@id,'menu-licence-decisions-undo-surrender')]", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to undo the surrender of this licence?");
        world.UIJourney.clickSubmit();
        waitForTextToBePresent("The licence surrender has been undone");
    }

    @Then("the quick actions and decision buttons are not displayed for the menu items listed")
    public void theQuickActionsAndDecisionButtonsAreNotDisplayedForTheMenuItemsListed(DataTable buttons) {
        assertFalse(isTextPresent("Quick actions"));
        List<String> section_button = buttons.asList(String.class);
        for (String button : section_button) {
            clickByLinkText(button);
            assertTrue(isElementNotPresent("//*[contains(@id,'menu-licence-quick-actions')]", SelectorType.XPATH));
            assertTrue(isElementNotPresent("//*[contains(@id,'menu-licence-decisions')]", SelectorType.XPATH));
        }
    }

    @And("the change history has the surrender under consideration")
    public void theChangeHistoryHasTheSurrenderUnderConsideration() {
        world.UIJourney.navigateToChangeHistory();
        checkForPartialMatch("Surrender Application Withdrawn");
        checkForPartialMatch("Surrender Under Consideration");
    }

    @And("i have started a surrender")
    public void iHaveStartedASurrender() {
        world.surrenderJourney.navigateToSurrendersStartPage();
        world.surrenderJourney.startSurrender();
    }
}