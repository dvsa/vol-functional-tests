package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.IRHPPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.ECMTInternationalRemovalOnly.PermitStartDatePage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.newPages.BasePage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;

public class ShortTerm2020APGGEndToEndJourneyIncludingIssuedPermitsPageSteps extends BasePage implements En {
    public ShortTerm2020APGGEndToEndJourneyIncludingIssuedPermitsPageSteps(OperatorStore operatorStore, World world)  {
        LicenceStore licenceStore = operatorStore.getCurrentLicence().orElseGet(LicenceStore::new);
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
            Assert.assertEquals(getElementValueByText("//span[@class='status grey']",SelectorType.XPATH),"NOT YET SUBMITTED");
            HomePage.PermitsTab.selectFirstOngoingApplication();
        });

        When("^I'm  viewing my saved Short term ECMT APGG application in internal and Granting Permit$", () -> {
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());

            IRHPPageJourney.logInToInternalAndIRHPGrantApplication();
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
            world.feeAndPaymentJourneySteps.customerPaymentModule();
            SubmittedPage.untilOnPage();
            SubmittedPage.goToPermitsDashboard();
        });

        Then("^My application status changes to Valid", () -> {
            untilAnyPermitStatusMatch(PermitStatus.VALID);
            HomePage.PermitsTab.waitUntilIssuedPermitsAndCertificatesHeading();
        });
    }
}