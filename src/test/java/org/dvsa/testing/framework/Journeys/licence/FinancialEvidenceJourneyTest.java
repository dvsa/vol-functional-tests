package org.dvsa.testing.framework.Journeys.licence;

import activesupport.driver.Browser;
import org.dvsa.testing.framework.Injectors.World;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FinancialEvidenceJourneyTest {
    @After
    public void clearDriver() {
        Browser.removeLocalDriverThread();
    }

    @Test
    public void waitsForFinancialAmountToBePresent() {
        AtomicInteger lookups = new AtomicInteger();
        WebElement amount = amountVisibleAfter(new AtomicInteger(), 0);
        TestBrowser.use((WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(), new Class<?>[]{WebDriver.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findElement")) {
                        if (lookups.incrementAndGet() < 3) {
                            throw new NoSuchElementException("Financial amount has not loaded");
                        }
                        return amount;
                    }
                    throw new UnsupportedOperationException(method.getName());
                }));

        assertEquals(7200, new FinancialEvidenceJourney(new World()).getFinancialValueFromPage());
        assertTrue("Should retry while the financial amount is absent", lookups.get() >= 3);
    }

    @Test
    public void waitsForFinancialAmountToBeVisible() {
        AtomicInteger visibilityChecks = new AtomicInteger();
        WebElement amount = amountVisibleAfter(visibilityChecks, 2);
        TestBrowser.use((WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(), new Class<?>[]{WebDriver.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findElement")) {
                        return amount;
                    }
                    throw new UnsupportedOperationException(method.getName());
                }));

        assertEquals(7200, new FinancialEvidenceJourney(new World()).getFinancialValueFromPage());
        assertTrue("Should wait until the financial amount is visible", visibilityChecks.get() > 2);
    }

    private WebElement amountVisibleAfter(AtomicInteger visibilityChecks, int hiddenChecks) {
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(), new Class<?>[]{WebElement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isDisplayed" -> visibilityChecks.incrementAndGet() > hiddenChecks;
                    case "getText" -> "\u00a3 7,200";
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    private static class TestBrowser extends Browser {
        static void use(WebDriver driver) {
            threadLocalDriver.set(driver);
        }
    }
}
