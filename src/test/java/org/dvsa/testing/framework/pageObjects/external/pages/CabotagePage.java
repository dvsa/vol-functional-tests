package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class CabotagePage extends BasePermitPage {

    private static String cabotageConfirmation = "//input[@id='qaElement']";

    public static void confirmWontUndertakeCabotage() {
        if (isNotConfirmed())
            scrollAndClick(cabotageConfirmation, SelectorType.XPATH);
    }

    private static boolean isNotConfirmed() {
        return !isElementPresent( cabotageConfirmation+ "/ancestor::label[contains(@class, 'selected')]", SelectorType.XPATH);
    }

    public static void clickNoToCabotage() {
        waitAndClick("//label[contains(text(),'No')]", SelectorType.XPATH);
    }

    public static void clickYesToCabotage() {
        waitAndClick("//label[contains(text(),'Yes')]", SelectorType.XPATH);
    }

    public static String yesAndCabotagePermitConfirmation() {
        List<WebElement> options = findAll("//*[starts-with(text(), 'I')]",SelectorType.XPATH);
        Random random = new Random();
        int index = random.nextInt(options.size());
        options.get(index).click();
        return String.valueOf(random);
    }
}
