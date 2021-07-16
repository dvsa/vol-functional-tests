package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.concurrent.TimeUnit;

public class EmissionStandardsPage extends BasePermitPage {

    private static String confirmationCheckBox = "//input[@type='checkbox']";

    public static void untilOnPage() {
        untilElementIsPresent("//h1[contains(text(),'Which vehicle euro emission standard will you use?')]", SelectorType.XPATH, 10L, TimeUnit.SECONDS);
    }

    public static boolean isEuro3To6RadioButtonsChoices() {
        return isElementPresent("//input[@type='radio']", SelectorType.XPATH) &&
        isElementPresent("//label[contains(text(),'Euro 3 or Euro 4')]", SelectorType.XPATH) &&
        isElementPresent("//label[contains(text(),'Euro5, Euro 6 or higher emission standard')]", SelectorType.XPATH);
    }

    public static void clickYes() {
        scrollAndClick("//input[@id='yesContent']", SelectorType.XPATH);
    }

    public static void clickNo() {
        scrollAndClick("//input[@value ='N']", SelectorType.XPATH);
    }

    public static boolean isAdvisoryTextPresent() {
        return isElementPresent("//p[contains(text(),'If you transport goods using an ECMT permit, the vehicles you intend to use must meet and comply with the minimum Euro emission standards that the permit allocated to you allows.')]", SelectorType.XPATH) &&
        isElementPresent("//p[contains(text(),'This includes any vehicles you currently own or lease, or are intending to buy or lease in the future.')]", SelectorType.XPATH) &&
        isElementPresent("//label[contains(text(),'I confirm that I will only use my ECMT permits with vehicles that meet the minimum euro emissions standards allowed.')]", SelectorType.XPATH);
    }

    public static void ukraineSelectNoAdvisoryText() {
        isElementPresent("//p[contains(text(),'You do not need permits for Ukraine if you use Eur')]", SelectorType.XPATH);
    }

    public static String getCheckboxText() {
        return getText("//label[@class='form-control form-control--checkbox form-control--advanced']", SelectorType.XPATH);
    }

    public static void confirmCheckbox() {
        boolean isNotSelected = !isElementPresent(confirmationCheckBox  + "/ancestor::label[contains(@class, 'selected')]", SelectorType.XPATH);
        if (isNotSelected) {
            scrollAndClick(confirmationCheckBox, SelectorType.XPATH);
        }
    }

}
