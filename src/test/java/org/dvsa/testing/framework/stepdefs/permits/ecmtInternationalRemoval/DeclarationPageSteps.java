package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;

public class DeclarationPageSteps extends BasePage {
     World world;

    public DeclarationPageSteps(World world) {
        this.world = world;
    }

    @When("I am on the ECMT Removal Declaration page")
    public void iAmOnTheECMTRemovalDeclaration() {
        world.ecmtInternationalRemovalJourney.completeUntilDeclarationPage();
    }

    @And("the declaration page has correct link under guidance notes")
    public void theDeclarationPageHasCorrectLink() {
        DeclarationPage.isGuidanceNotesLinkPresent();
    }

    @And("the declaration page checkbox has the correct text and displayed unselected by default")
    public void theDeclarationPageCheckbox() {
        DeclarationPageJourney.hasCheckboxText();
    }

    @And("I click declaration link on the Ecmt removal overview page again")
    public void iClickDeclarationLink() {
        OverviewPageJourney.clickOverviewSection(OverviewSection.Declaration);
    }

    @When("I confirm the declaration")
    public void iConfirmTheDeclaration() {
        DeclarationPage.confirmDeclaration();
    }

    @Then("I am on ECMT removal permits overview page with Declaration section marked as complete")
    public void iAmOnECMTRemovalPermits() {
        OverviewPage.untilOnPage();
        OverviewPageJourney.checkStatus(OverviewSection.Declaration, PermitStatus.COMPLETED);
    }

    @Then("I am directed to the ECMT removals permit fee page")
    public void iAmDirectedToTheECMTRemovals() {
        world.permitFeePage.untilOnPage();
    }

    @Then("I'm viewing my saved ECMT International application in internal")
    public void iAmViewingMySavedECMTInternationalApplication() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        clickByLinkText("IRHP Permits");
        clickByLinkText(world.applicationDetails.getLicenceNumber());
    }

    @Then("I am continuing on the on-going ECMT removal application")
    public void iAmContinuingOnTheOngoingECMT() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        HomePageJourney.selectPermitTab();
        HomePage.PermitsTab.selectFirstOngoingApplication();
    }

    @Then("I am on the ECMT removal application submitted page")
    public void iAmOnTheECMTRemovalApplication() {
        isPath("/permits/application/\\d+/submitted/");
    }

    @Then("I select the back to home link")
    public void iSelectTheBackToHomeLink() {
        FeesDetailsPage.untilOnFeePage();
        FeesDetailsPage.clickBackToHome();
    }
}
