package org.dvsa.testing.framework.pageObjects.internal.irhp;

import activesupport.string.Str;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InternalAnnualBilateralPermitApplicationPage extends BaseDetailsPage {
    private static final String DATE_RECEIVED_YEAR = "#dateReceived_year";

    private static final String DATE_RECEIVED_MONTH = "#dateReceived_month";
    private static final String DATE_RECEIVED_DAY = "#dateReceived_day";

    private static final String DECLARATION = "//input[@id='declaration']";

    public static void untilOnPage() {
        untilUrlMatches("/licence/\\d+/irhp-application/(add|edit)/\\d+/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static LocalDate getDateReceived() {
        int year = Integer.parseInt(getAttribute(DATE_RECEIVED_YEAR, SelectorType.CSS, "value"));
        int month = Integer.parseInt(getAttribute(DATE_RECEIVED_MONTH, SelectorType.CSS, "value"));
        int day = Integer.parseInt(getAttribute(DATE_RECEIVED_DAY, SelectorType.CSS, "value"));

        return LocalDate.of(year, month, day);
    }

    public static void enterDateReceived(LocalDate date) {
        scrollAndEnterField(DATE_RECEIVED_DAY, String.valueOf(date.getDayOfMonth()));
        scrollAndEnterField(DATE_RECEIVED_MONTH, String.valueOf(date.getMonthValue()));
        scrollAndEnterField(DATE_RECEIVED_YEAR, String.valueOf(date.getYear()));
    }

    public static void enterDateReceived(String year, String month, String day) {
        scrollAndEnterField(DATE_RECEIVED_YEAR, year);
        scrollAndEnterField(DATE_RECEIVED_MONTH, month);
        scrollAndEnterField(DATE_RECEIVED_DAY, day);
    }

    public static boolean isFutureDateReceivedErrorPresent() {
        return isTextPresent("This date is not allowed to be in the future");
    }

    public static boolean isInvalidDateErrorPresent() {
        return isTextPresent("The input does not appear to be a valid value");
    }

    public static int getNumberOfAuthorisedVehicles() {
        return Integer.parseInt(getText("//p[text()='Current total vehicle authorization']/b", SelectorType.XPATH));
    }

    public static List<String> getListOfCountries() {
        return findAll("//*[@data-group='fields']//legend", SelectorType.XPATH)
                .stream().map(el -> el.getText().trim()).collect(Collectors.toList());
    }

    public static List<Window> openWindows() {
        List<WebElement> rows = findAll("//fieldset[@data-group='fields[permitsRequired]']//fieldset", SelectorType.XPATH);
        return rows.stream().map((row) -> {
            String countryName = row.findElement(By.cssSelector("legend")).getText();

            return row.findElements(By.xpath(".//div[label or p]")).stream().map((field) -> {
                String year;
                String maxNumPermits;
                String numPermits;

                if (noMorePermitsForWindow(field)) {
                    String subject = field.findElement(By.xpath("./p")).getText();
                    year = Str.find("(?<=for )\\d{4}", subject).get();
                    maxNumPermits = Str.find("(?<=All )\\d+", subject).get();
                    numPermits = maxNumPermits;
                } else {
                    year = Str.find("\\d+", field.findElement(By.xpath(".//label")).getText()).get();
                    maxNumPermits = Str.find("\\d+", field.findElement(By.xpath(".//p")).getText()).get();
                    numPermits = field.findElement(By.xpath(".//input[@type='number']")).getAttribute("value");
                }

                return (new Window(countryName, Integer.parseInt(year), Integer.parseInt(maxNumPermits))).setNumberOfPermits(Integer.parseInt(numPermits));
            }).collect(Collectors.toList());
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private static boolean noMorePermitsForWindow(WebElement element) {
        boolean noMorePermits = false;
        try {
            element.findElement(By.xpath("./self::div[label]"));
        } catch (NoSuchElementException ex) {
            noMorePermits = true;
        }
        return noMorePermits;
    }

    public static List<Window> numPermits(int... numPermits) {
        IntStream.range(0, numPermits.length).forEach((idx) -> {
            numPermits(idx + 1, numPermits[idx]);
        });
        return openWindows();
    }

    public static void numPermits(int idx, int numberOfPermits) {
        scrollAndEnterField(String.format("(//*[contains(@class, 'field')]//input[@type='number'])[%d]", idx), SelectorType.XPATH, String.valueOf(numberOfPermits));
    }

    public static int numberOfOpenWindows() {
        return openWindows().size();
    }

    public static boolean hasExceedNumberOfPermitsMessage() {
        return isTextPresent("You have exceeded the maximum you can apply for");
    }

    public static void confirmDeclaration() {
        if (!hasNotDeclared()) {
            scrollAndClick(DECLARATION, SelectorType.XPATH);
        }
    }

    private static boolean hasNotDeclared() {
        return !isElementPresent(DECLARATION + "/../self::*[contains(@class, 'selected')]", SelectorType.XPATH);
    }

    public static void save() {
        scrollAndClick("#saveIrhpPermitApplication");
    }

    public static void cancel() {
        scrollAndClick("#closeModal");
    }

    public static class Window {
        private String country;
        private int year;
        private int maximumNumberOfPermits;
        private int numberOfPermits;

        public Window(String country, int year, int maximumNumberOfPermits) {
            this.country = country;
            this.year = year;
            this.maximumNumberOfPermits = maximumNumberOfPermits;
        }

        public String getCountry() {
            return country;
        }

        public int getYear() {
            return year;
        }

        public int getMaximumNumberOfPermits() {
            return maximumNumberOfPermits;
        }

        public int getNumberOfPermits() {
            return numberOfPermits;
        }

        public Window setNumberOfPermits(int numberOfPermits) {
            this.numberOfPermits = numberOfPermits;
            return this;
        }
    }

    public static class QuickActions {

        private static final String CANCEL = "#menu-irhp-application-quick-actions-cancel";

        public static boolean hasCancel() {
            return isElementPresent(CANCEL, SelectorType.CSS);
        }
    }

    public static class Decisions {

        private static final String SUBMIT = "#menu-irhp-application-decisions-submit";

        public static void submit() {
            scrollAndClick(SUBMIT);
        }

        public static boolean hasSubmit() {
            return isElementPresent(SUBMIT, SelectorType.CSS);
        }
    }

}
