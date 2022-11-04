package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.IRHPPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.PermitStartDatePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortTerm2020APGGEndToEndJourneyIncludingIssuedPermitsPageSteps extends BasePage implements En {
    public ShortTerm2020APGGEndToEndJourneyIncludingIssuedPermitsPageSteps(World world)  {
        Then("^I select short term ecmt period", () -> {
            PeriodSelectionPage.selectFirstAvailablePermitPeriod();
            PeriodSelectionPage.saveAndContinue();
        });
        Then("^I am on the Permits start date page$", () -> {
            PermitStartDatePage.permitDate();
            BasePermitPage.saveAndContinue();
        });

        Then("^I complete APGG Cabotage page section and click save and continue$", () -> {
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
        });

        Then("^I am navigated back to the permits dashboard page with my application status shown as Not yet Submitted", () -> {
            assertEquals(getElementValueByText("//strong[@class='govuk-tag govuk-tag--grey']",SelectorType.XPATH),"NOT YET SUBMITTED");
            HomePage.PermitsTab.selectFirstOngoingApplication();
        });

        When("^I'm  viewing my saved Short term ECMT APGG application in internal and Granting Permit$", () -> {
            IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
        });

        When("^I login back to the External to view the application in status of awaiting fee", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
            HomePage.PermitsTab.selectFirstOngoingApplication();
        });
        When("^the application status on the external changes to awaiting fee", IrhpPermitsApplyPage::permitsFeePage);

        When("^I accept and pay the APGG issuing fee", ApplicationIssuingFeePage::acceptAndPay);

        And("^I make card payment", () -> {
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.untilOnPage();
            SubmittedPage.goToPermitsDashboard();
        });

        Then("^My application status changes to Valid", () -> {
            untilAnyPermitStatusMatch(PermitStatus.VALID);
            HomePage.PermitsTab.waitUntilIssuedPermitsAndCertificatesHeading();
        });
        And("^I select short term ecmt permit on the select permit page$", () -> {
            CommonSteps.clickToPermitTypePage(world);
            BasePermitJourney.permitType(PermitType.SHORT_TERM_ECMT);
        });
    }
}