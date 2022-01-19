package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

public class ScanningSeparatorGeneration extends BasePage {
    private World world;

    public ScanningSeparatorGeneration(World world) {
        this.world = world;
    }

    @Given("I am on the Scanning page")
    public void iAmOnTheScanningPage() {
        world.internalNavigation.adminNavigation(AdminOption.SCANNING);
    }

    @When("I complete the Compliance Scanning details")
    public void iCompleteTheComplianceScanningDetails() {
        world.adminJourney.completeComplianceScanningDetails();
    }

    @Then("A scanning success message banner should be displayed")
    public void aScanningSuccessMessageBannerShouldBeDisplayed() {
        Assert.assertTrue(isElementPresent("(//p[text()='The separator sheet has been generated'])", SelectorType.XPATH));
    }
}

