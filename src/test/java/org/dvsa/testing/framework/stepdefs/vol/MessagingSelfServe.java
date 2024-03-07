package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.licence.MessagingJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class MessagingSelfServe extends BasePage {

    @Then("i click on start a new conversation link")
    public void iClickOnStartANewConversationLink() {
        MessagingJourney.createNewConversation();
    }

    @And("i redirect to the message tab to respond to the case worker's message")
    public void iRedirectToTheMessageTabToRespondToTheCaseWorkerSMessage() {
        MessagingInternal.iClickTheMessagesHeading();
        MessagingJourney.replyForMessage();
    }

    @Then("i view the new message that the caseworker has sent")
    public void iViewTheNewMessageThatTheCaseworkerHasSent() {
        MessagingInternal.iClickTheMessagesHeading();
        MessagingJourney.viewNewMessage();
    }

    @And("i have opened a new message, which will appear as open")
    public void iHaveOpenedANewMessageWhichWillAppearAsOpen() {
        MessagingJourney.openMessageStatusCheck();
    }

    @Then("i validate the new message count appears on the messaging tab")
    public void iValidateTheNewMessageCountAppearsOnTheMessagingTab() {
        MessagingJourney.notificationCount();

    }

    @Then("i click on back button to redirect to conversation page")
    public void iClickOnBackButtonToRedirectToConversationPage() {
        MessagingJourney.backToConversation();
    }

}
