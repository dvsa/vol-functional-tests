package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.ApplicationIssuingFeePage;
import org.dvsa.testing.lib.pages.external.permit.BaseApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.Permits;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.PermitStartDatePage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CabotagePage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.PeriodSelectionPageOne;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.SubmittedPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;

public class ShortTerm2020APGGEndToEndJourneyIncludingIssuedPermitsPageSteps extends BasePage implements En {
    public ShortTerm2020APGGEndToEndJourneyIncludingIssuedPermitsPageSteps(OperatorStore operatorStore, World world)  {
        LicenceStore licenceStore = operatorStore.getCurrentLicence().orElseGet(LicenceStore::new);
        Then("^I select short term ecmt period", () -> {
            PeriodSelectionPageOne.periodSelection();
            PeriodSelectionPageOne.continueButton();
        });
        Then("^I am on the Permits start date page$", () -> {
            PermitStartDatePage.permitDate();
            BasePermitPage.saveAndContinue();
        });
        Then("^I complete APGG Cabotage page section and click save and continue$", () -> {
            CabotagePage.cabotageConfirmation();
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

            BaseInternalJourney.getInstance().openLicence(
                    licence.getLicenceId()
            ).signin();
            IrhpPermitsApplyPage.licence();
            String browser = String.valueOf(getURL());
            System.out.println(getURL());
            get(browser+"irhp-application/");
            IrhpPermitsApplyPage.viewApplication();
            IrhpPermitsApplyPage.grantApplication();
            IrhpPermitsApplyPage.continueButton();
        });



        When("^I login back to the External to view the application in status of awaiting fee", () -> {


            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());

            LoginPage.signIn(world.get("username"), world.get("password"));
            HomePage.selectTab(Tab.PERMITS);
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
        });
        When("^the application status on the external changes to awaiting fee", IrhpPermitsApplyPage::permitsFeePage);

        When("^I accept and pay the APGG issuing fee", ApplicationIssuingFeePage::acceptAndPay);

        And("^I make card payment", () -> {
            EcmtApplicationJourney.getInstance().cardDetailsPage().cardHolderDetailsPage().confirmAndPay().passwordAuthorisation();
            SubmittedPage.untilOnPage();
            BaseApplicationSubmitPage.finish();
        });

        Then("^My application status changes to Valid", () -> {
            untilAnyPermitStatusMatch(PermitStatus.VALID);
            Permits.Permit.hasIssuedPermitsAndCertificates();

    });
}
}