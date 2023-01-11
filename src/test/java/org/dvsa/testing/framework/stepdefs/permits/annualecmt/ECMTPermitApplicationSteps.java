package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.IRHPPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static java.lang.Thread.sleep;
import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.junit.jupiter.api.Assertions.*;

public class ECMTPermitApplicationSteps extends BasePermitPage {

    private final World world;

    public ECMTPermitApplicationSteps(World world) {
        this.world = world;
    }
    @Then("the permits tab should not be displayed")
    public void thePermitsTabShouldNotBeDisplayed(String hidden) {
        if (hidden != null) {
            assertFalse(HomePage.isTabPresent(Tab.PERMITS));
        } else {
            assertTrue(HomePage.isTabPresent(Tab.PERMITS));
        }
    }
    @Then("I expect my application to be submitted")
    public void iExpectMyApplicationToBeSubmitted() {
        HomePage.untilOnPage();
        untilAnyPermitStatusMatch(PermitStatus.UNDER_CONSIDERATION);
    }
    @Then("There should be no selected licences")
    public void thereShouldBeNoSelectedLicences() {
        assertFalse(SelectALicencePage.hasSelectedLicence());
    }
    @And("I save and continue")
    public void iSaveAndContinue() {
        BasePermitPage.saveAndContinue();
    }
    @Then("I should be taken to the permits dashboard")
    public void iShouldBeTakenToThePermitsDashboard() {
        assertTrue(isPath(HomePage.PermitsTab.RESOURCE));
    }
    @And("I have completed an ECMT application")
    public void iHaveCompletedAnECMTApplication() {
        EcmtApplicationJourney.completeEcmtApplication(world);
    }
    @When("I withdraw without confirming")
    public void iWithdrawWithoutConfirming() {
        HomePage.PermitsTab.selectFirstOngoingApplication();
        ApplicationDetailsPage.withdraw();
        WithdrawApplicationPage.clickWithdraw();
    }
    @When("I have a partial completed ECMT application")
    public void iHaveAPartialCompletedECMTApplication() {
        EcmtApplicationJourney.beginApplication(world);
        BasePermitPage.back();
    }
    @When("I view the application from ongoing permit application table")
    public void iViewTheApplicationFromOngoingPermitApplicationTable() {
        HomePage.PermitsTab.selectFirstOngoingApplication();
    }
    @Then("I have an annual ECMT application in awaiting fee status")
    public void iHaveAnAnnualECMTApplicationInAwaitingFeeStatus() throws InterruptedException {
        EcmtApplicationJourney.completeEcmtApplication(world);
        IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
        sleep(5000);
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        HomePageJourney.selectPermitTab();
        refreshPage();
        untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
    }
    @Then("the user is navigated to awaiting fee page")
    public void theUserIsNavigatedToAwaitingFeePage() {
        isPath("/permits/\\d+/ecmt-awaiting-fee/");
    }

    @When("I try applying with a licence that has an existing annual ECMT application")
    public void iTryApplyingWithALicenceThatHasAnExistingAnnualECMTApplication() {
        HomePageJourney.beginPermitApplication();
        BasePermitJourney.permitType(PermitType.ECMT_ANNUAL);
        YearSelectionPage.selectECMTValidityPeriod();
        BasePermitJourney.licencePage(world);
    }
}