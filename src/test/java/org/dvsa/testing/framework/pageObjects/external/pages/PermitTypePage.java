package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class PermitTypePage extends BasePermitPage {

    public static void selectType(PermitType type) {
        String selector = String.format("//label[contains(text(), '%s')]/../input[@type='radio']", type.toString());
        scrollAndClick(selector, SelectorType.XPATH);
    }

    public static void clickContinue() {
        scrollAndClick("//input[@value = 'Continue']", SelectorType.XPATH);
    }

    public static void clickCancel() {
        scrollAndClick("//a[text()='Cancel']", SelectorType.XPATH);
    }
}
