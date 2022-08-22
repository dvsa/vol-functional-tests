package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import cucumber.api.java8.En;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ECMTPermitApplicationSteps extends BasePermitPage implements En {

    public ECMTPermitApplicationSteps(World world) {
        Then("^the permits tab should (not )?be displayed$", (String hidden) -> {
            if (hidden != null) {
                assertFalse(HomePage.isTabPresent(Tab.PERMITS));
            } else {
                assertTrue(HomePage.isTabPresent(Tab.PERMITS));
            }
        });
        Then("^I expect my application to be submitted$", () -> {
            HomePage.untilOnPage();
            untilAnyPermitStatusMatch(PermitStatus.UNDER_CONSIDERATION);
        });
        Then("^There should be no selected licences$", () -> assertFalse(SelectALicencePage.hasSelectedLicence()));
        And("^I click save and continue$", BasePermitPage::saveAndContinue);
        Then("^I should be taken to the permits dashboard$", () -> assertTrue(isPath(HomePage.PermitsTab.RESOURCE)));

        And("^I have completed an ECMT application$", () -> {
            EcmtApplicationJourney.completeEcmtApplication(world);
        });
        When("^I withdraw without confirming$", () -> {
            HomePage.PermitsTab.selectFirstOngoingApplication();
            ApplicationDetailsPage.withdraw();
            WithdrawApplicationPage.clickWithdraw();
        });
        When("^I have a partial completed ECMT application$", () -> {
            EcmtApplicationJourney.beginApplication(world);
            BasePermitPage.back();
        });
        When("^I view the application from ongoing permit application table$", HomePage.PermitsTab::selectFirstOngoingApplication);
        And("^I have an annual ECMT application in awaiting fee status$", () -> {
            EcmtApplicationJourney.completeEcmtApplication(world);
            IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
            sleep(5000);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            refreshPage();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
        });
        Then("^the user is navigated to awaiting fee page$", () -> assertTrue(isPath("/permits/\\d+/ecmt-awaiting-fee/")));

        When("^I try applying with a licence that has an existing annual ECMT application$", () -> {
            HomePageJourney.beginPermitApplication();
            BasePermitJourney.permitType(PermitType.ECMT_ANNUAL);
            YearSelectionPage.selectECMTValidityPeriod();
            BasePermitJourney.licencePage(world);
        });
    }
}