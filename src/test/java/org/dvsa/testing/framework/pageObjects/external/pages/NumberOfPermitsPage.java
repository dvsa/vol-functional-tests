package org.dvsa.testing.framework.pageObjects.external.pages;

import activesupport.number.Int;
import activesupport.string.Str;
import org.dvsa.testing.framework.pageObjects.type.Permit;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class NumberOfPermitsPage extends BasePermitPage {

    //TODO: Move these to bilaterals when fixing the journeys.

    public final static String FIELD = "(//*[contains(@class, 'field')]//input[@type='number'])";

    public static void untilOnPage() {
        untilElementIsPresent("//h1[contains(text(),'How many permits do you')]", SelectorType.XPATH, 10L, TimeUnit.SECONDS);
    }

    public static String getPermitLabel() {
        return getText("//div[contains(@class,'field')]//label", SelectorType.XPATH);
    }

    public static String getStandardLabel() {
        return getText("//label[contains(text(),'Standard multiple journey permit')]", SelectorType.XPATH);
    }

    public static String getCabotageLabel() {
        return getText("//label[contains(text(),'Cabotage multiple journey permit')]", SelectorType.XPATH);
    }

    public static int numberOfFields() {
        return size(FIELD, SelectorType.XPATH);
    }

    public static String getNthCountry(int position) {
        return getText(FIELD.concat(String.format("[%d]/../../legend", position)), SelectorType.XPATH).trim();
    }

    public static int getNthFee(int index) {
        String feeSelector = String.format(".guidance-blue strong:nth-of-type(%d)", index + 1);
        return Integer.parseInt(Str.find("\\d+", getText(feeSelector)).get());
    }

    public static String getYear(int idx) {
        idx += 1;
        return Str.find("\\d+", getText(FIELD.concat(String.format("[%d]", idx) + "/../label"), SelectorType.XPATH)).get();
    }

    public static boolean isAdvisoryTextPresent() {
        return isTextPresent("There is a £10 non-refundable application fee per permit.") &&
        isTextPresent("If your application is successful, you will need to pay an additional");
    }

    public static boolean isTurkeyAndUkraineBilateralStandardSingleInformationPresent() {
        return isElementPresent("//label[contains(text(),'Standard single journey permit')]", SelectorType.XPATH) &&
        isElementPresent("//p[@class='hint']", SelectorType.XPATH);
    }

    public static String getBilateralErrorMessage() {
        return getText("//a[contains(text(),'Enter the number of permits you require')]",SelectorType.XPATH);
    }

    public static boolean isFeeTextPresent() {
        return isElementPresent("//div[@class='guidance-blue']", SelectorType.XPATH) &&
        isElementPresent("//strong[contains(text(),'£18')]", SelectorType.XPATH);
    }

    public static void enterAuthorisedVehicles() {
        scrollAndEnterField("//input[@type ='text']", SelectorType.XPATH, String.valueOf(Int.random(1, getMaximumAuthorisedVehicles())), false);
    }

    public static void exceedAuthorisedVehicle() {
        scrollAndEnterField("//input[@type ='text']", SelectorType.XPATH, String.valueOf(getMaximumAuthorisedVehicles() + 1), false);
    }

    public static int getMaximumAuthorisedVehicles() {
        String regex = getText("//p[contains(@class,'hint')]", SelectorType.XPATH);
        return Integer.parseInt(Str.find("\\d+", regex).get());
    }

    public static boolean isShortTermECMTEmissionErrorTextPresent() {
        return isElementPresent("//p[contains(text(),'Select one euro emission standard')]", SelectorType.XPATH);
    }

    public static boolean isEnterNumberOfPermitsErrorTextPresent() {
        return isElementPresent("//p[contains(text(),'Enter the number of permits you require')]", SelectorType.XPATH);
    }

    public static boolean isShortTermEnterNumberOfPermitsErrorTextPresent() {
        return isElementPresent("//p[contains(text(),'Enter how many permits you need')]", SelectorType.XPATH);
    }

    public static boolean isMaximumNumberOfPermitsExceededErrorTextPresent() {
        return isElementPresent("//p[contains(text(),'You have exceeded the maximum you can apply for')]", SelectorType.XPATH);
    }

    public static void enterEuro5OrEuro6permitsValue() {
        if (numberOfRadioButtons() > 1) {
            euro5OrEuro6Select();
            scrollAndEnterField("//input[@type ='text']", SelectorType.XPATH, "1");
        }
        scrollAndEnterField("//input[@type ='text']", SelectorType.XPATH, "1");
    }

    private static int numberOfRadioButtons() {
        return size("//input[@type='radio']", SelectorType.XPATH);
    }

    public static void euro5OrEuro6Select() {
        scrollAndClick("//input[@type='radio']", SelectorType.XPATH);
    }

    //TODO: Possibly look at splitting this into Bilaterals and others.
}
