package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class NumberOfTripsPage extends BasePermitPage {

    private static String TRIPS_ABROAD = "//input[@type ='text']";

    public static void untilOnPage() {
        untilUrlMatches("permits/application/\\d+/no-of-permits/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static void quantity(@NotNull String quantity){
        untilElementIsPresent(TRIPS_ABROAD, SelectorType.XPATH,10000, TimeUnit.SECONDS);
        scrollAndEnterField(TRIPS_ABROAD, SelectorType.XPATH, quantity);
    }

    public static void enterNumberOfTripsValue() {
        scrollAndEnterField("//input[@type ='text']", SelectorType.XPATH, "10");
    }
}
