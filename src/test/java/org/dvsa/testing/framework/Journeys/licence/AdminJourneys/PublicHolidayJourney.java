package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.dates.Dates;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class PublicHolidayJourney extends BasePage {
    private World world;
    Dates date = new Dates(org.joda.time.LocalDate::new);

    public PublicHolidayJourney (World world) {this.world = world;}

    public void addPublicHoliday() {
        waitAndClick("add", SelectorType.ID);
        waitForElementToBeClickable("//input[@type='checkbox']", SelectorType.XPATH);
        selectRandomCheckBoxOrRadioBtn("checkbox");

        LocalDate startDate = LocalDate.now().plusYears(1);
        LocalDate endDate = LocalDate.now().plusYears(3);
        long days = startDate.until(endDate).getDays();
        LocalDate randomDate = startDate.plusDays(new Random().nextInt((int) days + 1));
        String formattedDate = randomDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String[] dateParts = formattedDate.split("/");
        LinkedHashMap<String, String> dateFields = new LinkedHashMap<>();
        dateFields.put("day", dateParts[0]);
        dateFields.put("month", dateParts[1]);
        dateFields.put("year", dateParts[2]);

        enterDateFieldsByPartialId("fields[holidayDate]", dateFields);
        UniversalActions.clickSubmit();
        UniversalActions.closeAlert();
    }

    public void editPublicHoliday() {
        waitAndClick("button.action-button-link:first-of-type", SelectorType.CSS);
        waitForElementToBeClickable("//input[@type='checkbox']", SelectorType.XPATH);
        HashMap<String, String> currentDate = date.getDateHashMap(+0, +7, +2);
        enterDateFieldsByPartialId("fields[holidayDate]", currentDate);
        UniversalActions.clickSubmit();
        UniversalActions.closeAlert();
    }

    public void deletePublicHoliday() {
        waitAndClick("//*[contains(text(),'Remove')]",SelectorType.XPATH);
        UniversalActions.clickConfirm();
        waitForElementToBeClickable("//p[text()='The public holiday is removed']", SelectorType.XPATH);
    }
}
