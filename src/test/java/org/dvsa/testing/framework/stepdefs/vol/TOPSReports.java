package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class TOPSReports extends BasePage {

    private World world;

    public TOPSReports(World world) {
        this.world = world;
    }

    @And("I have navigated and logged into the TOPS report portal")
    public void iHaveNavigatedAndLoggedIntoTheTOPSReportPortal() {
        world.topsJourney.navigateAndLoginToTopsReport();
    }

    @Given("I have navigated to the Operator Compliance Risk Score Page")
    public void iHaveNavigatedToTheOperatorComplianceRiskScorePage() {
        world.topsJourney.viewComplianceRiskScore();
    }
}
