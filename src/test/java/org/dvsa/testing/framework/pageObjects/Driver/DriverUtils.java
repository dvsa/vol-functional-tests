package org.dvsa.testing.framework.pageObjects.Driver;

import activesupport.driver.Browser;
import activesupport.selfhealing.SelfHealingElementLocator;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class DriverUtils {

    public static WebDriver getDriver() {
        return Browser.navigate();
    }

    public static void deleteCookies() {
        getDriver().manage().deleteAllCookies();
    }

    public static void refreshPage() {
        getDriver().navigate().refresh();
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
        try {
            return getDriver().findElement(by(selector, selectorType));
        } catch (NoSuchElementException failure) {
            return SelfHealingElementLocator.heal(getDriver(), selector, selectorType.name())
                    .orElseThrow(() -> failure);
        }
    }

    public static WebElement findElement(@NotNull String selector, @NotNull SelectorType selectorType, long timeOutInSeconds) {
        return waitForElement(selector, selectorType, timeOutInSeconds,
                ExpectedConditions::presenceOfElementLocated, element -> driver -> element);
    }

    public static WebElement waitForVisibleElement(String selector, SelectorType selectorType, long timeoutInSeconds) {
        return waitForElement(selector, selectorType, timeoutInSeconds,
                ExpectedConditions::visibilityOfElementLocated, ExpectedConditions::visibilityOf);
    }

    public static WebElement waitForClickableElement(String selector, SelectorType selectorType, long timeoutInSeconds) {
        return waitForElement(selector, selectorType, timeoutInSeconds,
                ExpectedConditions::elementToBeClickable, ExpectedConditions::elementToBeClickable);
    }

    private static WebElement waitForElement(String selector, SelectorType selectorType, long timeoutInSeconds,
                                            Function<By, ExpectedCondition<WebElement>> condition,
                                            Function<WebElement, ExpectedCondition<WebElement>> healedCondition) {
        WebDriver driver = getDriver();
        By locator = by(selector, selectorType);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        try {
            return wait.until(condition.apply(locator));
        } catch (TimeoutException failure) {
            // Hidden or disabled elements are not broken selectors.
            if (!driver.findElements(locator).isEmpty()) {
                throw failure;
            }
            WebElement healed = SelfHealingElementLocator.heal(driver, selector, selectorType.name())
                    .orElseThrow(() -> failure);
            return wait.until(healedCondition.apply(healed));
        }
    }

    public static List<WebElement> findElements(@NotNull String selector, @NotNull SelectorType selectorType) {
        return getDriver().findElements(by(selector, selectorType));
    }

    public static String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public static void get(String url) {
        getDriver().get(url);
    }

    public static void switchToWindow(String windowHandle) {
        getDriver().switchTo().window(windowHandle);
    }

    public static void switchToIframe(String iframeId) {
        getDriver().switchTo().frame(iframeId);
    }

    public static void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
    }


    public static Set<String> getWindowHandles() {
        return getDriver().getWindowHandles();
    }

    public static WebDriver.Options manage() {
        return getDriver().manage();
    }

    public static void closeTab() {
        getDriver().close();
    }

    public static String getTitle() {
        return getDriver().getTitle();
    }
}