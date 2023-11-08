package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Messaging extends BasePage {
    @Then("the messaging tab should be displayed")
    public void theMessagingTabShouldBeDisplayed() {
        assertTrue(isTextPresent("Messages"));
    }

    @Then("the messaging tab should not be displayed")
    public void theMessagingTabShouldNotBeDisplayed() {
        assertFalse(isTextPresent("Messages"));
    }

    @And("i click the messages tab")
    public void iClickTheMessagesTab() {
        clickByLinkText("Messages");
    }

    @Then("the messages page is displayed correctly")
    public void theMessagesPageIsDisplayedCorrectly() {
        assertTrue(isTitlePresent("Messages", 100));
    }
}
