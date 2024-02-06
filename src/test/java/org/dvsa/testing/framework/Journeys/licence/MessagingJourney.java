package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage.yesAndCabotagePermitConfirmation;


public class MessagingJourney extends BasePage {

    public MessagingJourney(World world) {
        this.world = world;
    }

    private static World world;


    public static void createConversation() {
        click("//*[contains(text(),'New Conversation')]", SelectorType.XPATH);
        selectRandomValueFromDropDown("//*[@id='subject']", SelectorType.XPATH);
        waitAndSelectValueFromDropDown("//*[@id='appOrLicNo']", SelectorType.XPATH, world.applicationDetails.getLicenceNumber());
        enterText("//*[@id='fields[messageContent]']", SelectorType.XPATH, yesAndCabotagePermitConfirmation());
        clickById("form-actions[submit]");
    }
}

