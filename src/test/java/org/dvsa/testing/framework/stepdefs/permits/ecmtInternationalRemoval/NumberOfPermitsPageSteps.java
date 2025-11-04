package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;

import static org.junit.jupiter.api.Assertions.*;

public class NumberOfPermitsPageSteps extends BasePage {

    private final World world;

    public NumberOfPermitsPageSteps(World world) {
        this.world = world;
    }

    @When("I am on the ECMT Removal number of permits page")
    public void iAmOnTheECMTRemovalNumberOfPermitsPage() {
        world.ecmtInternationalRemovalJourney.completeUntilNumberOfPermitsPage();
    }

    @Then("the reference number and heading are displayed correct")
    public void theReferenceNumberAndHeadingAreDisplayedCorrect() {
        assertEquals("How many permits do you require for this licence?",
                NumberOfPermitsPage.getPageHeading(),
                "Page heading does not match expected value");

        String actualReference = getText("span.govuk-caption-xl", SelectorType.CSS).trim();
        assertEquals(world.basePermitJourney.getFullReferenceNumber(),
                actualReference,
                "Application reference does not match expected value");
    }

    @And("the fee information is displayed correctly")
    public void theFeeInformationIsDisplayedCorrectly() {
        assertTrue(NumberOfPermitsPage.isFeeTextPresent(),
                "Fee information (£18) is not displayed on the page");
    }

    @When("I enter zero as value in the number of permits fields")
    public void iEnterZeroAsValueInTheNumberOfPermitsFields() {
        NumberOfPermitsPage.enterNumberOfPermits(0);
    }

    @When("I enter a number exceeding the maximum authorised vehicles")
    public void iEnterANumberExceedingTheMaximumAuthorisedVehicles() {
        NumberOfPermitsPage.enterInvalidNumber();
    }

    @When("I enter valid number of permits")
    public void iEnterValidNumberOfPermits() {
        NumberOfPermitsPage.enterRandomValidNumber();
    }

    @When("I specify my number of ECMT removal permits")
    public void iSpecifyMyNumberOfECMTRemovalPermits() {
        NumberOfPermitsPage.enterRandomValidNumber();
        UniversalActions.clickSaveAndContinue();
    }

    @And("I save and continue")
    public void iSaveAndContinue() {
        UniversalActions.clickSaveAndContinue();
    }

    @And("I select save and return overview link")
    public void iSelectSaveAndReturnOverviewLink() {
        UniversalActions.clickSaveAndReturn();
    }

    @Then("the relevant error message is displayed")
    public void theRelevantErrorMessageIsDisplayed() {
        assertTrue(NumberOfPermitsPage.isEnterNumberErrorPresent(),
                "Expected error message for empty/invalid number is not displayed");
    }

    @Then("the maximum exceeded error message is displayed")
    public void theMaximumExceededErrorMessageIsDisplayed() {
        assertTrue(NumberOfPermitsPage.isMaximumExceededErrorPresent(),
                "Expected error message for exceeding maximum is not displayed");
    }

    @Then("the number of permits section on the ECMT Removals Overview page is complete")
    public void theNumberOfPermitsSectionIsComplete() {
        OverviewPage.untilOnPage();

        String sectionStatusXPath = String.format(
                "//strong[contains(text(),'%s')]/ancestor::li//strong[@class='govuk-tag app-task-list__tag']",
                OverviewSection.NumberOfPermits.toString()
        );

        String status = getText(sectionStatusXPath, SelectorType.XPATH).trim();
        assertEquals(PermitStatus.COMPLETED.toString(), status,
                "Number of permits section is not marked as COMPLETED on overview page");
    }

    @And("the page heading on the ECMT removals number of permits page is displayed correctly")
    public void thePageHeadingOnTheECMTRemovalsNumberOfPermitsPage() {
        assertEquals("How many permits do you require for this licence?",
                NumberOfPermitsPage.getPageHeading());
    }

    @And("the application reference on the ECMT removals number of permits page is displayed correctly")
    public void theApplicationReferenceOnTheECMTRemovalsNumberOfPermitsPageIsDisplayedCorrectly() {
        String actualReference = BasePage.getText("span.govuk-caption-xl", SelectorType.CSS).trim();
        assertEquals(world.basePermitJourney.getFullReferenceNumber(), actualReference);
    }

    @And("the advisory text on the ECMT removals number of permits page is displayed correctly")
    public void theAdvisoryTextOnTheECMTRemovalsNumberOfPermitsPageIsDisplayedCorrectly() {
        assertTrue(NumberOfPermitsPage.isFeeTextPresent());
    }

    @Then("I should get the number of permits page error message")
    public void iShouldGetTheNumberOfPermitsPageErrorMessage() {
        assertTrue(NumberOfPermitsPage.isEnterNumberErrorPresent());
    }

    @Then("I should get the maximum number of permits exceeded page error message")
    public void iShouldGetTheMaximumNumberOfPermitsExceededPageErrorMessage() {
        assertTrue(NumberOfPermitsPage.isMaximumExceededErrorPresent());
    }

    @And("I enter number of permits more than the authorised vehicles and click save and continue")
    public void iEnterNumberOfPermitsMoreThanTheAuthorisedVehiclesAndClickSaveAndContinue() {
        NumberOfPermitsPage.enterInvalidNumber();
        UniversalActions.clickSaveAndContinue();
    }

    @And("I enter valid number of permits on the removals number of permits page and click save and continue")
    public void iEnterValidNumberOfPermitsOnTheRemovalsNumberOfPermitsPageAndClickSaveAndContinue() {
        NumberOfPermitsPage.enterRandomValidNumber();
        UniversalActions.clickSaveAndContinue();
    }

    @And("I enter valid number of permits on the removals number of permits page")
    public void iEnterValidNumberOfPermitsOnTheRemovalsNumberOfPermitsPage() {
        NumberOfPermitsPage.enterRandomValidNumber();
    }

    @And("I save and return to overview")
    public void iSaveAndReturnToOverview() {
        UniversalActions.clickSaveAndReturn();
    }
}