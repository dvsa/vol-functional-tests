package org.dvsa.testing.framework.Utils.Generic;

import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EnhancedWaitUtils extends BasePage {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration DEFAULT_POLLING = Duration.ofMillis(500);
    private static final Duration SHORT_WAIT = Duration.ofSeconds(5);

    public static void waitForPageReady() {
        waitForPageReady(DEFAULT_TIMEOUT);
    }

    public static void waitForPageReady(Duration timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeout);

        wait.until(driver -> {
            try {
                return ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState")
                        .equals("complete");
            } catch (Exception e) {
                return false;
            }
        });

        wait.until(driver -> {
            try {
                Boolean jQueryDefined = (Boolean) ((JavascriptExecutor) driver)
                        .executeScript("return typeof jQuery !== 'undefined'");

                if (jQueryDefined) {
                    Long activeConnections = (Long) ((JavascriptExecutor) driver)
                            .executeScript("return jQuery.active");
                    return activeConnections == 0;
                }
                return true;
            } catch (Exception e) {
                return true;
            }
        });

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static WebElement waitForClickableElement(String selector, SelectorType selectorType) {
        return waitForClickableElement(selector, selectorType, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForClickableElement(String selector, SelectorType selectorType, Duration timeout) {
        waitForPageReady(SHORT_WAIT);

        WebDriverWait wait = new WebDriverWait(getDriver(), timeout, DEFAULT_POLLING);
        By locator = createLocator(selector, selectorType);

        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

        scrollIntoView(element);

        wait.until(driver -> {
            try {
                element.isDisplayed();
                element.isEnabled();
                return true;
            } catch (Exception e) {
                return false;
            }
        });

        return element;
    }

    public static void safeClick(String selector, SelectorType selectorType) {
        safeClick(selector, selectorType, 3);
    }

    public static void safeClick(String selector, SelectorType selectorType, int maxAttempts) {
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                WebElement element = waitForClickableElement(selector, selectorType);

                try {
                    ((JavascriptExecutor) getDriver())
                            .executeScript("arguments[0].click();", element);
                    return;
                } catch (Exception jsError) {
                    element.click();
                    return;
                }

            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                lastException = e;
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(1000 * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        throw new RuntimeException(
                String.format("Failed to click element '%s' after %d attempts", selector, maxAttempts),
                lastException
        );
    }

    public static WebElement waitForElementPresent(String selector, SelectorType selectorType) {
        return waitForElementPresent(selector, selectorType, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForElementPresent(String selector, SelectorType selectorType, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeout);
        By locator = createLocator(selector, selectorType);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitForElementVisible(String selector, SelectorType selectorType) {
        return waitForElementVisible(selector, selectorType, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForElementVisible(String selector, SelectorType selectorType, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeout);
        By locator = createLocator(selector, selectorType);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForElementToDisappear(String selector, SelectorType selectorType) {
        waitForElementToDisappear(selector, selectorType, DEFAULT_TIMEOUT);
    }

    public static void waitForElementToDisappear(String selector, SelectorType selectorType, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeout);
        By locator = createLocator(selector, selectorType);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForUrlMatch(String urlPattern) {
        waitForUrlMatch(urlPattern, DEFAULT_TIMEOUT);
    }

    public static void waitForUrlMatch(String urlPattern, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeout);
        wait.until(driver -> driver.getCurrentUrl().matches(urlPattern));
        waitForPageReady(SHORT_WAIT);
    }

    public static void waitForPageTransition() {
        String currentUrl = getDriver().getCurrentUrl();

        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            wait.until(driver -> !driver.getCurrentUrl().equals(currentUrl));
        } catch (TimeoutException e) {
        }

        waitForPageReady();
    }

    private static void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) getDriver())
                    .executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", element);

            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static By createLocator(String selector, SelectorType selectorType) {
        return selectorType == SelectorType.XPATH
                ? By.xpath(selector)
                : By.cssSelector(selector);
    }

    public static void waitForTextToBePresentInElement(String selector, SelectorType selectorType, String text) {
        WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_TIMEOUT);
        By locator = createLocator(selector, selectorType);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
}