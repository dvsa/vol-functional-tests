package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.ShortTermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.IRHPPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.DeclineGrantedPermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.GrantedPermitRestrictionsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AwaitingFeePermitSteps extends BasePermitPage implements En {

    public AwaitingFeePermitSteps(World world) {
        And("^I have a short term application in awaiting fee status$", () -> {
            ShortTermECMTJourney.completeShortTermECMTApplication(world);
            IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            HomePage.PermitsTab.selectFirstOngoingApplication();
        });
        And("^the user is taken to the awaiting fee page$", () -> {
            assertTrue(isPath("/permits/application/\\d+/awaiting-fee/"));
        });
        And("^I should be on the short term decline awarded permits page$", () -> {
            DeclineGrantedPermitPage.untilOnPage();
            String heading = DeclineGrantedPermitPage.getPageHeading();
            assertEquals("Decline granted permits", heading);
        });
        And("^I should be on the short term decline awarded permits confirmation page$", () -> {
            PermitsDeclinedPage.untilOnPage();
            String heading = PermitsDeclinedPage.getPanelHeading();
            assertEquals("Permits declined", heading);
        });
        And("^I select the decline confirmation checkbox and confirm$", () -> {
            DeclineGrantedPermitPage.confirmDeclineOfPermits();
            DeclineGrantedPermitPage.saveAndContinue();
        });
        And("^I should see all the relevant texts on permits declined page$", () -> {
            assertTrue(DeclineGrantedPermitPage.isAdvisoryTextPresent());
        });
        And("^I should see all the relevant texts on permits declined confirmation page$", () -> {
            assertTrue(isTextPresent(world.applicationDetails.getLicenceNumber()));
            assertTrue(PermitsDeclinedPage.isAdvisoryTextPresent());
        });
        And("^the declined permit application is not displayed on the permit dashboard$", () -> {
            String permitLicence= String.valueOf(HomePage.PermitsTab.getPermitsWithStatus(HomePage.PermitsTab.Table.ongoing, PermitStatus.AWAITING_FEE));
            assertNotEquals(world.applicationDetails.getLicenceNumber(), permitLicence);
        });
        And("^I select accept and continue button without confirming decline checkbox$", DeclineGrantedPermitPage::saveAndContinue);
        And("^the error message is displayed$", () -> {
            assertTrue(DeclineGrantedPermitPage.isErrorTextPresent());
        });
        And("^I click the view permit restriction link$", PermitFeePage::clickPermitRestrictionLink);
        And("^the user is taken to the allocated candidate permit view page$", () -> {
            assertTrue(isPath("/application/\\d+/unpaid-permits/"));
        });
        And("^the details are displayed as expected$", () -> {
            String heading = GrantedPermitRestrictionsPage.getPageHeading();
            assertEquals("Granted permits restrictions", heading);
            List<String> tableHeadings = GrantedPermitRestrictionsPage.getTableRowHeadings();
            assertEquals("Permit", tableHeadings.get(0));
            assertEquals("Minimum emission standard", tableHeadings.get(1));
            assertEquals("Not valid to travel to", tableHeadings.get(2));
        });
        And("^I select the return to fee overview link$", GrantedPermitRestrictionsPage::returnToFeeSummaryPage);
    }
}