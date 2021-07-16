package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.concurrent.TimeUnit;

public class AnnualTripsAbroadPage extends BasePermitPage {

    private static String annualTrips = "//input[@type='text']";

    public static void helpCalculatingInternationalTrips() {
        waitAndClick("//span[@class='govuk-details__summary-text']", SelectorType.XPATH);
    }

    public static String getFirstGuidanceText() {
        return getText("//div[@class='guidance-blue']", SelectorType.XPATH);
    }

    public static String getSecondGuidanceText() {
        return getText("//span[contains(text(),'Help calculating your international trips')]", SelectorType.XPATH);
    }

    public static boolean isSummaryTextPresent() {
        return isTextPresent("Help calculating your international trips") &&
                isTextPresent("Start and finish inside the UK and go to or pass through any EU country and or Iceland, Liechtenstein plus Norway") &&
                isTextPresent("For example: Your truck leaves the UK, drives through France, Germany and Poland to reach Ukraine and returns\n" + "via Hungary, Austria, Switzerland and France. This is counted as one international journey.") &&
                isTextPresent("Start and finish outside of the UK but inside any EU country, Iceland, Liechtenstein or Norway") &&
                isTextPresent("For example: Your truck leaves the UK, drives through France, Belgium and Switzerland to reach Liechtenstein and returns\n" + "via Austria, Germany, Switzerland, Italy and France. This is also counted as one international journey.") &&
                isTextPresent("Start and finish outside any of the countries listed above but your journey passes through any of these countries") &&
                isTextPresent("For example: Your truck drives to Ireland to pick up goods, drives through the UK and France to reach Spain\n" + "and returns through France to the UK. This is counted as one international journey.");
    }

    public static void quantity(String quantity) {
        quantity(Integer.parseInt(quantity));
    }

    public static void quantity(int quantity) {
        untilElementIsPresent(annualTrips, SelectorType.XPATH, 60L, TimeUnit.SECONDS);
        scrollAndEnterField(annualTrips, SelectorType.XPATH, String.valueOf(quantity));
    }

    public static void clickReturnToOverview() {
        BasePermitPage.clickReturnToOverview();
        if (isElementPresent("//strong[@class='govuk-warning-text__text']", SelectorType.XPATH)) {
            BasePermitPage.clickReturnToOverview();
        }
    }
}