package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;

import Injectors.World;
import activesupport.dates.Dates;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.HashMap;

public class PublicHolidayJourney extends BasePage {
    private World world;
    Dates date = new Dates(org.joda.time.LocalDate::new);

    public PublicHolidayJourney (World world) {this.world = world;}

    public void addPublicHoliday() {
        waitAndClick("add", SelectorType.ID);
        waitForElementToBeClickable("//input[@type='checkbox']", SelectorType.XPATH);
        selectRandomCheckBoxOrRadioBtn("checkbox");
        HashMap<String, String> currentDate = date.getDateHashMap(0, 0, +1);
        enterDateFieldsByPartialId("fields[holidayDate]", currentDate);
        world.UIJourney.clickSubmit();
    }

    public void editPublicHoliday() {
        waitAndClick("(//input[@type='submit'])[2]", SelectorType.XPATH);
        waitForElementToBeClickable("//input[@type='checkbox']", SelectorType.XPATH);
        selectRandomCheckBoxOrRadioBtn("checkbox");
        HashMap<String, String> currentDate = date.getDateHashMap(+0, +0, +1);
        enterDateFieldsByPartialId("fields[holidayDate]", currentDate);
        world.UIJourney.clickSubmit();
    }

    public void deletePublicHoliday() {
        waitAndClick("//input[@value='Remove']", SelectorType.XPATH);
        waitAndClick("form-actions[confirm]", SelectorType.ID);
        waitForElementToBeClickable("//p[text()='The public holiday is removed']", SelectorType.XPATH);
    }
}
