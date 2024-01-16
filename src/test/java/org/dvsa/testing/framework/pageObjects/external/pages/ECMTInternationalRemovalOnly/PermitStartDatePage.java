package org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly;

import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.LinkedHashMap;

public class PermitStartDatePage extends BasePermitPage {

    public static Dates date = new Dates(new LocalDateCalendar());
    static LinkedHashMap<String, String> currentDate = date.getDateHashMap(0, 0, 0);
    static LinkedHashMap<String, String> monthsAheadDate = date.getDateHashMap(0, 6, 0);

    public static void permitDate() {
        enterDay(currentDate);
        enterMonth(currentDate);
        enterYear(currentDate);
    }

    public static void leaveDateBlank() {
        enterMonth(currentDate);
        enterYear(currentDate);
    }

    public static void inValidDate() {
        LinkedHashMap<String, String> invalidDate = new LinkedHashMap<>();
        invalidDate.put("day", "33");
        invalidDate.put("month", "13");
        enterDay(invalidDate);
        enterMonth(invalidDate);
        enterYear(currentDate);
    }

    public static void dayAhead() {
        enterDay(currentDate);
        enterMonth(monthsAheadDate);
        enterYear(currentDate);
    }

    public static void enterDay(LinkedHashMap<String, String> date) {
        findElement("//input[contains(@id,'_day')]",SelectorType.XPATH).clear();
        waitAndEnterText("//input[contains(@id,'_day')]",SelectorType.XPATH, date.get("day"));
    }

    public static void enterMonth(LinkedHashMap<String, String> date) {
        findElement("//input[contains(@id,'_month')]",SelectorType.XPATH).clear();
        waitAndEnterText("//input[contains(@id,'_month')]",SelectorType.XPATH, date.get("month"));
    }
    public static void enterYear(LinkedHashMap<String, String> date) {
        findElement("//input[contains(@id,'_year')]",SelectorType.XPATH).clear();
        waitAndEnterText("//input[contains(@id,'_year')]",SelectorType.XPATH, date.get("year"));
    }

    public static boolean checkDaysAheadErrorMessagePresent() {
        return isElementPresent(INLINE_ERROR_TEXT, SelectorType.XPATH);
    }

    public static boolean checkAdvisoryTextPresent() {
        return isTextPresent("Choose any date up to 60 days ahead.");
    }

    public static boolean checkCertificateAdvisoryTextPresent() {
        return isTextPresent("You must carry your vehicle and trailer certificates at all times.");
    }

}



