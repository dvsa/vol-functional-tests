package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.temporal.ChronoUnit;

public class CancellationConfirmationPage extends BasePermitPage {

    public static void untilOnPage() {
        untilUrlMatches("/permits/application/\\d+/cancel/confirmation/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static String getReferenceNumberHeading() {
        return getText("//div[@class='govuk-panel__body']/strong", SelectorType.XPATH);
    }

    public static String getAdvisoryHeadingPresent() {
        return getText(PAGE_SUBHEADING, SelectorType.XPATH);
    }

    public static String getAdvisoryTextPresent() {
        return getText("//p[contains(text(),'You have cancelled your application')]", SelectorType.XPATH);
    }
}
