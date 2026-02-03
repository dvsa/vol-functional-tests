package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class MessagingSelfServe extends BasePage {

    private static final String CategoryErrorMessage = "Select a Category";
    private static final String LicenceOrApplicationErrorMessage = "Select an application or licence ID";
    private static final String TextFieldErrorMessage = "Enter message";


    private final World world;

    public MessagingSelfServe(World world) {
        this.world = world;
    }

    @Then("i click on start a new conversation link and select the licence number")
    public void iClickOnStartANewConversationLinkAndSelectALicenceNumber() {
        world.messagingJourney.createNewConversationAndSelectTheLicenceNumber();
    }

    @And("i redirect to the message tab to respond to the case worker's message")
    public void iRedirectToTheMessageTabToRespondToTheCaseWorkerSMessage() throws IllegalBrowserException, IOException {
        world.messagingInternal.iClickTheMessagesHeading();
        world.messagingJourney.replyForMessage();
        world.messagingJourney.sendMessage();
    }

    @Then("i view the new message that the caseworker has sent")
    public void iViewTheNewMessageThatTheCaseworkerHasSent() {
        world.messagingInternal.iClickTheMessagesHeading();
        world.messagingJourney.viewNewMessage();
    }

    @And("i have opened a new message, which will appear as open")
    public void iHaveOpenedANewMessageWhichWillAppearAsOpen() throws IllegalBrowserException, IOException {
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
          UniversalActions.clickHome();
        world.messagingJourney.messageTabHidden();
    }

    @Then("the messages tab is displayed on the dashboard")
    public void theMessagesTabIsDisplayedOnTheDashboard() {
          UniversalActions.clickHome();
        world.messagingJourney.messageTabShown();
    }

    @Then("i send a new message without selecting a category, licence or application number and text")
    public void iSendANewMessageWithoutSelectingAnyOption() {
        world.messagingJourney.submitMessageWithoutSelectingAnyOption();
        world.messagingJourney.sendMessage();
    }

    @Then("i should get an error message")
    public void iShouldGetAnErrorMessage() {
        assertTrue(isTextPresent(CategoryErrorMessage));
        assertTrue(isTextPresent(LicenceOrApplicationErrorMessage));
        assertTrue(isTextPresent(TextFieldErrorMessage));
    }

    @Then("i send a reply without entering a message in the text field, and an error message will appear")
    public void iSendAReplyWithoutEnteringAMessageInTheTextFieldAndAnErrorMessageWillAppear() {
        world.messagingInternal.iClickTheMessagesHeading();
        world.messagingJourney.replyErrorMessage();
        world.messagingJourney.sendMessage();
        assertTrue(isTextPresent(TextFieldErrorMessage));
    }

    @And("i click Send message to send a message to the caseworker")
    public void iClickSendMessageToSendAMessageToTheCaseworker() {
       world.messagingJourney.sendMessage();
    }
}
