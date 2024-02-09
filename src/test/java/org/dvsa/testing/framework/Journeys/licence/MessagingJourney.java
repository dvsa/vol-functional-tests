package org.dvsa.testing.framework.Journeys.licence;

import activesupport.string.Str;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage.yesAndCabotagePermitConfirmation;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MessagingJourney extends BasePage {

    public MessagingJourney(World world) {
        this.world = world;
    }

    private static World world;


    public static void createConversation() {
        click("//*[contains(text(),'New Conversation')]", SelectorType.XPATH);
        selectRandomValueFromDropDown("//*[@id='subject']", SelectorType.XPATH);
        waitAndSelectValueFromDropDown("//*[@id='appOrLicNo']", SelectorType.XPATH, world.applicationDetails.getLicenceNumber());
        waitAndEnterText("//*[@id='fields[messageContent]']", SelectorType.XPATH, Str.randomWord(10));
        clickById("form-actions[submit]");
    }

    public static void replyForMessage() {
        if (getText("//*/strong[contains(@class,'govuk-tag govuk-tag--red')]", SelectorType.XPATH).equals("NEW MESSAGE"));
        waitAndClick("//*[contains(@class,'govuk-body govuk-link govuk-!-padding-right-1 govuk-!-font-weight-bold')]", SelectorType.XPATH);
        click("//span[contains(@class,'govuk-details__summary-text')]", SelectorType.XPATH);
        click("//*[@id='form-actions[reply]']", SelectorType.XPATH);
        waitAndEnterText("//*[@id='form-actions[reply]']", SelectorType.XPATH, Str.randomWord(10));
        clickById("send");
    }

    public static void archiveTheConversation() {
        clickById("close");
        assert (isElementPresent("//*[contains(@class,'field')]", SelectorType.XPATH));
        clickById("close");
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag--grey')]", SelectorType.XPATH), "CLOSED");
    }

    public static void createNewConversation(){
        clickByLinkText("Start a new conversation");
        selectRandomValueFromDropDown("//*[@id='form-actions[messageSubject]']", SelectorType.XPATH);
        click("//*[@id='form-actions[appOrLicNo]']", SelectorType.XPATH);
        click("//*[@id='form-actions[appOrLicNo]']/optgroup[1]/option ", SelectorType.XPATH);
        waitAndEnterText("//*[@id='form-actions[messageContent]']", SelectorType.XPATH, Str.randomWord(10));
        clickById("send");
    }

}

