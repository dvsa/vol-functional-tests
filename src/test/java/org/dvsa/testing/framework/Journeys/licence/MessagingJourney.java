package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import activesupport.string.Str;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;


public class MessagingJourney extends BasePage {

    private final World world;

    public MessagingJourney(World world) {
        this.world = world;
    }

    String randomWord = Str.randomWord(10);

    public String getRandomWord() {
        return randomWord;
    }

    public void setRandomWord(String randomWord) {
        this.randomWord = randomWord;
    }

    public void messageHeading() {
        assertTrue(isTextPresent("Messages"));
    }

    public void sendMessage(){
    clickById("send");
    }

    public void internalMessagePageDisplay() {
        assertTrue(isTextPresent("Conversations"));
        assertTrue(isElementPresent("(//span[text()='Messages'])[2]", SelectorType.XPATH));
        assertTrue(isElementPresent("New Conversation", SelectorType.LINKTEXT));
        assertTrue(isElementPresent("Disable Messaging", SelectorType.LINKTEXT));
    }

    public void createConversation() {
        waitAndClick("//*[contains(text(),'New Conversation')]", SelectorType.XPATH);
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
    }

    public void archiveTheConversation() {
        waitAndClick("//button[@id='close']", SelectorType.XPATH);
        waitForTextToBePresent("End Conversation");
        click("//button[@id='close']", SelectorType.XPATH);
        String actualText = getText("//*[@class='govuk-tag govuk-tag--grey']", SelectorType.XPATH);
        assertTrue(actualText.equalsIgnoreCase("CLOSED"));
    }

    public void createNewConversationAndSelectTheLicenceNumber() {
        waitAndClickByLinkText("Start a new conversation");
        selectValueFromDropDownByIndex("//*[@id='form-actions[inputs][messageSubject]']", SelectorType.XPATH,1);
        click("//*[@id='form-actions[inputs][appOrLicNo]']", SelectorType.XPATH);

        if (world.configuration.env.equals(EnvironmentType.PREPRODUCTION)) {
            selectValueFromDropDown("//*[@id='form-actions[inputs][appOrLicNo]']", SelectorType.XPATH, "OB1057273");
        } else {
            selectValueFromDropDown("//*[@id='form-actions[inputs][appOrLicNo]']", SelectorType.XPATH, world.applicationDetails.getLicenceNumber());
        }
        waitAndEnterText("//*[@id='form-actions[inputs][messageContent]']", SelectorType.XPATH, randomWord);
        clickById("send");
    }

    public void checkForNewTask() {
        waitAndClickByLinkText("Processing");
        assertTrue(isElementPresent("New message", SelectorType.LINKTEXT));
    }

    public void viewNewMessage() {
        String actualText = getText("//*[@class='govuk-tag govuk-tag--red']", SelectorType.XPATH);
        assertTrue(actualText.equalsIgnoreCase("NEW MESSAGE"));
        click("//*[@id='main-content']//tbody/tr[1]/td[1]/a", SelectorType.XPATH);
    }

    public void openMessageStatusCheck() {
        waitAndClickByLinkText("Messages");
        refreshPage();
        String actualText = getText("//*[@class='govuk-tag govuk-tag--blue']", SelectorType.XPATH);
        assertTrue(actualText.equalsIgnoreCase("OPEN"));
    }

    public void notificationCount() {
        assertEquals(getText("//*[contains(@class,'notification-count__number')]", SelectorType.XPATH), "1");
        waitAndClickByLinkText("Messages");
        String actualText = getText("//*[@class='govuk-tag govuk-tag--red']", SelectorType.XPATH);
        assertTrue(actualText.equalsIgnoreCase("NEW MESSAGE"));
    }

    public void backToConversation() {
        waitAndClickByLinkText("Back to conversations");
    }


    public void messageTabHidden() {
        assertFalse(HomePage.isTabPresent(Tab.MESSAGES));
    }

    public void messageTabShown() {
        assertTrue(HomePage.isTabPresent(Tab.MESSAGES));
    }

    public void disableMessaging()  {
        waitAndClickByLinkText("Messages");
        waitAndClickByLinkText("Disable Messaging");
        waitAndClick("close", SelectorType.ID);
        waitForTextToBePresent("Messaging will be disabled for this operator");
        click("close", SelectorType.ID);
        waitForTextToBePresent("Messaging has been disabled for this operator");
    }

    public void submitMessageWithoutSelectingAnyOption() {
        waitAndClickByLinkText("Start a new conversation");
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
    }

    public void replyToOperator() {
        waitAndClick("//*[@id='main']//td/a", SelectorType.XPATH);
        click("//*[contains(text(),'Send a reply')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='form-actions[reply]']", SelectorType.XPATH, Str.randomWord(10));
    }

    public void replyOperatorErrorMessage() {
        waitAndClick("//*[@id='main']//td/a", SelectorType.XPATH);
        click("//*[contains(text(),'Send a reply')]", SelectorType.XPATH);
    }

}