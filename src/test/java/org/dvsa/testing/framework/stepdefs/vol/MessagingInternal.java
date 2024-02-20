package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.licence.MessagingJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessagingInternal extends BasePage {
    @Then("the messaging heading should be displayed")
    public void theMessagingHeadingShouldBeDisplayed() {
        MessagingJourney.messageHeading();
    }

    @Then("the messaging heading should not be displayed")
    public void theMessagingHeadingShouldNotBeDisplayed() {
        MessagingJourney.messageHeading();
    }

    @And("i click the messages heading")
    public static void iClickTheMessagesHeading() {
        clickByLinkText("Messages");
    }

    @Then("the internal messages page is displayed correctly")
    public void theInternalMessagesPageIsDisplayedCorrectly() {
        MessagingJourney.internalMessagePageDisplay();
    }

    @And("i create a new conversation to operator")
    public void iCreateANewConversationToOperator() {
        MessagingJourney.createConversation();
    }

    @Then("i end and archive the conversation")
    public void iEndAndArchiveTheConversation() {
        MessagingJourney.archiveTheConversation();
    }

    @When("i select a message check box and team")
    public void iSelectAMessageCheckBoxAndTeam() {
        MessagingJourney.selectMessageCheckBox();
    }

    @Then("i should able to see new task created as new message for case worker")
    public void iShouldAbleToSeeNewTaskCreatedAsNewMessageForCaseWorker() {
        MessagingJourney.checkForNewTask();
    }
}
