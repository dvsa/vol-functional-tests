package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SystemMessages extends BasePage {
    private final World world;

    public SystemMessages(World world) {
        this.world = world;
    }

    @Given("I am on the System Messages page")
    public void iAmOnTheSystemMessagesPage() throws HttpException {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.adminNavigation(AdminOption.SYSTEM_MESSAGES);
    }

    @When("I add an external message")
    public void iAddAnExternalMessage() {
        world.systemMessagesJourney.addExternalMessage();
    }

    @And("I select a current time period")
    public void iSelectACurrentTimePeriod() {
        world.systemMessagesJourney.inputCurrentTimeDate();
    }

    @Then("The message should be displayed on the external screen")
    public void theMessageShouldBeDisplayedOnTheExternalScreen() {
        world.selfServeNavigation.navigateToLoginPage();
        assertTrue(isTextPresent(world.DataGenerator.getRandomWord()));
    }
}