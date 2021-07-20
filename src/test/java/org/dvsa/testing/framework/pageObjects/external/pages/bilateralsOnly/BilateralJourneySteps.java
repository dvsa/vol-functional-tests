package org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class BilateralJourneySteps extends BasePermitPage {

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

    public static void clickFinishButton() {
        waitAndClick("//a[contains(text(),'Go to permits dashboard')]",SelectorType.XPATH);
    }

    public static boolean areSectionsPresent(boolean hasCabotage) {
        boolean areSectionsPresent = isTextPresent("//dt[contains(text(),'Validity period')]") &&
        isTextPresent("//dt[contains(text(),'Permit usage')]") &&
        isTextPresent("//dt[contains(text(),'Number of permits')]");
        if (hasCabotage) {
            areSectionsPresent = areSectionsPresent && isTextPresent("//dt[contains(text(),'Cabotage needed')]");
        }
        return areSectionsPresent;
    }

    public static String getPeriodText() {
        return getText("//*[@id='main-content']//dl/div[1]/dd[1]", SelectorType.XPATH);
    }

    public static String getJourney() {
        return getText("//*[@id='main-content']/div/div/dl/div[2]/dd[1]", SelectorType.XPATH);
    }

    public static String getCabotageValue() {
        return getText("//*[@id='main-content']//dl/div[3]/dd[1]", SelectorType.XPATH);
    }

    public static String getPermitValue() {
        return getText("//*[@id='main-content']//dl/div[4]/dd[1]", SelectorType.XPATH);
    }

    public static String getPermitValueMultiple() {
        return getText("//div[4]//dd[1]", SelectorType.XPATH);
    }
}
