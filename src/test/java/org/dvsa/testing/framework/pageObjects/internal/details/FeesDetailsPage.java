package org.dvsa.testing.framework.pageObjects.internal.details;

import activesupport.string.Str;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.pageObjects.enums.SelectorType.XPATH;

public class FeesDetailsPage extends BaseDetailsPage {

    private static final String WAIVE = "//*[@id='fee-details[waiveRemainder]']";

    private static final String NTH_FEE = "//button[@id='pay']";

    public static void pay() {
        scrollAndClick("//*[@id='pay']", XPATH);
    }

    public static void checkAll(){
        clickById("checkall");
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
        return isElementPresent(NTH_FEE, SelectorType.XPATH);
    }

    public static void confirmWaive() {
        waitForElementToBePresent(WAIVE);
        waitAndClick(WAIVE, XPATH);
    }

    public static void enterWaiveNote() {
        String selector = "//textarea[@id='fee-details[waiveReason]']";
        untilVisible(selector, XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
        scrollAndEnterField(selector, XPATH, Str.randomWord(180));
    }

    public static void clickRecommend() {
        scrollAndClick("button#recommend");
    }

    public static void clickApprove() {
        scrollAndClick("button#approve");
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
