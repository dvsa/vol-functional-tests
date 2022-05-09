package org.dvsa.testing.framework.pageObjects.internal.irhp;


import activesupport.dates.Dates;
import activesupport.string.Str;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;
import org.joda.time.LocalDate;

import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public class IrhpPermitsApplyPage extends BaseDetailsPage {

    private static final String EMISSIONS = "//label[contains(text(),'I confirm that I will only use my ECMT permits wit')]";
    private static final String DECLARATION = "//input[@id='declaration']";
    private static final String NEED_ECMT_PERMIT = "//label[contains(text(),'I confirm that I need to apply for an ECMT permit to make 3 cross-trade jobs, cross-trade between EU and non-EU countries or travel to the ECMT member countries listed.')]";

    static Dates date = new Dates(LocalDate::new);

    static LinkedHashMap<String, String> currentDate = date.getDateHashMap(0, 0, 0);

    public static void applyForPermit() {
        scrollAndClick("//button[@id='Apply']", SelectorType.XPATH);
    }

    public static void permitsQuantityEcmtAPGGInternal(int number) {
        String Selector = "//input[@id='qa[fieldset172][permitsRequired]']";
        untilElementIsPresent(Selector, SelectorType.XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
        scrollAndEnterField(Selector, SelectorType.XPATH, String.valueOf(number));
    }
    public static void emissionRadioSelect() {
        String Selector = "//input[@name='qa[fieldset172][emissionsCategory]']";
        untilElementIsPresent(Selector, SelectorType.XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
        scrollAndClick(Selector,SelectorType.XPATH);
    }

    public static void emissionRadioSelectNew() {
        String Selector = "//input[@value='euro6']";
        untilElementIsPresent(Selector, SelectorType.XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
        scrollAndClick(Selector,SelectorType.XPATH);
    }

    public static void numberOfPermitsShortTermAPSG(int number) {
        String Selector = "//input[@id='qa[fieldset203][permitsRequired]']";
        untilElementIsPresent(Selector, SelectorType.XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
        scrollAndEnterField(Selector, SelectorType.XPATH, String.valueOf(number));
    }
    public static void restrictedCountriesNo() {
        String Selector = "//label[contains(text(),'No')]";
        waitAndClick(Selector,SelectorType.XPATH);
    }

    public static void permitStartDate() {
        waitAndClick("//input[@name='qa[fieldset37][qaElement][day]']", SelectorType.XPATH);
        waitAndEnterText("//input[@name='qa[fieldset37][qaElement][day]']", SelectorType.XPATH, currentDate.get("day"));
        waitAndClick("//input[@name='qa[fieldset37][qaElement][month]']", SelectorType.XPATH);
        waitAndEnterText("//input[@name='qa[fieldset37][qaElement][month]']", SelectorType.XPATH, currentDate.get("month"));
        waitAndClick("//input[@name='qa[fieldset37][qaElement][year]']", SelectorType.XPATH);
        waitAndEnterText("//input[@name='qa[fieldset37][qaElement][year]']", SelectorType.XPATH, currentDate.get("year"));
    }

    public static void datePermitNeededShortTermApgg() {
        waitAndClick("//input[@id='qaDateSelect_day']", SelectorType.XPATH);
        waitAndEnterText("//input[@id='qaDateSelect_day']", SelectorType.XPATH, currentDate.get("day"));
        waitAndClick("//input[@id='qaDateSelect_month']", SelectorType.XPATH);
        waitAndEnterText("//input[@id='qaDateSelect_month']", SelectorType.XPATH, currentDate.get("month"));
        waitAndClick("//input[@id='qaDateSelect_year']", SelectorType.XPATH);
        waitAndEnterText("//input[@id='qaDateSelect_year']", SelectorType.XPATH, currentDate.get("year"));
    }

    public static void numberOfPermitRemoval() {
        waitAndEnterText("//input[@name='qa[fieldset35][qaElement]']", SelectorType.XPATH,"1");
    }

    public static void removalsEligibility() {
        waitAndClick("//label[contains(text(),'I confirm that I will only use an ECMT internation')]",SelectorType.XPATH);
    }

    public static void needECMTPermit() {
        waitAndClick(NEED_ECMT_PERMIT,SelectorType.XPATH);
    }

    public static void cabotageEligibility() {
        waitAndClick("//label[contains(text(),'I confirm that I will not undertake cabotage journ')]", SelectorType.XPATH);
    }

    public static int getNumberOfPermits() {
        String Selector = "p.hint";
        untilElementIsPresent(Selector, Duration.MEDIUM, TimeUnit.SECONDS);
        return Integer.parseInt(Str.find("\\d+", getText(Selector)).get());
    }

    public static void certificatesRequired() {
        waitAndClick("//label[contains(text(),'I understand that I must obtain and carry the appr')]",SelectorType.XPATH);
    }

    public static void isEuro6Compliant(boolean isCompliant) {
        if (isCompliant && emissionsNotSelected()) {
        scrollAndClick(EMISSIONS, SelectorType.XPATH);
        } else if (!isCompliant && emissionsSelected()) {
            scrollAndClick(EMISSIONS, SelectorType.XPATH);
        }
    }

    private static boolean emissionsSelected() {
        return isElementPresent(EMISSIONS.concat("/../self::label[contains(@class, 'selected')]"), SelectorType.XPATH);
    }

    private static boolean emissionsNotSelected() {
        return !emissionsSelected();
    }

    public static void declare(boolean declaration) {
        if (declaration && declarationNotSelected()) {
            scrollAndClick(DECLARATION, SelectorType.XPATH);
        } else if (!declaration && declarationSelected()) {
            scrollAndClick(DECLARATION, SelectorType.XPATH);
        }
    }

    private static boolean declarationNotSelected() {
        return !declarationSelected();
    }

    private static boolean declarationSelected() {
        return isElementPresent(DECLARATION.concat("/../self::label[contains(@class, 'selected')]"), SelectorType.XPATH);
    }

    public static void saveIRHP() {
        scrollAndClick("//button[@id='saveIrhpPermitApplication']", SelectorType.XPATH);
    }

    public static void submitApplication() {
        scrollAndClick("#menu-irhp-application-decisions-submit", SelectorType.CSS);
    }

    public static void viewApplication() {
        String applicationLink = "//tbody/tr/td/a";
        untilElementIsPresent(applicationLink, SelectorType.XPATH, Duration.CENTURY, TimeUnit.SECONDS);
        scrollAndClick(applicationLink,SelectorType.XPATH);
    }

    public static void submitIRHP() {
        scrollAndClick("#menu-irhp-application-decisions-submit", SelectorType.CSS);
    }

    public static void submitButtonExists() {
        untilElementIsPresent("#menu-irhp-application-decisions-submit", SelectorType.CSS, Duration.MEDIUM, TimeUnit.SECONDS);
    }

    public static void submitButtonAPSGExists() {
        untilElementIsPresent("#menu-irhp-application-decisions-submit", SelectorType.CSS, Duration.MEDIUM, TimeUnit.SECONDS);
    }

    public static void cancelButtonExists() {
        untilElementIsPresent("//a[@id='menu-irhp-application-quick-actions-cancel']", SelectorType.XPATH, Duration.LONG, TimeUnit.SECONDS);
    }

    public static void cancelStatusExists() {
        untilElementIsPresent("//span[@class='status grey']", SelectorType.XPATH, Duration.LONG, TimeUnit.SECONDS);
    }

    public static void withdrawStatusExists() {
        untilElementIsPresent("//span[@class='status red']", SelectorType.XPATH, Duration.LONG, TimeUnit.SECONDS);
    }

    public static void cancelPermitApplication() {
        String cancelButton = "//a[@id='menu-irhp-application-quick-actions-cancel']";
        String continueButton = "//button[@id='form-actions[confirm]']";
        untilElementIsPresent(cancelButton, SelectorType.XPATH, Duration.LONG, TimeUnit.SECONDS);
        scrollAndClick(cancelButton, SelectorType.XPATH);
        untilElementIsPresent(continueButton, SelectorType.XPATH, Duration.LONG, TimeUnit.SECONDS);
        scrollAndClick(continueButton, SelectorType.XPATH);
    }

    public static void withdrawPermitApplication() {
        String withdrawButton = "//a[contains(text(), 'Withdraw application')]";
        String withdrawReasonButton = "//select[@id='withdraw-details[reason]']";
        String withdrawReason = "//option[contains(text(),'Permits withdrawn by user')]";
        String continueButton = "//button[@id='form-actions[withdraw]']";
        untilElementIsPresent(withdrawButton, SelectorType.XPATH, Duration.LONG, TimeUnit.SECONDS);
        scrollAndClick(withdrawButton, SelectorType.XPATH);
        waitAndSelectByIndex(withdrawReasonButton, SelectorType.XPATH,4);
       // waitAndClick(withdrawReason,SelectorType.XPATH);
        untilElementIsPresent(continueButton, SelectorType.XPATH, Duration.LONG, TimeUnit.SECONDS);
        scrollAndClick(continueButton, SelectorType.XPATH);
    }

    public static void selectApplication() {
        String payButton = "//button[@id='pay']";
        scrollAndClick("//input[@id='checkall']",SelectorType.XPATH);
        untilElementIsPresent(payButton, SelectorType.XPATH, Duration.LONG, TimeUnit.SECONDS);
        scrollAndClick(payButton, SelectorType.XPATH);
    }

    public static void selectCardPayment() {
        String paymentMethod = "//select[@id='details[paymentType]']";
        untilElementIsPresent(paymentMethod, SelectorType.XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
        selectValueFromDropDown(paymentMethod, SelectorType.XPATH, PaymentMethod.Card.toString());
        scrollAndClick("//button[@id='form-actions[pay]']", SelectorType.XPATH);
        untilElementIsPresent("#scp_cardPage_cardNumber_input", SelectorType.CSS, Duration.LONG, TimeUnit.SECONDS);
    }

    public static boolean submitButtonNotExists() {
        return !isElementPresent("#menu-irhp-permit-decisions-submit", SelectorType.CSS);
    }

    public static boolean withdrawButtonNotExists() {
        return isElementPresent("//a[@id='menu-irhp-permit-decisions-withdraw']", SelectorType.XPATH);
    }

    public static boolean cancelButtonNotExists() {
        return isElementPresent("#menu-irhp-application-quick-actions-cancel", SelectorType.CSS);
    }

    public static void grantApplication() {
        waitAndClick("//a[contains(text(), 'Grant application')]", SelectorType.XPATH);
    }

    public static void continueButton() {
        BaseModel.untilModalIsPresent(30L, TimeUnit.SECONDS);
        waitAndClick("//button[@id='form-actions[confirm]']",SelectorType.XPATH);
    }

    public static void underConsiderationStatusExists() {
        untilElementIsPresent("//span[@class='status orange']", SelectorType.XPATH, Duration.LONG, TimeUnit.SECONDS);
    }

    public enum PaymentMethod{
        Card("Card Payment"),
        Cash("Cash"),
        Cheque("Cheque");

        private String option;

        PaymentMethod(String option) {
            this.option = option;
        }

        @Override
        public String toString(){
            return option;
        }
    }

    public static void untilOnPage(){
        untilVisible("//button[@id='Apply']",SelectorType.XPATH,30,TimeUnit.SECONDS);
    }

    public static boolean isErrorTextPresent() {
        String Selector = "//p[@class='error__text' and contains(text(),'You have exceeded the maximum you can apply for')]";
        return isElementPresent(Selector,SelectorType.XPATH);
    }
    public static void isWithdrawButtonPresent() {
        isElementPresent("//a[@id='menu-irhp-application-decisions-withdraw']",SelectorType.XPATH);
    }
    public static void permitsFeePage(){
        untilElementWithText(ChronoUnit.SECONDS, Duration.CENTURY);
    }
    public static void permitsSelectFeeTab(){
        waitAndClick("//a[@id='menu-licence_irhp_applications-fees']",SelectorType.XPATH);
    }
}