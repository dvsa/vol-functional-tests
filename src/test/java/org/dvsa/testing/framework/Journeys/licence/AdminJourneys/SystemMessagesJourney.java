package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;


import Injectors.World;
import activesupport.dates.Dates;
import org.dvsa.testing.framework.Utils.Generic.DataGenerator;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.joda.time.LocalDate;

import java.util.HashMap;

public class SystemMessagesJourney extends BasePage {
    private final World world;
    public SystemMessagesJourney(World world) {
        this.world = world;
    }

        public void addExternalMessage () {
            waitAndClick("//button[@name='action']", SelectorType.XPATH);
            waitForTextToBePresent("Add system message");
            findSelectAllRadioButtonsByValue("Self Serve");
            waitAndEnterText("description", SelectorType.ID, DataGenerator.generateRandomWords(12));
        }

    public void inputCurrentTimeDate() {
            HashMap<String, String> StartDate = new Dates(LocalDate::new).getDateHashMap(0,0,0);
            enterDateFieldsByPartialId("details[startDate]", StartDate);
            selectValueFromDropDown("details[startDate]_hour", SelectorType.XPATH, "00");
            selectValueFromDropDown("details[startDate]_minute", SelectorType.XPATH, "00");
            HashMap<String, String> EndDate = new Dates(LocalDate::new).getDateHashMap(1,0,0);
            enterDateFieldsByPartialId("details[endDate]", EndDate);
            selectValueFromDropDown("details[endDate]_hour", SelectorType.XPATH, "00");
            selectValueFromDropDown("details[endDate]_minute", SelectorType.XPATH, "00");
            world.UIJourney.clickSubmit();}

    public void checkForDisplayedMessageOnExternal() {

    }
}

