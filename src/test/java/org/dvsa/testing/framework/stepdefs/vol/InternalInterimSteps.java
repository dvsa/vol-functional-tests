package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;

public class InternalInterimSteps {
    private final World world;

    public InternalInterimSteps(World world) {
        this.world = world;
    }

    @When("I remove the interim request on Internal")
    public void iRemoveTheInterimRequestOnInternal() {
        world.internalInterimJourney.removeInterimRequest();
    }

    @Then("the Internal fees page should show {string} with its recorded amount and status {string}")
    public void theInternalFeesPageShouldShowFee(String description, String status) throws HttpException {
        world.internalInterimJourney.verifyInterimFee(description, status);
    }

    @Then("the application overview should show interim status {string}")
    public void theApplicationOverviewShouldShowInterimStatus(String expectedStatus) {
        world.internalInterimJourney.verifyApplicationInterimStatus(expectedStatus);
    }
}
