package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import io.cucumber.datatable.DataTable;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SurrenderLogic extends BasePage implements En {
    private final World world;

    public SurrenderLogic (World word) {this.world = word;}

    @And("my application to surrender is under consideration")
    public void myApplicationToSurrenderIsUnderConsideration() {
        world.updateLicence.printLicenceDiscs();
        world.surrenderJourney.submitSurrender();
    }

    @When("the caseworker approves the surrender")
    public void theCaseworkerApprovesTheSurrender() {
        world.surrenderJourney.caseworkManageSurrender();
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

    @And("the licence should not displayed in selfserve")
    public void theLicenceShouldNotDisplayedInSelfserve() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        assertFalse(isLinkPresent(world.applicationDetails.getLicenceNumber(), 30));
    }

    @When("the caseworker attempts to withdraw the surrender")
    public void theCaseworkerAttemptsToWithdrawTheSurrender() {
        waitAndClick("//*[contains(@id,'menu-licence-decisions-undo-surrender')]", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to undo the surrender of this licence?");
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("The licence surrender has been undone");
    }

    @Then("a modal box is displayed")
    public void aModalBoxIsDisplayed() {
        assertTrue(isElementPresent("//*[contains(text(),'Continue')]", SelectorType.XPATH));
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

    @And("the caseworker cancels the withdraw")
    public void theCaseworkerCancelsTheWithdraw() {
        waitAndClick("cancel", SelectorType.ID);
    }

    @Then("the modal box is hidden")
    public void theModalBoxIsHidden() {
        assertFalse(isElementPresent("//*[@class='modal']", SelectorType.XPATH));
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

    @And("the case worker undoes the surrender")
    public void theCaseWorkerUndoesTheSurrender() {
        waitAndClick("//*[contains(@id,'menu-licence-decisions-undo-surrender')]", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to undo the surrender of this licence?");
        waitAndClick("form-actions[submit]", SelectorType.ID);
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

    @When("i am on the surrenders review contact details page")
    public void iAmOnTheSurrendersReviewContactDetailsPage() {
        assertTrue(Browser.navigate().getCurrentUrl().contains("review-contact-details"));
    }
}

