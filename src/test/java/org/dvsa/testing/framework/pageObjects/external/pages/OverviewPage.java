package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;

public class OverviewPage extends BasePermitPage {

    public static final String RESOURCE_URL = "permits/application/\\d+/";
    private static String ACTIVE_SECTION_STATUS_TEMPLATE = "//span[contains(text(), '%s')]/following-sibling::span";
    private static String ACTIVE_BILATERAL_SECTION_STATUS_TEMPLATE = "//span/a[contains(text(), '%s')]/../following-sibling::span";
    private static String DISABLED_SECTION_STATUS_TEMPLATE = "//div[contains(text(), '%s')]/span";
    private static String DISABLED_BILATERAL_SECTION_STATUS_TEMPLATE = "//span/a[contains(text(), '%s')]/../span";

    public static void untilOnPage() {
        untilUrlMatches(RESOURCE_URL, Duration.LONG, ChronoUnit.SECONDS);
    }

    public static String getPageHeading() {
        return getText("//h1[@class='js-title']", SelectorType.XPATH);
    }

    public static void clickOverviewSection(OverviewSection section) {
        scrollAndClick(String.format("//span[contains(text(),'%s')]", section.toString()), SelectorType.XPATH);
    }

    public static void clickBilateralOverviewSection(OverviewSection section) {
        scrollAndClick(String.format("//span/a[contains(text(),'%s')]", section.toString()), SelectorType.XPATH);
    }

    public static void clickCountrySection(Country country) {
        waitAndClick(String.format("//a[contains(text(),'%s')]", country.toString()), SelectorType.XPATH);
    }

    public static PermitStatus getStatusOfSection(@NotNull OverviewSection section) {
        String selector = org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage.isSectionActive(section) ? ACTIVE_SECTION_STATUS_TEMPLATE : DISABLED_SECTION_STATUS_TEMPLATE;
        String status = getText(String.format(selector, section), SelectorType.XPATH);
        return PermitStatus.getEnum(status);
    }

    public static boolean isSectionActive(@NotNull OverviewSection section) {
        return isElementPresent(String.format(ACTIVE_SECTION_STATUS_TEMPLATE, section), SelectorType.XPATH);
    }

    public static PermitStatus getBilateralStatusOfSection(OverviewSection section) {
        String selector = org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage.isBilateralSectionActive(section) ? ACTIVE_BILATERAL_SECTION_STATUS_TEMPLATE : DISABLED_BILATERAL_SECTION_STATUS_TEMPLATE;
        String status = getText(String.format(selector, section), SelectorType.XPATH);
        return PermitStatus.getEnum(status);

    }

    public static boolean isBilateralSectionActive(@NotNull OverviewSection section) {
        return isElementPresent(String.format(ACTIVE_BILATERAL_SECTION_STATUS_TEMPLATE, section), SelectorType.XPATH);
    }

    public static boolean isActiveLinkPresent(@NotNull OverviewSection section) {
        return isElementPresent(String.format("//*[contains(text(), '%s')]/parent::a", section), SelectorType.XPATH);
    }

    public static void clickCancelApplication() {
        scrollAndClick("//a[contains(text(), 'Cancel application')]", SelectorType.XPATH);
    }
}
