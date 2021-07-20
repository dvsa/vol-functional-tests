package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class CabotagePage extends BasePermitPage {

    private static String cabotageConfirmation = "//input[@id='qaElement']";

    public static void confirmWontUndertakeCabotage() {
        if (isNotConfirmed())
            scrollAndClick(cabotageConfirmation, SelectorType.XPATH);
    }

    private static boolean isNotConfirmed() {
        return !isElementPresent( cabotageConfirmation+ "/ancestor::label[contains(@class, 'selected')]", SelectorType.XPATH);
    }
}
