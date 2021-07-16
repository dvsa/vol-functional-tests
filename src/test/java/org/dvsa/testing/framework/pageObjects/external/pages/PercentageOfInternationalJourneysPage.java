package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyProportion;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class PercentageOfInternationalJourneysPage extends BasePermitPage {

    final public static String RESOURCE = "st-percentage-international-journeys/";

    private static String PROPORTION_TEMPLATE = ".govuk-radios__item:nth-of-type(%d) input[type=radio]";

    public static void selectProportion(JourneyProportion proportion) {
        untilUrlMatches(RESOURCE, Duration.LONG, ChronoUnit.SECONDS);
        String selector = String.format(PROPORTION_TEMPLATE, proportion.ordinal() + 1);

        untilElementIsPresent(selector, SelectorType.CSS, BasePermitPage.WAIT_TIME_SECONDS, TimeUnit.SECONDS);
        scrollAndClick(selector);
        saveAndContinue();
    }

    public static boolean isIntensityMessagePresent() {
        return isTextPresent("You have stated a high intensity of use of these permits. Check the details are correct. We may contact you to verify this information.");
    }

}