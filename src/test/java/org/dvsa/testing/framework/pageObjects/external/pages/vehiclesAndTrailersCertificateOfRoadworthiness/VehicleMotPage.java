package org.dvsa.testing.framework.pageObjects.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness;

import activesupport.dates.Dates;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BaseCertificateOfRoadWorthiness;
import org.joda.time.LocalDate;

import java.util.LinkedHashMap;


public class VehicleMotPage extends BaseCertificateOfRoadWorthiness {

    static Dates date = new Dates(LocalDate::new);
    static LinkedHashMap<String, String> monthsAheadDate = date.getDateHashMap(0, 6, 0);

    public static void enterMOTDate() {
        String DAY = "#qaDateSelect_day";
        String MONTH = "#qaDateSelect_month";
        String YEAR = "#qaDateSelect_year";
        waitAndEnterText(DAY, SelectorType.CSS, monthsAheadDate.get("day"));
        waitAndEnterText(MONTH, SelectorType.CSS, monthsAheadDate.get("month"));
        waitAndEnterText(YEAR, SelectorType.CSS, monthsAheadDate.get("year"));
    }
}