package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PresidingTCsStep extends BasePage {
    private final World world;

    public PresidingTCsStep(World world) {
        this.world = world;
    }

    @When("I am on the Presiding TC page")
    public void iAmOnThePresidingTCPage() throws HttpException {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.adminNavigation(AdminOption.PRESIDING_TCS);
    }

    @And("an admin adds a Presiding TC")
    public void anAdminAddsAPresidingTC() {
        world.submissionsJourney.addPresidingTC();
    }

    @Then("the Presiding TC should be displayed")
    public void thePresidingTCShouldBeDisplayed() {
        assertTrue(isTextPresent(world.DataGenerator.getOperatorForeName()));
    }

    @When("I add a Presiding TC and then create a Submission")
    public void iAddAPresidingTCAndThenCreateASubmission() {
        world.internalNavigation.adminNavigation(AdminOption.PRESIDING_TCS);
        world.submissionsJourney.addPresidingTC();
        world.internalNavigation.getLicence();
        world.submissionsJourney.createAndSubmitSubmission();
        world.submissionsJourney.setInfoCompleteAndAssignSubmission();
    }

    @Then("I can view the added Presiding TC in the drop down list")
    public void iCanViewTheAddedPresidingTCInTheDropDownList() {
        assertTrue(isTextPresent(getSelectedValue()));
    }
}
