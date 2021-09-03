package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.util.List;

import static org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage.getBilateralErrorMessage;
import static org.junit.Assert.assertEquals;

public class NumberOfPermitsPageJourney extends BasePermitJourney {

    public static int fieldCount;
    public static int numberOfPermits;
    public static String cabotageValue;
    public static String label;
    public static String permitValue;
    public static String standardValue;
    public static LocalDateTime applicationDate;

    public static int getFieldCount() {
        return fieldCount;
    }

    public static void setFieldCount(int fieldCount) {
        NumberOfPermitsPageJourney.fieldCount = fieldCount;
    }

    public static void setNumberOfPermits(int numberOfPermits) {
        NumberOfPermitsPageJourney.numberOfPermits = numberOfPermits;
    }

    public static int getNumberOfPermits() {
        return NumberOfPermitsPageJourney.numberOfPermits;
    }

    public static String getCabotageValue() {
        return cabotageValue;
    }

    public static void setCabotageValue(String cabotageValue) {
        NumberOfPermitsPageJourney.cabotageValue = cabotageValue;
    }

    public static String getLabel() {
        return NumberOfPermitsPageJourney.label;
    }

    public static String getPermitValue() {
        return permitValue;
    }

    public static void setPermitValue(String permitValue) {
        NumberOfPermitsPageJourney.permitValue = permitValue;
    }


    public static String getStandardValue() {
        return standardValue;
    }

    public static void setStandardValue(String standardValue) {
        NumberOfPermitsPageJourney.standardValue = standardValue;
    }

    public static void setApplicationDate(LocalDateTime applicationDate) {
        NumberOfPermitsPageJourney.applicationDate = applicationDate;
    }

    public static LocalDateTime getApplicationDate() {
        return NumberOfPermitsPageJourney.applicationDate;
    }

    public static void hasPageHeading() {
        String heading = NumberOfPermitsPage.getPageHeading();
        assertEquals("How many permits do you need?", heading);
    }

    public static void hasECMTPageHeading() {
        String heading = NumberOfPermitsPage.getPageHeading();
        assertEquals("How many permits do you require for this licence?", heading);
    }

    public static void completePage() {
        setNumberOfPermitsAndSetRespectiveValues();
        saveAndContinue();
    }

    public static void setNumberOfPermitsAndSetRespectiveValues() {
        List<WebElement> webLocators = findAll("//input[@type='text']", SelectorType.XPATH);
        if(webLocators.size() > 1) {
            String value = setNumberOfPermitsAndGetValue("//input[@id='cabotage']", 4);
            setCabotageValue(value);

            value = setNumberOfPermitsAndGetValue("//input[@id='standard']", 5);
            setStandardValue(value);

            setFieldCount(2);
        }
        else {
            String value = setNumberOfPermitsAndGetValue("//input[@type='text']", 3);
            setPermitValue(value);
            setFieldCount(1);
        }
        label = NumberOfPermitsPage.getPermitLabel();
    }

    public static String setNumberOfPermitsAndGetValue(String selector, int numberOfPermits) {
        WebElement field = findElement(selector, SelectorType.XPATH);
        field.sendKeys(String.valueOf(numberOfPermits));
        return field.getAttribute("value");
    }

    public static void completeECMTPage() {
        NumberOfPermitsPage.enterEuro5OrEuro6permitsValue();
        saveAndContinue();
    }

    public static void hasBilateralErrorMessage() {
        assertEquals(getBilateralErrorMessage(), "Enter the number of permits you require");
    }
}
