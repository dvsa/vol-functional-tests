package org.dvsa.testing.framework.pageObjects.Driver;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

import static org.dvsa.testing.framework.runner.Hooks.getBrowser;

public class DriverUtils {


    public static void deleteCookies() {
        getBrowser().get().manage().deleteAllCookies();
    }

    public static void refreshPage() {
        getBrowser().get().navigate().refresh();
    }

    public static By by(@NotNull String selector, @NotNull SelectorType selectorType) {
        By bySelector;

        switch (selectorType) {
            case CSS:
                bySelector = By.cssSelector(selector);
                break;
            case XPATH:
                bySelector = By.xpath(selector);
                break;
            case NAME:
                bySelector = By.name(selector);
                break;
            case ID:
                bySelector = By.id(selector);
                break;
            case LINKTEXT:
                bySelector = By.linkText(selector);
                break;
            case PARTIALLINKTEXT:
                bySelector = By.partialLinkText(selector);
                break;
            default:
                throw new IllegalArgumentException("Only CSS, ID, NAME, XPATH, LINKTEXT and PARTIALLINKTEXT selector types are allowed");
        }
        return bySelector;
    }

    public static WebElement findElement(@NotNull String selector, @NotNull SelectorType selectorType) {
        return getBrowser().get().findElement(by(selector, selectorType));
    }

    public static WebElement findElement(@NotNull String selector, @NotNull SelectorType selectorType, long timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(getBrowser().get(), timeOutInSeconds);
        wait.until(ExpectedConditions.presenceOfElementLocated(by(selector, selectorType)));

        return findElement(selector, selectorType);
    }

    public static List<WebElement> findElements(@NotNull String selector, @NotNull SelectorType selectorType) {
        return getBrowser().get().findElements(by(selector, selectorType));
    }

    public static String getCurrentUrl() {
        return getBrowser().get().getCurrentUrl();
    }

    public static void get(String url) {
        getBrowser().get().get(url);
    }

    public static void switchToWindow(String windowHandle) {
        getBrowser().get().switchTo().window(windowHandle);
    }

    public static Set<String> getWindowHandles() {
        return getBrowser().get().getWindowHandles();
    }

    public static WebDriver.Options manage() {
        return getBrowser().get().manage();
    }

    public static void closeTab() {
        getBrowser().get().close();
    }

    public static String getTitle() {
        return getBrowser().get().getTitle();
    }
}