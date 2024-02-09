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
        assertTrue(isTextPresent("Messages"));
    }

    @Then("the messaging heading should not be displayed")
    public void theMessagingHeadingShouldNotBeDisplayed() {
        assertFalse(isTextPresent("Messages"));
    }

    @And("i click the messages heading")
    public static void iClickTheMessagesHeading() {
        clickByLinkText("Messages");
    }

    @Then("the internal messages page is displayed correctly")
    public void theInternalMessagesPageIsDisplayedCorrectly() {
        assertTrue(isTextPresent("Conversations"));
        assertTrue(isElementPresent("(//span[text()='Messages'])[2]", SelectorType.XPATH));
        assertTrue(isElementPresent("New Conversation", SelectorType.LINKTEXT));
        assertTrue(isElementPresent("Disable Messaging", SelectorType.LINKTEXT));
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
