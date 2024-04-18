package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.licence.MessagingJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class MessagingInternal extends BasePage {

    private final World world;

    public MessagingInternal(World world) {
        this.world = world;
    }

    @Then("the messaging heading should be displayed")
    public void theMessagingHeadingShouldBeDisplayed() {
        world.messagingJourney.messageHeading();
    }

    @Then("the messaging heading should not be displayed")
    public void theMessagingHeadingShouldNotBeDisplayed() {
        world.messagingJourney.messageHeading();
    }

    @And("i click the messages heading")
    public void iClickTheMessagesHeading() {
        clickByLinkText("Messages");
    }

    @Then("the internal messages page is displayed correctly")
    public void theInternalMessagesPageIsDisplayedCorrectly() {
        world.messagingJourney.internalMessagePageDisplay();
    }

    @And("i create a new conversation to operator")
    public void iCreateANewConversationToOperator() {
        world.messagingJourney.createConversation();
        world.messagingJourney.openMessageStatusCheck();
    }

    @And("i create a new conversation to operator and archive the conversation")
    public void iCreateANewConversationToEndAndArchiveTheConversation() {
        world.messagingJourney.createConversation();
        world.messagingJourney.archiveTheConversation();
    }

    @Then("i should able to see new task created as new message for case worker")
    public void iShouldAbleToSeeNewTaskCreatedAsNewMessageForCaseWorker() {
        world.messagingJourney.checkForNewTask();
    }

    @And("the internal user disables messaging")
    public void theInternalUserDisablesMessaging() {
        world.messagingJourney.disableMessaging();
    }
}
