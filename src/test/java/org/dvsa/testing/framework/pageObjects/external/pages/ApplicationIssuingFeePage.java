package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class ApplicationIssuingFeePage extends BasePage {

    public static void  acceptAndPay() {
        scrollAndClick("//*[contains(text(), 'Accept and pay')]", SelectorType.XPATH);
    }

    public static void declinePermits() {
        scrollAndClick("//*[contains(text(), 'Decline permits')]", SelectorType.XPATH);
    }

    public static void cancelAndReturnToDashboard() {
        scrollAndClick("//*[contains(text(), 'Cancel and return to dashboard')]", SelectorType.XPATH);
    }
}
