package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.DeclineGrantedPermitPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.GrantedPermitRestrictionsPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTInternationalRemovalOnly.PermitStartDatePage;
import org.dvsa.testing.lib.newPages.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.internal.details.LicenceDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.junit.Assert;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AwaitingFeePermitSteps extends BasePermitPage implements En {

    public AwaitingFeePermitSteps(OperatorStore operatorStore, World world) {
        And("^I have a short term application in awaiting fee status$", () -> {
            CommonSteps.clickToPermitTypePage(world);
            ECMTShortTermJourney.getInstance()
                    .permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            PeriodSelectionPage.selectFirstAvailablePermitPeriod();
            PeriodSelectionPage.saveAndContinue();
            ECMTShortTermJourney.getInstance()
                    .licencePage(operatorStore, world);
            OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPage.saveAndContinue();
            CheckIfYouNeedECMTPermitsPage.hasErrorMessagePresent();
            CheckIfYouNeedECMTPermitsPageJourney.completePage();
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPageJourney.completeECMTPage();
            PermitStartDatePage.permitDate();
            BasePermitPage.saveAndContinue();
            EmissionStandardsPageJourney.completePage();
            saveAndContinue();
            DeclarationPageJourney.completeDeclaration();
            PermitFeePage.submitAndPay();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .passwordAuthorisation();
            SubmittedPage.untilOnPage();
            BilateralJourneySteps.clickFinishButton();


            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());

            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);

        LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
        operatorStore.withLicences(licenceStore);

            IrhpPermitsApplyPage.licence();
            String browser = String.valueOf(getURL());
            get(browser+"irhp-application/");
            IrhpPermitsApplyPage.viewApplication();
            IrhpPermitsApplyPage.grantApplication();
            IrhpPermitsApplyPage.continueButton();

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            String licence1 = operatorStore.getCurrentLicenceNumber().toString().substring(9, 18);
            HomePage.PermitsTab.selectOngoing(licence1);
        });
        And("^the user is taken to the awaiting fee page$", () -> {
            Assert.assertTrue(isPath("/permits/application/\\d+/awaiting-fee/"));
        });
        And("^I select Decline Permits button$", () -> {
            scrollAndClick("//*[contains(text(), 'Decline permits')]", SelectorType.XPATH);
        });
        And("^I should be on the short term decline awarded permits page$", () -> {
            DeclineGrantedPermitPage.untilOnPage();
            String heading = DeclineGrantedPermitPage.getPageHeading();
            assertEquals("Decline granted permits", heading);
        });
        And("^I select the decline confirmation checkbox and confirm$", () -> {
            DeclineGrantedPermitPage.confirmDeclineOfPermits();
            DeclineGrantedPermitPage.saveAndContinue();
        });
        And("^I should see all the relevant texts on permits declined page$", () -> {
            assertTrue(DeclineGrantedPermitPage.getAdvisoryText());
        });
        And("^the declined permit application is not displayed on the permit dashboard$", () -> {
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            String permitLicence= String.valueOf(HomePage.PermitsTab.permitsWithStatus(HomePage.PermitsTab.Table.ongoing, PermitStatus.AWAITING_FEE));
            Assert.assertNotEquals(licence1,permitLicence);
        });
        And("^I select accept and continue button without confirming decline checkbox$", DeclineGrantedPermitPage::saveAndContinue);
        And("^the error message is displayed$", () -> {
            assertTrue(DeclineGrantedPermitPage.isErrorTextPresent());
        });
        And("^I click the view permit restriction link$", () -> {
            PermitFeePage.clickPermitRestrictionLink();
        });
        And("^the user is taken to the allocated candidate permit view page$", () -> {
            isPath("/application/\\d+/unpaid-permits/");
        });
        And("^the details are displayed as expected$", () -> {
            String heading = GrantedPermitRestrictionsPage.getPageHeading();
            assertEquals("Granted permits restrictions", heading);
            List<String> tableHeadings = GrantedPermitRestrictionsPage.getTableRowHeadings();
            Assert.assertEquals("Permit", tableHeadings.get(1));
            Assert.assertEquals("Minimum emission standard", tableHeadings.get(2));
            Assert.assertEquals("Not valid to travel to", tableHeadings.get(3));
        });
        And("^I select the return to fee overview link$", GrantedPermitRestrictionsPage::returnToFeeSummaryPage);

    }
}