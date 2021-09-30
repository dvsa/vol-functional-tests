package org.dvsa.testing.framework.pageObjects.internal;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.joda.time.LocalDate;

public class InterimPage extends BasePage{

    //Selectors
    private static String INTERIM_REASON_FIELD = nameAttribute("textarea","data[interimReason]");
    private static String START_DATE_FIELDS = nameAttribute("input", "data[interimStart][%s]");
    private static String END_DATE_FIELDS = nameAttribute("input", "data[interimEnd][%s]");
    private static String VEHICLE_FIELD = nameAttribute("input", "data[interimAuthHgvVehicles]");
    private static String TRAILERS_FIELD = nameAttribute("input", "data[interimAuthTrailers]");
    private static String SAVE = nameAttribute("button", "form-actions[save]");
    private static String GRANT = nameAttribute("button", "form-actions[grant]");

    public static void enterInterimDetail(String interimDetails) {
          enterText(INTERIM_REASON_FIELD, SelectorType.CSS, interimDetails);
    }

    public static void startDate(int day, int month, int year) {
        enterText(String.format(START_DATE_FIELDS, "day"), SelectorType.CSS, String.valueOf(day));
        enterText(String.format(START_DATE_FIELDS, "month"), SelectorType.CSS, String.valueOf(month));
        enterText(String.format(START_DATE_FIELDS, "year"), SelectorType.CSS, String.valueOf(year));
    }

    public static void endDate(int day, int month, int year) {
        enterText(String.format(END_DATE_FIELDS, "day"), SelectorType.CSS, String.valueOf(day));
        enterText(String.format(END_DATE_FIELDS, "month"), SelectorType.CSS, String.valueOf(month));
        enterText(String.format(END_DATE_FIELDS, "year"), SelectorType.CSS, String.valueOf(year));
    }

    public static void vehicleAuthority(int vehicles) {
        enterText(VEHICLE_FIELD, SelectorType.CSS, String.valueOf(vehicles));
    }

    public static void trailerAuthority(int trailer) {
        enterText(TRAILERS_FIELD, SelectorType.CSS, String.valueOf(trailer));
    }

    public static void save() {
        click(SAVE, SelectorType.CSS);
    }

    public static void grant() {
        click(GRANT, SelectorType.CSS);
    }

    public static void addInterimValues() {
        clickByLinkText("add interim");
        findSelectAllRadioButtonsByValue("Y");
        InterimPage.enterInterimDetail("Test Test");
        InterimPage.startDate(LocalDate.now().getDayOfWeek(), LocalDate.now().getMonthOfYear(), LocalDate.now().getYear());
        InterimPage.endDate(LocalDate.now().plusDays(7).getDayOfWeek(), LocalDate.now().plusMonths(2).getMonthOfYear(), LocalDate.now().getYear());
    }
}
