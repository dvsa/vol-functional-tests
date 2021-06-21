package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class NumberOfPermitsPageJourneySteps extends BasePermitJourney {

    public static int fieldCount;
    public static String cabotageLabel;
    public static String cabotageValue;
    public static String label;
    public static String permitValue;
    public static String standardLabel;
    public static String standardValue;

    public static int getFieldCount() {
        return fieldCount;
    }

    public static void setFieldCount(int fieldCount) {
        NumberOfPermitsPageJourneySteps.fieldCount = fieldCount;
    }

    public static String getCabotageLabel() {
        return cabotageLabel;
    }

    public static void setCabotageLabel(String cabotageLabel) {
        NumberOfPermitsPageJourneySteps.cabotageLabel = cabotageLabel;
    }

    public static String getCabotageValue() {
        return cabotageValue;
    }

    public static void setCabotageValue(String cabotageValue) {
        NumberOfPermitsPageJourneySteps.cabotageValue = cabotageValue;
    }

    public static String getLabel() {
        return NumberOfPermitsPageJourneySteps.label;
    }

    public static String getPermitValue() {
        return permitValue;
    }

    public static void setPermitValue(String permitValue) {
        NumberOfPermitsPageJourneySteps.permitValue = permitValue;
    }

    public static String getStandardLabel() {
        return standardLabel;
    }

    public static void setStandardLabel(String standardLabel) {
        NumberOfPermitsPageJourneySteps.standardLabel = standardLabel;
    }

    public static String getStandardValue() {
        return standardValue;
    }

    public static void setStandardValue(String standardValue) {
        NumberOfPermitsPageJourneySteps.standardValue = standardValue;
    }

    public static void completePage() {
        setNumberOfPermitsAndSetRespectiveValues();
        saveAndContinue();
    }

    public static void hasPageHeading() {
        String heading = NumberOfPermitsPage.getPageHeading();
        assertEquals("How many permits do you need?", heading);
    }

    public static void hasECMTPageHeading() {
        String heading = NumberOfPermitsPage.getPageHeading();
        assertEquals("How many permits do you require for this licence?", heading);
    }

    public static void setNumberOfPermitsAndSetRespectiveValues() {
        List<WebElement> webLocators = findAll("//input[@type='text']", SelectorType.XPATH);
        if(webLocators.size() > 1) {
            String value = setNumberOfPermitsAndGetValue("//input[@id='cabotage']", 4);
            setCabotageValue(value);
            setCabotageLabel(NumberOfPermitsPage.getCabotageLabel());

            value = setNumberOfPermitsAndGetValue("//input[@id='standard']", 5);
            setStandardValue(value);
            setStandardLabel(NumberOfPermitsPage.getStandardLabel());

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


}
