package org.dvsa.testing.framework.pageObjects.external.pages;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import java.util.concurrent.TimeUnit;

public class CookiesPage extends BasePage {
    private static String ACCEPT_ALL_COOKIES_BUTTON = "//button[@class='gem-c-button govuk-button gem-c-button--inline']";
    private static String COOKIES_BANNER = "//span[@class='govuk-heading-m']";
    private static String COOKIES_CONFIRMED = "//p[@class='gem-c-cookie-banner__confirmation-message']";
    private static String HIDE_BANNER = "//button[@class='gem-c-cookie-banner__hide-button']";
    private static String COOKIES_DETAILS_PAGE = "//a[@class='gem-c-button govuk-button gem-c-button--inline']";
    private static String COOKIES_DETAILS_PAGE_LINK = "//a[@class='govuk-link']";


    public static void selectAllCookies() {
        untilElementIsPresent(COOKIES_BANNER, SelectorType.XPATH, 30, TimeUnit.SECONDS);
        scrollAndClick(ACCEPT_ALL_COOKIES_BUTTON, SelectorType.XPATH);
        untilElementIsPresent(COOKIES_CONFIRMED, SelectorType.XPATH, 30, TimeUnit.SECONDS);
        scrollAndClick(HIDE_BANNER, SelectorType.XPATH);
    }

    public static void goToCookiesDetailsPage() {
        untilElementIsPresent(COOKIES_DETAILS_PAGE, SelectorType.XPATH, 30, TimeUnit.SECONDS);
        scrollAndClick(COOKIES_DETAILS_PAGE, SelectorType.XPATH);
        untilElementIsPresent("//h1[contains(@class,'gem-c-title__text')]", SelectorType.XPATH, 30, TimeUnit.SECONDS);
    }

    public static void goToCookiesDetailsPageFromLink() {
        untilElementIsPresent(COOKIES_DETAILS_PAGE_LINK, SelectorType.XPATH, 30, TimeUnit.SECONDS);
        scrollAndClick(COOKIES_DETAILS_PAGE_LINK, SelectorType.XPATH);
        untilElementIsPresent("//h1[contains(@class,'gem-c-title__text')]", SelectorType.XPATH, 30, TimeUnit.SECONDS);
    }

    public static boolean cookiePageTextPresent() {
        return isTextPresent("Cookies are files saved on your phone, tablet or computer when you visit a website.");
    }

    public static boolean cookieSaveTextPresent() {
        return isTextPresent("Government services may set additional cookies and, if so, will have their own cookie policy and banner.");
    }

    public static boolean cookiesBannerIsNotPresent() {
        return isElementNotPresent(COOKIES_BANNER, SelectorType.XPATH) &&
        isElementNotPresent(ACCEPT_ALL_COOKIES_BUTTON, SelectorType.XPATH);
    }

    public static void cookiesBannerIsPresent() {
        untilElementIsPresent(COOKIES_BANNER, SelectorType.XPATH,30, TimeUnit.SECONDS);
        untilElementIsPresent(ACCEPT_ALL_COOKIES_BUTTON, SelectorType.XPATH,30, TimeUnit.SECONDS);
    }

}
