package org.dvsa.testing.framework.Journeys.licence;

import activesupport.driver.Browser;
import org.dvsa.testing.framework.Injectors.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GovSignInJourneyTest {
    @AfterEach
    public void clearDriver() {
        Browser.removeLocalDriverThread();
    }

    @Test
    public void waitsForRedirectAfterSubmittingMockSignIn() {
        List<String> actions = new ArrayList<>();
        AtomicInteger urlChecks = new AtomicInteger();
        WebElement element = (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(), new Class<?>[]{WebElement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isDisplayed", "isEnabled" -> true;
                    case "sendKeys" -> {
                        actions.add("enter email");
                        yield null;
                    }
                    case "click" -> {
                        actions.add("continue");
                        yield null;
                    }
                    default -> throw new UnsupportedOperationException(method.getName());
                });
        TestBrowser.use((WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(), new Class<?>[]{WebDriver.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findElement" -> {
                        String selector = args[0].toString();
                        if (selector.contains("Sign in") || selector.equals("By.id: email")
                                || selector.equals("By.id: sign-in-button")) {
                            yield element;
                        }
                        throw new NoSuchElementException(selector);
                    }
                    case "getCurrentUrl" -> {
                        int check = urlChecks.incrementAndGet();
                        if (check > 1) {
                            assertEquals(List.of("enter email", "continue"), actions);
                        }
                        yield check < 4 ? "https://mock.example/sign-in" : "https://vol.example/application";
                    }
                    default -> throw new UnsupportedOperationException(method.getName());
                }));

        new GovSignInJourney(new World()).completeMockSignIn("test@example.com");

        assertTrue(urlChecks.get() >= 4, "Sign-in should wait for the redirect to complete");
        assertEquals(List.of("enter email", "continue"), actions);
    }

    private static class TestBrowser extends Browser {
        static void use(WebDriver driver) {
            threadLocalDriver.set(driver);
        }
    }
}
