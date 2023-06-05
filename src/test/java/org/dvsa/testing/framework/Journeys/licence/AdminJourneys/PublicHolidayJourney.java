package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;

import org.dvsa.testing.framework.Injectors.World;
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
        HashMap<String, String> currentDate = date.getDateHashMap(0, 0, +2);
        enterDateFieldsByPartialId("fields[holidayDate]", currentDate);
        world.UIJourney.clickSubmit();
        world.UIJourney.closeAlert();
    }

    public void editPublicHoliday() {
        waitAndClick("button.action-button-link:first-of-type", SelectorType.CSS);
        waitForElementToBeClickable("//input[@type='checkbox']", SelectorType.XPATH);
        HashMap<String, String> currentDate = date.getDateHashMap(+0, +7, +2);
        enterDateFieldsByPartialId("fields[holidayDate]", currentDate);
        world.UIJourney.clickSubmit();
        world.UIJourney.closeAlert();
    }

    public void deletePublicHoliday() {
        waitAndClick("//input[@value='Remove']", SelectorType.XPATH);
        world.UIJourney.clickConfirm();
        waitForElementToBeClickable("//p[text()='The public holiday is removed']", SelectorType.XPATH);
    }
}
