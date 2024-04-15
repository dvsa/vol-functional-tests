package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;


public class MessagingSelfServe extends BasePage {

        private final World world;

        public MessagingSelfServe(World world) {
            this.world = world;
        }


        @Then("i click on start a new conversation link")
    public void iClickOnStartANewConversationLink() {
            world.messagingJourney.createNewConversation();
    }

    @And("i redirect to the message tab to respond to the case worker's message")
    public void iRedirectToTheMessageTabToRespondToTheCaseWorkerSMessage() {
        world.messagingInternal.iClickTheMessagesHeading();
        world.messagingJourney.replyForMessage();
    }

    @Then("i view the new message that the caseworker has sent")
    public void iViewTheNewMessageThatTheCaseworkerHasSent() {
            world.messagingInternal.iClickTheMessagesHeading();
        world.messagingJourney.viewNewMessage();
    }

    @And("i have opened a new message, which will appear as open")
    public void iHaveOpenedANewMessageWhichWillAppearAsOpen() {
            world.messagingJourney.openMessageStatusCheck();
    }

    @Then("i validate the new message count appears on the messaging tab")
    public void iValidateTheNewMessageCountAppearsOnTheMessagingTab() {
        world.messagingJourney.notificationCount();

    }

    @Then("i click on back button to redirect to conversation page")
    public void iClickOnBackButtonToRedirectToConversationPage() {
        world.messagingJourney.backToConversation();
    }

    @Then("the messages tab is not displayed on the dashboard")
    public void theMessagesTabIsNotDisplayedOnTheDashboard() {
        clickByLinkText("Home");
        world.messagingJourney.messageTabHidden();
    }

    @Then("the messages tab is displayed on the dashboard")
    public void theMessagesTabIsDisplayedOnTheDashboard() {
        clickByLinkText("Home");
        world.messagingJourney.messageTabShown();
    }
}
