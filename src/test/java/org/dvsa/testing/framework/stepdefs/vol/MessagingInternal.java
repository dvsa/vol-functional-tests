package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.io.IOException;

public class MessagingInternal extends BasePage {
    private static final String CategoryErrorMessage = "Select a Category";
    private static final String TextFieldErrorMessage = "Enter message";

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

    @When("i click the messages heading")
    public void iClickTheMessagesHeading() {
        waitAndClickByLinkText("Messages");
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

    @When("i sent a new message without selecting a category and text from internal application")
    public void iSentANewMessageWithoutSelectingACategoryAndTextFromInternalApplication() {
        world.messagingJourney.submitMessageWithoutOptions();
    }

    @Then("the error message will display on message page")
    public void theErrorMessageWillDisplayOnMessagePage() {
        assert (isTextPresent(CategoryErrorMessage));
        assert (isTextPresent(TextFieldErrorMessage));
    }

    @And("i reply for the operators message")
    public void iReplyForTheOperatorsMessage() {
        world.messagingJourney.replyToOperator();
        world.messagingJourney.sendMessage();
    }

    @And("i reply to operator without a text to validate an error message")
    public void iReplyToOperatorWithoutATextToValidateAnErrorMessage() {
        world.messagingJourney.replyOperatorErrorMessage();
        world.messagingJourney.sendMessage();
        assert (isTextPresent(TextFieldErrorMessage));
    }
}
