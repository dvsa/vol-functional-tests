package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class WithdrawApplicationPage extends BasePermitPage {

    public static void clickWithdraw() {
        scrollAndClick("//button[@id='withdrawbutton']", SelectorType.XPATH);
    }
}
