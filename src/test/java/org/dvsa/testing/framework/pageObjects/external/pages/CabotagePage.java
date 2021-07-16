package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class CabotagePage extends BasePermitPage {

    private static String cabotageConfirmation = "//input[@id='qaElement']";

    public static void AnnualBilateralUntilOnPage() {
        untilElementIsPresent("//h1[contains(text(),'Do you need to carry out cabotage?')]", SelectorType.XPATH,10L, TimeUnit.SECONDS);
    }

    public static void ECMTRemovalsUntilOnPage() {
        untilUrlMatches("/permits/application/\\d+/removals-cabotage/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static boolean isCheckBoxTextPresent() {
        return isTextPresent("I confirm that I will not undertake cabotage journeys using an ECMT international removal permit.");
    }

    public static String getAdvisoryText() {
        return getText("//p[contains(text(),'Cabotage is the haulage and moving of goods for hi')]", SelectorType.XPATH);
    }

    public static void confirmWontUndertakeCabotage() {
        if (isNotConfirmed())
            scrollAndClick(cabotageConfirmation, SelectorType.XPATH);
    }

    private static boolean isNotConfirmed() {
        return !isElementPresent( cabotageConfirmation+ "/ancestor::label[contains(@class, 'selected')]", SelectorType.XPATH);
    }

    public static boolean hasReference() {
        return getReferenceFromPage().matches("\\w{2}\\d{7} / \\d+");
    }
}
