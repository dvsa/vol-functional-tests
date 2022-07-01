package org.dvsa.testing.framework.pageObjects.internal.details;

import activesupport.string.Str;
import org.dvsa.testing.framework.enums.Duration;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.pageObjects.enums.SelectorType.XPATH;

public class FeesDetailsPage extends BaseDetailsPage {

    private static final String WAIVE = "//*[@id='fee-details[waiveRemainder]']";

    private static final String NTH_FEE = "(//*[@data-heading='Description'])[%d]//a";

    public static void pay() {
        scrollAndClick("//*[text()='Pay']", XPATH);
    }

    public static void outstanding() {
        scrollAndClick("//th[last()]", XPATH);
    }

    public static void select1stFee() {
        String firstFee= "//tbody/tr/td/a";
        untilElementIsPresent(firstFee, XPATH, Duration.CENTURY, TimeUnit.SECONDS);
        waitAndClick(firstFee, XPATH);
    }

    public static boolean hasFee() {
        return tryUntilElementIsPresent(String.format(NTH_FEE, 1), XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
    }

    public static void confirmWaive() {
        if (isNotWaived())
            waitAndClick(WAIVE, XPATH);
    }

    private static boolean isNotWaived() {
        waitForElementToBePresent(WAIVE);
        return !tryUntilElementIsPresent(
                WAIVE + "/ancestor::label[@class='selected']", XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
    }

    public static void enterWaiveNote() {
        String selector = "//textarea[@id='fee-details[waiveReason]']";
        untilVisible(selector, XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
        scrollAndEnterField(selector, XPATH, Str.randomWord(180));
    }

    public static void clickRecommend() {
        scrollAndClick("button#recommend");
    }

    public static void clickBackToHome() {
        waitAndClick("//a[@class='govuk-back-link']", XPATH);
    }

    public static void untilFeePaidNotification() {
        untilElementIsPresent("//*[contains(text(), 'The fee(s) have been paid successfully')]", XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
    }

    public static void untilOnFeePage() {
        untilElementIsPresent("//h1[@class='js-title']", XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
    }
}
