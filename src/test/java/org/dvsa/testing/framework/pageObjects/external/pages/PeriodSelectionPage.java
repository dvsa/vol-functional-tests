package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.concurrent.TimeUnit;

public class PeriodSelectionPage extends BasePermitPage {

    public static String PAGE_TITLE = "Validity period";
    public static String MOROCCO_PAGE_TITLE = "Which permit do you need?";

    public static void untilOnPage() {
        untilElementIsPresent(String.format("//h1[contains(text(),'%s')]", PAGE_TITLE), SelectorType.XPATH, 10L, TimeUnit.SECONDS);
    }

    public static void chooseBilateralPeriodType(PeriodType shortTermType) {
        String selector = String.format("//label[contains(text(), '%s')]/../input[@type='radio']", shortTermType.toString());
        if (isElementPresent("//input[@id='stock']", SelectorType.XPATH)) {
            scrollAndClick(selector, SelectorType.XPATH);
        }
        saveAndContinue();
    }

    public static void selectFirstAvailablePermitPeriod() {
        String selector = String.format("//input[@id='stock']");
        scrollAndClick(selector, SelectorType.XPATH);
    }

    public static void selectShortTermType(PeriodType shortTermType) {
        String selector = String.format("//label[contains(text(), '%s')]/../input[@type='radio']", shortTermType.toString());
        scrollAndClick(selector, SelectorType.XPATH);
    }
}
