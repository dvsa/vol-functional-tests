package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;

public class SystemMessages extends BasePage {
    private final World world;

    public SystemMessages(World world) {
        this.world = world;
    }

    @Given("I am on the System Messages page")
    public void iAmOnTheSystemMessagesPage() {
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
        world.systemMessagesJourney.checkForDisplayedMessageOnExternal();
    }
}
