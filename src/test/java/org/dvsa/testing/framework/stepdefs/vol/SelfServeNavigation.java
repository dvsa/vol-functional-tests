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
}
