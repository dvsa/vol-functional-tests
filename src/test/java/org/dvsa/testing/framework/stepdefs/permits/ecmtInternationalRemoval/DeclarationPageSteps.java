package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitFeePage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.internal.details.FeesDetailsPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class DeclarationPageSteps extends BasePage implements En {

    public DeclarationPageSteps(World world, OperatorStore operatorStore) {

        When("^I am on  the ECMT Removal Declaration page", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewSection.RemovalsEligibility)
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage()
                    .numberOfPermits()
                    .checkYourAnswers();
        });
        Then ("^the page heading on Ecmt Removal declaration page is displayed correctly", DeclarationPage::untilOnPage);
        And ("^the Ecmt removal declaration page has reference number", DeclarationPage::hasReference);
        And ("^the Ecmt removal declaration page has correct link under guidance notes", DeclarationPage::hasGuidanceNotesLinkPresent);
        And ("^the advisory text on removal declaration page is displayed correctly", DeclarationPage::hasECMTAdvisoryText);
        And ("^the Ecmt removal declaration page checkbox has the correct text and displayed unselected by default", DeclarationPage::hasCheckboxText);
        And ("^I should see the validation error message on the Ecmt removal declaration page", DeclarationPage::hasErrorText);
        And ("^I click declaration link on the Ecmt removal overview page again", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.Declaration);
        });
        When  ("^I confirm the ECMT removal declaration", DeclarationPage::confirmDeclaration);
        Then("^I am on ECMT removal permits overview page with Declaration section marked as complete$", () -> {
            OverviewPage.untilOnPage();
            OverviewPage.checkStatus(OverviewSection.Declaration, PermitStatus.COMPLETED);
        });
        Then("^I am directed to the ECMT removals permit fee page$", PermitFeePage::untilOnPage);
        Then("^I'm viewing my saved ECMT International application in internal$", () -> {
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            refreshPage();
        });
        Then("^I am continuing on the on-going ECMT removal application$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
        });
        Then("^I am on the ECMT removal application submitted page$", () -> {
           isPath("/permits/application/\\d+/submitted/");
        });
        Then("^I select the back to home link$", () -> {
           FeesDetailsPage.untilOnFeePage();
           FeesDetailsPage.backToHomeLink();
        });


    }
}
