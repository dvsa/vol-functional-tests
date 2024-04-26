package org.dvsa.testing.framework.Journeys.licence;

import activesupport.string.Str;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

import static org.junit.jupiter.api.Assertions.*;


public class MessagingJourney extends BasePage {

    private final World world;

    public MessagingJourney(World world) {
        this.world = world;
    }


    public void messageHeading() {
        assertTrue(isTextPresent("Messages"));
    }


    public void internalMessagePageDisplay() {
        assertTrue(isTextPresent("Conversations"));
        assertTrue(isElementPresent("(//span[text()='Messages'])[2]", SelectorType.XPATH));
        assertTrue(isElementPresent("New Conversation", SelectorType.LINKTEXT));
        assertTrue(isElementPresent("Disable Messaging", SelectorType.LINKTEXT));
    }

    public void createConversation() {
        click("//*[contains(text(),'New Conversation')]", SelectorType.XPATH);
        selectRandomValueFromDropDown("//*[@id='subject']", SelectorType.XPATH);
        waitAndEnterText("//*[@id='fields[messageContent]']", SelectorType.XPATH, Str.randomWord(10));
        clickById("form-actions[submit]");
    }

    public void replyForMessage() {
        if (getText("//*/strong[contains(@class,'govuk-tag govuk-tag--red')]", SelectorType.XPATH).equals("NEW MESSAGE"))
            ;
        waitAndClick("//*[contains(@class,'govuk-body govuk-link govuk-!-padding-right-1 govuk-!-font-weight-bold')]", SelectorType.XPATH);
        click("//span[contains(@class,'govuk-details__summary-text')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='form-actions[inputs][reply]']", SelectorType.XPATH, Str.randomWord(10));
        clickById("send");
    }

    public void archiveTheConversation() {
        click("//button[@id='close']", SelectorType.XPATH);
        waitForTextToBePresent("End Conversation");
        click("//button[@id='close']", SelectorType.XPATH);
        waitForTextToBePresent("CLOSED");
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag--grey')]", SelectorType.XPATH), "CLOSED");
    }

    public void createNewConversation() {
        clickByLinkText("Start a new conversation");
        selectRandomValueFromDropDown("//*[@id='form-actions[inputs][messageSubject]']", SelectorType.XPATH);
        click("//*[@id='form-actions[inputs][appOrLicNo]']", SelectorType.XPATH);
        click("//*[@id='form-actions[inputs][appOrLicNo]']/optgroup[1]/option ", SelectorType.XPATH);
        waitAndEnterText("//*[@id='form-actions[inputs][messageContent]']", SelectorType.XPATH, Str.randomWord(10));
        clickById("send");
    }

    public void checkForNewTask() {
        clickByLinkText("Processing");
        assertTrue(isElementPresent("New message", SelectorType.LINKTEXT));
    }

    public void viewNewMessage() {
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag--red')]", SelectorType.XPATH), "NEW MESSAGE");
        click("//*[@id='main-content']//tbody/tr[1]/td[1]/a", SelectorType.XPATH);
    }

    public void openMessageStatusCheck() {
        clickByLinkText("Messages");
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag--blue')]", SelectorType.XPATH), "OPEN");
    }

    public void notificationCount() {
        assertEquals(getText("//*[contains(@class,'notification-count__number')]", SelectorType.XPATH), "1");
        clickByLinkText("Messages");
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag--red')]", SelectorType.XPATH), "NEW MESSAGE");
    }

    public void backToConversation() {
        click("//*[contains(@class,'govuk-back-link')]", SelectorType.XPATH);
        click("//*[contains(@class,'govuk-body govuk-link govuk-!-padding-right-1 ')]", SelectorType.XPATH);
        click("//*[contains(@class,'govuk-back-link')]", SelectorType.XPATH);
    }


    public void messageTabHidden() {
        assertFalse(HomePage.isTabPresent(Tab.MESSAGES));
    }

    public void messageTabShown() {
        assertTrue(HomePage.isTabPresent(Tab.MESSAGES));
    }

    public void disableMessaging() {
        clickByLinkText("Messages");
        clickByLinkText("Disable Messaging");
        waitAndClick("close", SelectorType.ID);
        waitForTextToBePresent("Messaging will be disabled for this operator");
        click("close", SelectorType.ID);
        waitForTextToBePresent("Messaging has been disabled for this operator");
    }

    public void submitMessageWithoutSelectingAnyOption() {
        clickByLinkText("Start a new conversation");
        clickById("send");
    }

    public void submitMessageWithoutOptions() {
        click("//*[contains(text(),'New Conversation')]", SelectorType.XPATH);
        clickById("form-actions[submit]");
    }

    public void replyErrorMessage() {
        if (getText("//*/strong[contains(@class,'govuk-tag govuk-tag--red')]", SelectorType.XPATH).equals("NEW MESSAGE"))
            ;
        waitAndClick("//*[contains(@class,'govuk-body govuk-link govuk-!-padding-right-1 govuk-!-font-weight-bold')]", SelectorType.XPATH);
        click("//span[contains(@class,'govuk-details__summary-text')]", SelectorType.XPATH);
        clickById("send");
    }

    public void replyToOperator() {
        waitAndClick("//*[@id='main']//td/a", SelectorType.XPATH);
        click("//*[contains(text(),'Send a reply')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='form-actions[reply]']", SelectorType.XPATH, Str.randomWord(10));
        clickById("send");
    }

    public void replyOperatorErrorMessage() {
        waitAndClick("//*[@id='main']//td/a", SelectorType.XPATH);
        click("//*[contains(text(),'Send a reply')]", SelectorType.XPATH);
        clickById("send");
    }

}

