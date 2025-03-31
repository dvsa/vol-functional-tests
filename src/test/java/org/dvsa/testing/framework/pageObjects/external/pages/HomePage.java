package org.dvsa.testing.framework.pageObjects.external.pages;

import org.apache.commons.lang3.StringUtils;
import org.dvsa.testing.framework.pageObjects.PermitApplication;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomePage extends BasePage {

    private static final String APPLY_FOR_LICENCE_BUTTON = "//*/a[contains(text(), 'Apply for a')]";

    private static final String TAB_TEMPLATE = "//*/div[@class='govuk-tabs']//a[contains(text(), '%s')]";


    public static void selectTab(Tab tab) {
        refreshPageWithJavascript();
        String selector = String.format(TAB_TEMPLATE, tab.toString());
        untilElementIsPresent(selector, SelectorType.XPATH, BasePage.WAIT_TIME_SECONDS, TimeUnit.SECONDS);
        click(selector, SelectorType.XPATH);
    }

    public static void applyForLicenceButton() {
        scrollAndClick(APPLY_FOR_LICENCE_BUTTON, SelectorType.XPATH);
    }

    public static void untilOnPage() {
        waitForElementToBePresent(APPLY_FOR_LICENCE_BUTTON);
    }

    public static boolean isTabPresent(Tab tab) {
        String selector = String.format(TAB_TEMPLATE, tab.toString());
        untilOnPage();
        return isElementPresent(selector, SelectorType.XPATH);
    }

    public static class PermitsTab {

        private static final String TABLE_ROW = "//tbody//tr";
        private static final String REFERENCE_NUMBER_TEMPLATE = "//td[@data-heading='Licence number']";
        private static final String REFERENCE_NUMBER = ".//td[@data-heading='Application reference']//span | .//td[@data-heading='Application reference']";
        private static final String NO_OF_PERMITS = ".//td[@data-heading='Number of permits']";
        private static final String TYPE = ".//td[@data-heading='Type']";
        private static final String STATUS = ".//td[@data-heading='Status']//strong";
        public static String RESOURCE = "/permits";

        public static void untilOnPage() {
            untilUrlMatches(RESOURCE, Duration.LONG, ChronoUnit.SECONDS);
        }

        public static String getPageHeading() {
            return getText("//h1[@class='js-title']", SelectorType.XPATH).trim();
        }

        public static void waitUntilOnGoingApplications() {
            untilElementIsPresent("//h2[contains(text(),'Ongoing applications')]",SelectorType.XPATH, 10, TimeUnit.SECONDS);
        }

        public static void waitUntilIssuedPermitsAndCertificatesHeading() {
            untilElementIsPresent("//h2[contains(text(),'Issued permits and certificates')]",SelectorType.XPATH, 10, TimeUnit.SECONDS);
        }

        public static boolean isAnyPermitWithStatus(@NotNull PermitStatus status) {
            List<WebElement> rows = findAll(TABLE_ROW, SelectorType.XPATH);
            return rows.stream().map((row) -> PermitStatus.getEnum(row.findElement(By.xpath(STATUS)).getText()))
                    .anyMatch((statusEnum) -> statusEnum == status);
        }

        public static void selectFirstValidPermit() {
            long kickoutTime = System.currentTimeMillis() + 20000;
            do {
                refreshPageWithJavascript();
            } while(!getText("//*//tr//th", SelectorType.XPATH).toLowerCase().contains("licence number") && System.currentTimeMillis() < kickoutTime);
            waitAndClick("span.overview__link--underline", SelectorType.CSS);
        }

        public static void selectFirstOngoingApplication() {
            scrollAndClick(REFERENCE_NUMBER, SelectorType.XPATH);
        }

        public static List<PermitApplication> getPermitsWithStatus(Table table, @NotNull PermitStatus status) {
            List<WebElement> rows = findAll(TABLE_ROW, SelectorType.XPATH);
            return permitApplications(table).stream()
                    .filter(permit -> permit.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        public static List<PermitApplication> permitApplications(Table table) {
            List<WebElement> rows = findAll(table.toString() + TABLE_ROW, SelectorType.XPATH);
            return rows.stream().map((el) -> {
                        String numOfPermitsText = el.findElement(By.xpath(NO_OF_PERMITS)).getText();
                        Integer numOfPermits = StringUtils.isBlank(numOfPermitsText) ? null : Integer.valueOf(numOfPermitsText);

                        return new PermitApplication()
                                .withReferenceNumber(el.findElement(By.xpath(REFERENCE_NUMBER)).getText())
                                .withNoOfPermits(numOfPermits)
                                .withType(el.findElement(By.xpath(TYPE)).getText())
                                .withStatus(el.findElement(By.xpath(STATUS)).getText());
                    }
            ).collect(Collectors.toList());
        }


        public static void untilPermitHasStatus(String reference, PermitStatus status, long duration, TimeUnit unit) {
            String selector = String.format("//td[text()='%s']/following-sibling::td[last()]/span[text()='%s']", reference, status.toString());
            untilElementIsPresent(selector, SelectorType.XPATH, duration, unit);
        }

        public enum Table {
            issued("(//table)[1]"),
            ongoing("(//table)[count(//table)]");

            String type;

            Table(String type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return type;
            }
        }

    }

    public static class FeesTab {

        public static void selectAllOutstandingFees() {
            String selector = "//th[last()]/input";
            boolean isNotSelected = !findElement(selector, SelectorType.XPATH).isSelected();
            if (isNotSelected) {
                scrollAndClick(selector, SelectorType.XPATH);
            }
        }

        public static void payNowButton() {
            waitAndClick("//button[@id='form-actions[pay]']",SelectorType.XPATH);
        }

        public static void pay() {
            scrollAndClick("#pay");
        }

        public static boolean areOutstandingFeesPresent() {
            return !isTextPresent("There are currently no outstanding fees to pay");
        }

    }

}
