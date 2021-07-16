package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyProportion;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class ProportionOfInternationalJourneyPage extends BasePermitPage {

    private static String PROPORTION_TEMPLATE = ".govuk-radios__item:nth-of-type(%d) input[type=radio]";

    final public static String RESOURCE = "st-percentage-international-journeys/";

    public static void chooseDesiredProportion(JourneyProportion proportion) {
        untilOnPage();
        String selector = String.format(PROPORTION_TEMPLATE, proportion.ordinal() + 1);
        untilElementIsPresent(selector, SelectorType.CSS, BasePermitPage.WAIT_TIME_SECONDS, TimeUnit.SECONDS);
        scrollAndClick(String.format(PROPORTION_TEMPLATE, proportion.ordinal() + 1));
        saveAndContinue();
    }

    private static void untilOnPage() {
        untilUrlMatches(RESOURCE, Duration.LONG, ChronoUnit.SECONDS);
    }

    public static String getIntensityMessage() {
        return getText("//strong[@class='govuk-warning-text__text']", SelectorType.XPATH);
    }

}
