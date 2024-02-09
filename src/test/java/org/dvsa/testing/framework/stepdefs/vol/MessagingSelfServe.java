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

    @And("I redirect to the message tab to respond to the case worker's message")
    public void iRedirectToTheMessageTabToRespondToTheCaseWorkerSMessage() {
        MessagingInternal.iClickTheMessagesHeading();
        MessagingJourney.replyForMessage();
    }
}
