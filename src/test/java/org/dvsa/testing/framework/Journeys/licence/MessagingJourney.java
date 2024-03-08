package org.dvsa.testing.framework.Journeys.licence;

import activesupport.string.Str;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

import static org.junit.jupiter.api.Assertions.*;


public class MessagingJourney extends BasePage {

    public MessagingJourney(World world) {
        this.world = world;
    }

    private static World world;


    public static void messageHeading() {
        assertTrue(isTextPresent("Messages"));
    }


    public static void internalMessagePageDisplay() {
        assertTrue(isTextPresent("Conversations"));
        assertTrue(isElementPresent("(//span[text()='Messages'])[2]", SelectorType.XPATH));
        assertTrue(isElementPresent("New Conversation", SelectorType.LINKTEXT));
        assertTrue(isElementPresent("Disable Messaging", SelectorType.LINKTEXT));
    }

    public static void createConversation() {
        click("//*[contains(text(),'New Conversation')]", SelectorType.XPATH);
        selectRandomValueFromDropDown("//*[@id='subject']", SelectorType.XPATH);
        waitAndSelectValueFromDropDown("//*[@id='appOrLicNo']", SelectorType.XPATH, world.applicationDetails.getLicenceNumber());
        waitAndEnterText("//*[@id='fields[messageContent]']", SelectorType.XPATH, Str.randomWord(10));
        clickById("form-actions[submit]");
    }

    public static void replyForMessage() {
        if (getText("//*/strong[contains(@class,'govuk-tag govuk-tag--red')]", SelectorType.XPATH).equals("NEW MESSAGE"))
            ;
        waitAndClick("//*[contains(@class,'govuk-body govuk-link govuk-!-padding-right-1 govuk-!-font-weight-bold')]", SelectorType.XPATH);
        click("//span[contains(@class,'govuk-details__summary-text')]", SelectorType.XPATH);
        click("//*[@id='form-actions[reply]']", SelectorType.XPATH);
        waitAndEnterText("//*[@id='form-actions[reply]']", SelectorType.XPATH, Str.randomWord(10));
        clickById("send");
    }

    public static void archiveTheConversation() {
        clickById("close");
        assert (isElementPresent("//*[contains(@class,'field')]", SelectorType.XPATH));
        click("//*[@id='close']", SelectorType.XPATH);
        waitForTextToBePresent("CLOSE");
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag--grey')]", SelectorType.XPATH), "CLOSED");
    }

    public static void createNewConversation() {
        clickByLinkText("Start a new conversation");
        selectRandomValueFromDropDown("//*[@id='form-actions[messageSubject]']", SelectorType.XPATH);
        click("//*[@id='form-actions[appOrLicNo]']", SelectorType.XPATH);
        click("//*[@id='form-actions[appOrLicNo]']/optgroup[1]/option ", SelectorType.XPATH);
        waitAndEnterText("//*[@id='form-actions[messageContent]']", SelectorType.XPATH, Str.randomWord(10));
        clickById("send");
    }

    public static void selectMessageCheckBox() {
        selectValueFromDropDown("//*[@id='assignedToTeam']", SelectorType.XPATH, "Leeds Licensing Goods");
        clickById("date");
        selectValueFromDropDown("//*[@id='status']", SelectorType.XPATH, "Open");
        click("//*[@id='messaging']", SelectorType.XPATH);
    }

    public static void checkForNewTask() {
        if (isTextPresent("results per page")) {
            waitAndClick("//*[@id='main']//div[3]/nav[1]/ul/li[3]/a", SelectorType.XPATH);
            selectMessageCheckBox();
        } else if (isTextPresent(world.applicationDetails.getLicenceNumber())) ;
        assertTrue(isTextPresent(world.applicationDetails.getLicenceNumber()));
    }

    public static void viewNewMessage() {
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag--red')]", SelectorType.XPATH), "NEW MESSAGE");
        click("//*[@id='main-content']//tbody/tr[1]/td[1]/a", SelectorType.XPATH);
    }

    public static void openMessageStatusCheck() {
        clickByLinkText("Messages");
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag--blue')]", SelectorType.XPATH), "OPEN");
    }

    public static void notificationCount(){
        assertEquals(getText("//*[contains(@class,'notification-count__number')]", SelectorType.XPATH), "1");
        clickByLinkText("Messages");
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag--red')]", SelectorType.XPATH), "NEW MESSAGE");
    }

    public static void backToConversation(){
        click("//*[contains(@class,'govuk-back-link')]", SelectorType.XPATH);
        click("//*[contains(@class,'govuk-body govuk-link govuk-!-padding-right-1 ')]", SelectorType.XPATH);
        click("//*[contains(@class,'govuk-back-link')]", SelectorType.XPATH);
    }


    public static void messageTabHidden() {
        assertFalse(HomePage.isTabPresent(Tab.MESSAGES));
    }

    public static void messageTabShown() {
        assertTrue(HomePage.isTabPresent(Tab.MESSAGES));
    }

    public static void disableMessaging() {
        clickByLinkText("Messages");
        clickByLinkText("Disable Messaging");
        waitAndClick("close",SelectorType.ID);
        waitForTextToBePresent("Messaging will be disabled for this operator");
        click("close",SelectorType.ID);
        waitForTextToBePresent("Messaging has been disabled for this operator");
    }
}

