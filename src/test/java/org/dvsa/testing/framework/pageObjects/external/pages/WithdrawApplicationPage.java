package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.temporal.ChronoUnit;

public class WithdrawApplicationPage extends BasePermitPage {

    public static void untilOnPage() {
        untilUrlMatches("/permits/application/\\d+/withdraw/", 12L, ChronoUnit.SECONDS);
    }

    public static void clickWithdraw() {
        scrollAndClick("//input[@id='withdrawbutton']", SelectorType.XPATH);
    }
}
