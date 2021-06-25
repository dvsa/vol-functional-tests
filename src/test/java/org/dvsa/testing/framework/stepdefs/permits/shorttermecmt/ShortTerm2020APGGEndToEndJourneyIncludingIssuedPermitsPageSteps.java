package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.newPages.external.pages.ApplicationIssuingFeePage;
import org.dvsa.testing.lib.newPages.external.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTInternationalRemovalOnly.PermitStartDatePage;
import org.dvsa.testing.lib.newPages.external.pages.PeriodSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.Permits;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
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
            String licence= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            Assert.assertEquals(getElementValueByText("//span[@class='status grey']",SelectorType.XPATH),"NOT YET SUBMITTED");
            HomePage.PermitsTab.selectOngoing(licence);
        });

        When("^I'm  viewing my saved Short term ECMT APGG application in internal and Granting Permit$", () -> {
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsApplyPage.licence();
            String browser = String.valueOf(getURL());
            System.out.println(getURL());
            get(browser+"irhp-application/");
            IrhpPermitsApplyPage.viewApplication();
            IrhpPermitsApplyPage.grantApplication();
            IrhpPermitsApplyPage.continueButton();
        });

        When("^I login back to the External to view the application in status of awaiting fee", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
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
            Permits.Permit.hasIssuedPermitsAndCertificates();
    });
}
}