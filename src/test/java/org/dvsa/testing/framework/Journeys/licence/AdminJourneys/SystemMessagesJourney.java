package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;


import org.dvsa.testing.framework.Injectors.World;
import activesupport.dates.Dates;
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
            waitAndClick("(//input[@type='radio'])[2]", SelectorType.XPATH);
            waitAndEnterText("description", SelectorType.ID, world.DataGenerator.getRandomWord());
        }

    public void inputCurrentTimeDate() {
        HashMap<String, String> StartDate = new Dates(LocalDate::new).getDateHashMap(0, 0, 0);
        enterDateFieldsByPartialId("details[startDate]", StartDate);
        selectValueFromDropDown("details[startDate]_hour", SelectorType.ID, "00");
        selectValueFromDropDown("details[startDate]_minute", SelectorType.ID, "00");
        HashMap<String, String> EndDate = new Dates(LocalDate::new).getDateHashMap(1, 0, 0);
        enterDateFieldsByPartialId("details[endDate]", EndDate);
        selectValueFromDropDown("details[endDate]_hour", SelectorType.ID, "00");
        selectValueFromDropDown("details[endDate]_minute", SelectorType.ID, "00");
        world.UIJourney.clickSubmit();
        world.UIJourney.closeAlert();
    }
}

