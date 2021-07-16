package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.temporal.ChronoUnit;

public class CancellationPage extends BasePermitPage {

    protected final static String CONFIRM_CANCEL_CHECKBOX = "//input[@id='ConfirmCancel']";

    public static void untilOnPage() {
        untilUrlMatches("/permits/application/\\d+/cancel/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static void clickCancelCheckbox() {
        if (notConfirmed())
            scrollAndClick(CONFIRM_CANCEL_CHECKBOX, SelectorType.XPATH);
    }

    private static boolean notConfirmed() {
        return !isConfirmed();
    }

    public static boolean isConfirmed() {
        return findElement("input[type='checkbox']", SelectorType.CSS).isSelected();
    }

    public static void clickCancelButton() { // Not entirely sure of these 4 methods. Check pages before moving.
        scrollAndClick("#cancelbutton");
    }

    public static boolean isAdvisoryTextPresent() {
        return isElementPresent("//p[contains(text(),'By cancelling you understand that:')]", SelectorType.XPATH) &&
        isElementPresent("//li[contains(text(),'this application will be cancelled permanently')]", SelectorType.XPATH) &&
        isElementPresent("//li[contains(text(),'the information you have entered will be deleted')]", SelectorType.XPATH) &&
        isElementPresent("//li[contains(text(),'you will not be able to access it again and must make a new application in the future')]", SelectorType.XPATH);
    }

    public static String getConfirmCheckboxText() {
        return getText("//label[@class='form-control form-control--checkbox']", SelectorType.XPATH).trim();
    }

}
