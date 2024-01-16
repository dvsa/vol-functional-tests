package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class SubmittedPage extends BasePermitPage {

    private static final String RECEIPT = "//a[text() = 'your receipt (opens in new tab)']";

    public static void untilOnPage() {
        untilUrlMatches("permits/application/\\d+/submitted/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static void untilPageLoad() {
        untilElementIsPresent(PAGE_SUBHEADING, SelectorType.XPATH, 10, TimeUnit.SECONDS);
    }

    public static String getSubHeading() {
        return getText(PAGE_SUBHEADING, SelectorType.XPATH);
    }

    public static boolean isBilateralAdvisoryTextPresent() {
        return isTextPresent("You will receive a confirmation email that we have received your application.") &&
        isTextPresent("Your permits will be grouped together under the same licence number that you applied with.") &&
        isTextPresent("We will now post your Standard permits within the next 3 working days. Cabotage permits may arrive later.");
    }

    public static boolean hasViewReceipt() {
        return isElementPresent(RECEIPT, SelectorType.XPATH);
    }

    public static void openReceipt() {
        waitAndClick(RECEIPT, SelectorType.XPATH);
    }

    public static void goToPermitsDashboard(){
        waitAndClick("//a[contains(text(),'Go to permits dashboard')]", SelectorType.XPATH);
    }

    public static boolean isCertificateAdvisoryTextPresent() {
        return isTextPresent("We will now post your certificate within the next 10 working days.");
    }

    public static boolean isWarningMessagePresent() {
        return isElementPresent("//strong[@class='govuk-warning-text__text']", SelectorType.XPATH);
    }
}
