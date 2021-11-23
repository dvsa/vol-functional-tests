package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class ScanningSeparatorGeneration extends BasePage {
    private World world;

    public ScanningSeparatorGeneration(World world) {
        this.world = world;
    }

    @Given("I am on the Scanning page")
    public void iAmOnTheScanningPage() {
        world.adminJourney.navigateToAdminScanning();
    }

    @When("I select the Compliance Scanning Category")
    public void iSelectTheCaseScanningCategory() {
        world.adminJourney.selectComplianceScanningCategory();

    }
}
