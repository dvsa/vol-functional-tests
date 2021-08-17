package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.time.temporal.ChronoUnit;

public class ReceiptPage extends BasePage {

    public static void untilOnPage() {
        untilUrlMatches("/permits/\\d+/print-receipt/", Duration.LONG, ChronoUnit.SECONDS);
    }
}