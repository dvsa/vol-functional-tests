package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.concurrent.TimeUnit;

public class TurkeyThirdCountryPage extends BasePermitPage {

    public static void untilOnPage() {
        untilElementIsPresent("//h1[contains(text(),'Are you transporting goods from Turkey to a third-')]", SelectorType.XPATH, 10L, TimeUnit.SECONDS);
    }

    public static boolean areCorrectRadioButtonsPresent() {
        return isElementPresent("//input[@type='radio']", SelectorType.XPATH) &&
        isElementPresent("//label[contains(text(),'Yes')]", SelectorType.XPATH) &&
        isElementPresent("//label[contains(text(),'No')]", SelectorType.XPATH);
    }

    public static String getErrorText() {
        return getText("//a[contains(text(),'Please select one option')]", SelectorType.XPATH);
    }

    public static void clickYesToRadioButton() {
        scrollAndClick("//input[@id='yesContent']", SelectorType.XPATH);
    }

    public static void clickNoToRadioButton() {
        scrollAndClick("//input[@value ='N']", SelectorType.XPATH);
    }

    public static String getOverviewThirdCountryContinuationText() {
        return getText("//*[@id='main-content']//dl/div[3]/dd[1]", SelectorType.XPATH);
    }

    public static boolean noSelectionAdvisoryText() {
        return isElementPresent("//p[contains(text(),'If you are not continuing to a third-country, you')]",SelectorType.XPATH);
    }
}
