package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
<<<<<<< HEAD
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitFeePage;
import org.dvsa.testing.lib.newPages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.newPages.BasePage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3

public class DeclarationPageSteps extends BasePage implements En {

    public DeclarationPageSteps(World world) {

        When("^I am on  the ECMT Removal Declaration page", () -> {
            EcmtInternationalRemovalJourney.completeUntilDeclarationPage(world);
        });
        And ("^the declaration page has correct link under guidance notes", DeclarationPage::isGuidanceNotesLinkPresent);
        And ("^the declaration page checkbox has the correct text and displayed unselected by default", DeclarationPageJourney::hasCheckboxText);
        And ("^I click declaration link on the Ecmt removal overview page again", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.Declaration);
        });
        When("^I confirm the declaration$", DeclarationPage::confirmDeclaration);
        Then("^I am on ECMT removal permits overview page with Declaration section marked as complete$", () -> {
            OverviewPage.untilOnPage();
            OverviewPageJourney.checkStatus(OverviewSection.Declaration, PermitStatus.COMPLETED);
        });
        Then("^I am directed to the ECMT removals permit fee page$", PermitFeePage::untilOnPage);
        Then("^I'm viewing my saved ECMT International application in internal$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            refreshPage();
            world.internalNavigation.urlSearchAndViewLicence();
            clickByLinkText("IRHP Permits");
            clickByLinkText(world.applicationDetails.getLicenceNumber());
        });
        Then("^I am continuing on the on-going ECMT removal application$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            HomePage.PermitsTab.selectFirstOngoingApplication();
        });
        Then("^I am on the ECMT removal application submitted page$", () -> {
           isPath("/permits/application/\\d+/submitted/");
        });
        Then("^I select the back to home link$", () -> {
           FeesDetailsPage.untilOnFeePage();
           FeesDetailsPage.clickBackToHome();
        });
    }
}
