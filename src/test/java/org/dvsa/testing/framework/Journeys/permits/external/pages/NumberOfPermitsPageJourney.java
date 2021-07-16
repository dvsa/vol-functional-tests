package org.dvsa.testing.framework.Journeys.permits.external.pages;

import Injectors.World;
import activesupport.number.Int;
import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.common.type.Permit;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.NumberOfPermitsPage;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.dvsa.testing.lib.newPages.external.pages.NumberOfPermitsPage.*;
import static org.junit.Assert.assertEquals;

public class NumberOfPermitsPageJourney extends BasePermitJourney {

    private static World world;
    public static int fieldCount;
    public static String cabotageLabel;
    public static String cabotageValue;
    public static String label;
    public static String permitValue;
    public static String standardLabel;
    public static String standardValue;
    public static LocalDateTime applicationDate;

    private static List<Permit> permitsPerCountry;

    public static int getFieldCount() {
        return fieldCount;
    }

    public static void setFieldCount(int fieldCount) {
        NumberOfPermitsPageJourney.fieldCount = fieldCount;
    }

    public static String getCabotageLabel() {
        return cabotageLabel;
    }

    public static void setCabotageLabel(String cabotageLabel) {
        NumberOfPermitsPageJourney.cabotageLabel = cabotageLabel;
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

    public static String getStandardLabel() {
        return standardLabel;
    }

    public static void setStandardLabel(String standardLabel) {
        NumberOfPermitsPageJourney.standardLabel = standardLabel;
    }

    public static String getStandardValue() {
        return standardValue;
    }

    public static void setStandardValue(String standardValue) {
        NumberOfPermitsPageJourney.standardValue = standardValue;
    }

    public static void setPermitsPerCountry(List<Permit> permitsPerCountry) {
        NumberOfPermitsPageJourney.permitsPerCountry = permitsPerCountry;
    }

    public static List<Permit> getPermitsPerCountry() {
        return NumberOfPermitsPageJourney.permitsPerCountry;
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

    public static void completeECMTPage() {
        NumberOfPermitsPage.enterEuro5OrEuro6permitsValue();
        saveAndContinue();
    }

    public static void completeBilateralPage() {
        quantity(1, PermitType.ANNUAL_BILATERAL);
        saveAndContinue();
    }

    public static void completeMultilateralPage() {
        int maxNumberOfPermits = world.createApplication.getNoOfVehiclesRequested();
        quantity(maxNumberOfPermits, PermitType.ANNUAL_MULTILATERAL);
        saveAndContinue();
        setApplicationDate(LocalDateTime.now());
    }

    public static void quantity(int maxNumberOfPermits, PermitType permitType){
        List<Permit> permitsPerCountry = new ArrayList<>();

        for (int idx = 0; idx < numberOfFields(); idx ++) {
            int randomQuantity = Int.random(maxNumberOfPermits);
            String selector = String.format(NumberOfPermitsPage.FIELD + "[%d]", idx + 1);
            scrollAndEnterField(selector, SelectorType.XPATH, String.valueOf(randomQuantity));

            if (permitType.equals(PermitType.ANNUAL_BILATERAL)) {
                Country country = Country.toEnum(getNthCountry(idx + 1));
                permitsPerCountry.add(new Permit(country, getYear(idx), randomQuantity));
            } else if (permitType.equals(PermitType.ANNUAL_MULTILATERAL)) {
                int fee = getNthFee(idx);
                permitsPerCountry.add(new Permit(getYear(idx), randomQuantity, fee));
            }
            setPermitsPerCountry(permitsPerCountry);
        }
    }

    public static void hasBilateralErrorMessage() {
        assertEquals(getBilateralErrorMessage(), "Enter the number of permits you require");
    }
}
