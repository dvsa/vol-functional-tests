package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

public class SelfServeNavigation extends BasePage {

    public World world;

    public SelfServeNavigation(World world) {
        this.world = world;
    }
    @When("i navigate to the {string} safety and compliance page")
    public void iNavigateToTheSafetyAndCompliancePage(String type) {
        world.selfServeNavigation.navigateToPage(type, SelfServeSection.SAFETY_AND_COMPLIANCE);
    }

    @And("i navigate to the {string} directors page")
    public void iNavigateToTheDirectorsPage(String type) {
        world.selfServeNavigation.navigateToPage(type, SelfServeSection.DIRECTORS);
    }

    @And("i navigate to the manage users page")
    public void iNavigateToTheManageUsersPage() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        waitAndClick("//*[contains(text(),'Manage users')]", SelectorType.XPATH);
        Assert.assertEquals("Manage users", getText("h1", SelectorType.CSS));
    }

    @And("i navigate to the apply for a licence page")
    public void iNavigateToTheApplyForALicencePage() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.VIEW);
    }

    @And("i navigate to the application licence authorisation page")
    public void iNavigateToTheLicenceAuthorisationPage() {
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.LICENCE_AUTHORISATION);
    }

    @And("i navigate to the application review and declarations page")
    public void iNavigateToTheApplicationReviewAndDeclarationsPage() {
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.REVIEW_AND_DECLARATIONS);
    }

    @And("i navigate to the application operating centres and authorisations page")
    public void iNavigateToTheOperatingCentresAndAuthorisationsPage() {
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
    }

    @When("i am on the {string} financial evidence page and click on the How Did We Calculate This Link")
    public void iAmOnTheFinancialEvidencePageAndClickOnTheHowDidWeCalculateThisLink(String statusType) {
        world.selfServeNavigation.navigateToPage(statusType, SelfServeSection.FINANCIAL_EVIDENCE);
        click("//span[contains(text(),'How did we calculate this?')]", SelectorType.XPATH);
    }

    @When("i am on the operating centre and authorisations page")
    public void iAmOnTheOperatingCentreAndAuthorisationsPage() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
    }
}
