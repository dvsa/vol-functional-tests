package org.dvsa.testing.framework.pageObjects.internal.irhp;

import activesupport.dates.Dates;
import activesupport.string.Str;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;
import org.joda.time.LocalDate;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public class IrhpPermitFeesPage extends BaseDetailsPage {

    private static DecimalFormat df = new DecimalFormat("#");
    static Dates date = new Dates(LocalDate::new);
    static LinkedHashMap<String, String> currentDate = date.getDateHashMap(0, 0, 0);


    public static int getFeeAmount() {
        String feeAmount = Str.find("(?<=£)\\d+", getText("//tbody/tr[1]/td[5]", SelectorType.XPATH)).get();
        return Integer.parseInt(feeAmount);
    }

    public static int getOutstandingFee() {
        String Outstanding = Str.find("(?<=£)\\d+", getText("//tbody/tr[1]/td[6]", SelectorType.XPATH)).get();
        return Integer.parseInt(Outstanding);
    }

    public static int getFeeAmountFromDetails() {
        return Integer.parseInt(Str.find("(?<=£)\\d+", getText("//*[@id='main']/div[1]/div/div[2]/div[1]/ul/li[3]/dd", SelectorType.XPATH)).get());
    }

    public static int getOutstandingFromDetails() {
        return Integer.parseInt(Str.find("(?<=£)\\d+", getText("//*[@id='main']/div[1]/div/div[2]/div[1]/ul/li[4]/dd", SelectorType.XPATH)).get());
    }

    public static int getFeeAmountInFrame() {
        return Integer.parseInt(Str.find("(?<=)\\d+", getAttribute("//*[@id='details[maxAmountForValidator]']", SelectorType.XPATH, "value")).get());
    }

    public static void clickFeeDetailsLink() {
        waitAndClick("//tbody/tr[1]/td[2]/a[1]", SelectorType.XPATH);
    }

    public static void underPayFee() {
        int fee = Integer.parseInt(String.valueOf(getFeeAmountInFrame()));
        int newFee = fee - 5;
        enterPaymentDetails(newFee);
        confirmPayFee();
    }

    public static void overPayFee() {
        int fee = Integer.parseInt(String.valueOf(getFeeAmountInFrame()));
        int newFee = (fee * 2) - 1;
        enterPaymentDetails(newFee);
        enterChequeDetails();
        confirmPayFee();
    }

    public static void moreThanDoublePayment() {
        int fee = Integer.parseInt(String.valueOf(getFeeAmountInFrame()));
        int newFee1 = (fee * 2) + 10;
        enterPaymentDetails(newFee1);
        enterChequeDetails();
        confirmPayFee();
    }

    public static void fillInCashDetailsAndPay() {
        enterPaymentDetails(5);
        confirmPayFee();
    }

    private static void confirmPayFee() {
        scrollAndClick("//button[@id='form-actions[pay]']", SelectorType.XPATH);
    }

    private static void enterChequeDetails() {
        scrollAndEnterField("//input[@id='details[chequeNo]']", SelectorType.XPATH, "23435");
        scrollAndEnterField("//input[@id='details[chequeDate]_day']", SelectorType.XPATH, currentDate.get("day"));
        scrollAndEnterField("//input[@id='details[chequeDate]_month']", SelectorType.XPATH, currentDate.get("month"));
        scrollAndEnterField("//input[@id='details[chequeDate]_year']", SelectorType.XPATH, currentDate.get("year"));
    }

    private static void enterPaymentDetails(int feeValue) {
        scrollAndEnterField("//input[@id='details[received]']", SelectorType.XPATH, String.valueOf(feeValue));
        scrollAndEnterField("//input[@id='details[payer]']", SelectorType.XPATH,"smith");
        scrollAndEnterField("//input[@id='details[slipNo]']", SelectorType.XPATH,"23435");
    }

    public static String getPaymentWarningMessage(){
        untilVisible("//div[@class='modal--alert']", SelectorType.XPATH,1000,TimeUnit.MILLISECONDS);
        return getText("//p[contains(text(),'The payment ')]", SelectorType.XPATH);
    }

    public static void clickConfirmPaymentButton() {
        waitAndClick("//button[@id='form-actions[confirm]']", SelectorType.XPATH);
    }

    public static void clickCancel() {
        waitAndClick("//button[@id='cancel']", SelectorType.XPATH);
    }

     public static boolean areNoOutstandingFeesPresent() {
        return isTextPresent("There are no results matching your search");
    }

}