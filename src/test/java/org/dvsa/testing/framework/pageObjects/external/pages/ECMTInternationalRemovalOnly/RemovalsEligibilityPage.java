package org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class RemovalsEligibilityPage extends BasePermitPage {

    public static void confirmCheckbox() {
        if (notConfirmed()) {
            scrollAndClick("//input[@id='qaElement']", SelectorType.XPATH);
        }
    }

    private static boolean notConfirmed() {
        return !isElementPresent("//input[@id='qaElement']/ancestor::label[contains(@class, 'selected')]", SelectorType.XPATH);
    }

    public static String getAdvisoryText() {
        return getText("//p[contains(text(),'You may only use ECMT international removal permit')]", SelectorType.XPATH);
    }

    public static boolean isCheckboxAdvisoryTextPresent() {
        return isTextPresent("I confirm that I will only use an ECMT international removal permit to move household goods or business possessions and that I will use specialised equipment and staff for removal operations.");
    }

    public static boolean isErrorMessagePresent() {
        return isTextPresent("Tick to confirm you will only use your Removal permits to move household goods or business possessions");
    }

}
