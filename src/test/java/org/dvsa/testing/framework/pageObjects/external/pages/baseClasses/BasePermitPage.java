package org.dvsa.testing.framework.pageObjects.external.pages.baseClasses;

import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

public class BasePermitPage extends BasePage {

    private static String BACK = "//a[contains(text(), 'Back')]";
    protected static String INLINE_ERROR_TEXT = "//p[@class='govuk-error-message']";
    protected static String PAGE_HEADING = "//h1[contains(@class,'govuk-fieldset__heading') or contains(@class,'govuk-heading-xl')]";
    protected static String PAGE_SUBHEADING = "//h2[@class='govuk-heading-m']";
    protected static String PANEL_HEADING = "//h1[@class='govuk-panel__title']";
    private static String SAVE_AND_CONTINUE ="//*[@name='Submit[SubmitButton]' or @class='button govuk-button']";
    private static String SAVE_AND_RETURN_TO_OVERVIEW = "//*[@id='save-return-button' or contains(text(),'eturn to overview')]";
    protected static String TABLE_SECTION_TEMPLATE = "//dt[contains(text(), '%s')]/../dd";


    public static String getPageHeading() {
        return getText(PAGE_HEADING, SelectorType.XPATH).trim();
    }

    public static String getPanelHeading() {
        return getText(PANEL_HEADING, SelectorType.XPATH).trim();
    }

    public static String getReferenceFromPage() {
        return getText("span.govuk-caption-xl").trim();
    }

    public static void back() {
        untilElementIsPresent(BACK, SelectorType.XPATH, BasePermitPage.WAIT_TIME_SECONDS, TimeUnit.SECONDS);
        scrollAndClick(BACK, SelectorType.XPATH);
    }

    public static void clickReturnToOverview() {
        untilElementIsPresent(SAVE_AND_RETURN_TO_OVERVIEW, SelectorType.XPATH, BasePermitPage.WAIT_TIME_SECONDS, TimeUnit.SECONDS);
        scrollAndClick(SAVE_AND_RETURN_TO_OVERVIEW, SelectorType.XPATH);
    }

    public static void saveAndContinue() {
        untilElementIsPresent(SAVE_AND_CONTINUE, SelectorType.XPATH, BasePermitPage.WAIT_TIME_SECONDS, TimeUnit.SECONDS);
        scrollAndClick(SAVE_AND_CONTINUE, SelectorType.XPATH);
    }

    public static String getErrorText() {
        return getText(INLINE_ERROR_TEXT, SelectorType.XPATH).replace("Error:","").trim();
    }

    public static String getCountry() {
        return getText("//*[@class='govuk-caption-xl']", SelectorType.XPATH);
    }

    public static void hasInternationalAuthorisationGovGuidanceLink() {
        boolean linkExists = isElementPresent("//a[@href='https://www.gov.uk/guidance/international-authorisations-and-permits-for-road-haulage']", SelectorType.XPATH);
        Assert.assertTrue("Unable to find guidance notes link", linkExists);
    }

    public static String getTableSectionValue(FeeSection section) {
        return getText(String.format(TABLE_SECTION_TEMPLATE, section.toString()), SelectorType.XPATH).trim();
    }

    protected static String getTextFromRowElement(WebElement row, String rowHeading) {
        String selector = String.format("./td[@data-heading='%s']", rowHeading);
        return row.findElement(By.xpath(selector)).getText();
    }

}
