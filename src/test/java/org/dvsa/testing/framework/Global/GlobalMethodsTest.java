package org.dvsa.testing.framework.Global;

import activesupport.driver.Browser;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GlobalMethodsTest {
    @AfterEach
    public void clearDriver() {
        Browser.removeLocalDriverThread();
    }

    @Test
    public void signsInUsingVisibleLabelForHiddenDeclarationCheckbox() {
        verifySignIn(false, 1);
    }

    @Test
    public void doesNotToggleAnAlreadySelectedDeclaration() {
        verifySignIn(true, 0);
    }

    private void verifySignIn(boolean initiallySelected, int expectedLabelClicks) {
        AtomicBoolean selected = new AtomicBoolean(initiallySelected);
        AtomicInteger labelClicks = new AtomicInteger();
        AtomicBoolean submitted = new AtomicBoolean();
        WebElement checkbox = element(false, selected, () -> {
            throw new AssertionError("The hidden checkbox must not be clicked directly");
        });
        WebElement label = element(true, selected, () -> {
            labelClicks.incrementAndGet();
            selected.set(!selected.get());
        });
        WebElement submit = element(true, selected, () -> submitted.set(true));
        TestBrowser.use((WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(), new Class<?>[]{WebDriver.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findElement" -> {
                        String selector = args[0].toString();
                        if (selector.equals("By.id: declarationRead")) {
                            yield checkbox;
                        }
                        if (selector.equals("By.xpath: //label[@for='declarationRead']")) {
                            yield label;
                        }
                        if (selector.equals("By.xpath: //*[@id='auth.login.button']")) {
                            yield submit;
                        }
                        throw new NoSuchElementException(selector);
                    }
                    case "findElements" -> List.of(submit);
                    default -> throw new UnsupportedOperationException(method.getName());
                }));

        List<String> credentials = new ArrayList<>();
        GlobalMethods methods = new GlobalMethods(new World()) {
            @Override
            public void replaceText(String selector, SelectorType type, String value) {
                credentials.add(value);
            }
        };
        methods.signIn("user", "password");

        assertTrue(selected.get());
        assertEquals(expectedLabelClicks, labelClicks.get());
        assertEquals(List.of("user", "password"), credentials);
        assertTrue(submitted.get());
    }

    private WebElement element(boolean displayed, AtomicBoolean selected, Runnable click) {
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(), new Class<?>[]{WebElement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isDisplayed" -> displayed;
                    case "isEnabled" -> true;
                    case "isSelected" -> selected.get();
                    case "click" -> {
                        click.run();
                        yield null;
                    }
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    private static class TestBrowser extends Browser {
        static void use(WebDriver driver) {
            threadLocalDriver.set(driver);
        }
    }
}
