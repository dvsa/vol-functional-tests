package org.dvsa.testing.framework.pageObjects.external.pages;

import activesupport.number.Int;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CountrySelectionPage extends BasePermitPage {

    public static void untilOnPage() {
        untilElementIsPresent(PAGE_HEADING, SelectorType.XPATH,10L, TimeUnit.SECONDS);
    }

    public static String selectCountry(String countryName) {
        String country = String.format("//label[contains(text(),'%s')]", countryName);
        String countryCheckbox = country.concat("//input[@type='checkbox']");

        String countryText = getText(country, SelectorType.XPATH);
        scrollAndClick(countryCheckbox, SelectorType.XPATH);
        saveAndContinue();
        return (countryText);
    }

    public static List<Country> allCountries(){
        List<Country> allCountries = new ArrayList<>();
        List<String> countries = countries();
        countries.forEach((country) -> {
            scrollAndClick(String.format("//label[text()='%s']//input[@type='checkbox']", country),
                    SelectorType.XPATH);
        });
        return allCountries;
    }

    public static List<Country> randomCountries() {
        List<Country> countries = nRandomCountries(countries().size());
        countries.forEach((country) -> {
            scrollAndClick(String.format("//label[text()='%s']//input[@type='checkbox']", country.toString()),
                    SelectorType.XPATH);
        });
        return countries;
    }

    private static List<Country> nRandomCountries(int max) {
        List<Country> randomCountries = new ArrayList<>();
        List<String> countries = countries();
        int iterationsToDo = Int.random(1, (max > countries.size()) ? countries.size() : max);

        for (int i = 0; i < iterationsToDo; i++) {
            int idx = Int.random(0, countries.size() - 1);
            randomCountries.add(Country.toEnum(countries.get(idx)));
            countries.remove(idx);
        }

        return randomCountries;
    }

    public static int numberOfCountries() {
        return size("(//input[@type='checkbox'])",  SelectorType.XPATH);
    }

    public static List<String> countries() {
        String selector = "//input[@type='checkbox']/..";
        return findAll(selector, SelectorType.XPATH).stream().map(el -> el.getText().trim()).collect(Collectors.toList());
    }

    public static List<String> countriesSelected() {
        String selector = "//li[2]//ul[1]";
        return findAll(selector, SelectorType.XPATH).stream().map(el -> el.getText().trim()).collect(Collectors.toList());
    }

    public static List<String> selectedCountries() {
        return findAll("//*[contains(@class, 'selected')]", SelectorType.XPATH).stream().map(el -> el.getText().trim()).collect(Collectors.toList());
    }

    public static boolean isErrorMessagePresent(){
        return isElementPresent("//li[@class='validation-summary__item']", SelectorType.XPATH);
    }
}
