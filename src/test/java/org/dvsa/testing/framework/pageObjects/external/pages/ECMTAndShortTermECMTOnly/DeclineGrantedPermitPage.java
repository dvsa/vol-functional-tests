package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.temporal.ChronoUnit;

public class DeclineGrantedPermitPage extends BasePermitPage {

    private static String declineConfirmation = "//input[@id='DeclineApplication']";

    public static void untilOnPage() {
        untilUrlMatches("/permits/application/\\d+/decline/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static boolean isAdvisoryTextPresent() {
        return isTextPresent("By declining these permits, you agree that:")
        && isTextPresent("granted permits will be cancelled permanently")
        && isTextPresent("the information you have entered will be deleted")
        && isTextPresent("you will not be able to access the application again")
        && isTextPresent("you will not receive a refund for the application fee paid")
        && isTextPresent("I confirm that I am declining these permits.");
    }

    public static void confirmDeclineOfPermits() {
        if (notConfirmed()) {
            scrollAndClick(declineConfirmation, SelectorType.XPATH);
        }
    }

    private static boolean notConfirmed() {
        return !isElementPresent(declineConfirmation + "/ancestor::label[contains(@class, 'selected')]", SelectorType.XPATH);
    }

    public static boolean isErrorTextPresent() {
        return isTextPresent("You must select the checkbox to continue");
    }
}