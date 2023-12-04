package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Messaging extends BasePage {
    @Then("the messaging heading should be displayed")
    public void theMessagingHeadingShouldBeDisplayed() {
        assertTrue(isTextPresent("Messages"));
    }

    @Then("the messaging heading should not be displayed")
    public void theMessagingHeadingShouldNotBeDisplayed() {
        assertFalse(isTextPresent("Messages"));
    }

    @And("i click the messages heading")
    public void iClickTheMessagesHeading() {
        clickByLinkText("Messages");
    }

    @Then("the internal messages page is displayed correctly")
    public void theInternalMessagesPageIsDisplayedCorrectly() {
        assertTrue(isTextPresent("Conversations"));
        assertTrue(isElementPresent("(//span[text()='Messages'])[2]", SelectorType.XPATH));
        assertTrue(isElementPresent("New Conversation", SelectorType.LINKTEXT));
        assertTrue(isElementPresent("Disable Messaging", SelectorType.LINKTEXT));
    }
}
