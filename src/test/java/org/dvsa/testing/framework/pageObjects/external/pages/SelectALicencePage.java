package org.dvsa.testing.framework.pageObjects.external.pages;

import activesupport.number.Int;
import activesupport.string.Str;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class SelectALicencePage extends BasePermitPage {

    protected static final String TITLE = "h1";
    protected static final String LICENCE_NTH = "(//*[@class='govuk-radios__item'])[%d]";
    protected static final String LICENCE_NTH_LABEL = LICENCE_NTH + "/label";
    protected static final String LICENCE_NTH_TYPE = LICENCE_NTH + "/div";
    protected static final String LICENCE_INDEX_TEMPLATE = LICENCE_NTH + "/input";
    protected static final String LICENCE_RADIOS = ".govuk-radios__item input[type=radio]";
    protected static final String SELECTED_LICENCE = "label[class*='selected'] input[type=radio]";

    protected static String LICENCE_WITH_TYPE_REGEX = "(?<=Permit application for licence ).+";

    final public static String RESOURCE = "(licence/|permits/licence/add/)";

    public static void untilOnPage() {
        untilUrlMatches("/permits/licence/add/\\d+/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static String getActivePermitMessage() {
        return getText(".govuk-warning-text__text").trim();
    }

    public static boolean hasSelectedLicence() {
        return isElementPresent(SELECTED_LICENCE, SelectorType.CSS);
    }

    public static void clickRandomLicence() {
        int index = Int.random(1, numberOfLicences());
        scrollAndClick(String.format(LICENCE_INDEX_TEMPLATE, index), SelectorType.XPATH);
    }

    public static int numberOfLicences() {
        return size(LICENCE_RADIOS, SelectorType.CSS);
    }

    public static String getLicenceNumber(int index) {
        String selector = String.format(LICENCE_NTH_LABEL, index);
        untilElementIsPresent(selector, SelectorType.XPATH, BasePage.WAIT_TIME_SECONDS, TimeUnit.SECONDS);
        return Str.find("\\w{2}\\d{7}", getText(selector, SelectorType.XPATH)).get();
    }

    public static void clickLicence(@NotNull String licenceNumber) {
        scrollAndClick(String.format("//*[contains(text(), '%s')]/../input[@type='radio']", licenceNumber), SelectorType.XPATH);
    }

    public static String getLicenceNumber() {
        return Str.find( "\\w{2}\\d{7}", getLicenceNumberWithType()).get();
    }

    public static String getLicenceNumberWithType() {
        untilUrlMatches(RESOURCE, Duration.LONG, ChronoUnit.SECONDS);
        untilElementIsPresent(TITLE, SelectorType.CSS, BasePage.WAIT_TIME_SECONDS, TimeUnit.SECONDS);
        return getText("//*[@for='licence']/../div", SelectorType.XPATH);
    }

    public static String getLicenceNumberWithType(int index) {
        String typeSelector = String.format(LICENCE_NTH_TYPE, index);
        String licenceSelector = String.format(LICENCE_NTH_LABEL, index);

        untilElementIsPresent(typeSelector, SelectorType.XPATH, BasePage.WAIT_TIME_SECONDS, TimeUnit.SECONDS);
        String licenceNumber = getText(licenceSelector, SelectorType.XPATH);
        String type = getText(typeSelector, SelectorType.XPATH);
        return licenceNumber + " " + type;
    }

    public static boolean isLicencePresent(String licenceNo) {
        return isTextPresent(licenceNo.trim());
    }
}
