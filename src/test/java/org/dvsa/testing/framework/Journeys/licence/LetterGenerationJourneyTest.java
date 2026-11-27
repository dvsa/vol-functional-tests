package org.dvsa.testing.framework.Journeys.licence;

import activesupport.driver.Browser;
import org.dvsa.testing.framework.Injectors.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LetterGenerationJourneyTest {
    @AfterEach
    public void clearDriver() {
        Browser.removeLocalDriverThread();
    }

    @Test
    public void waitsForAjaxTemplateOptionsBeforeSelecting() {
        List<String> selected = new ArrayList<>();
        AtomicInteger templateChecks = new AtomicInteger();
        TestBrowser.use((WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(), new Class<?>[]{WebDriver.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findElement" -> {
                        String selector = args[0].toString();
                        if (selector.startsWith("By.name: ")) {
                            String field = selector.substring("By.name: ".length());
                            String option = switch (field) {
                                case "details[category]" -> "Application";
                                case "details[documentSubCategory]" -> "Application Letters";
                                case "details[documentTemplate]" -> "[New] First and Finals GB";
                                default -> throw new NoSuchElementException(selector);
                            };
                            yield select(option, selected);
                        }
                        if (selector.startsWith("By.xpath: //select[")) {
                            if (selector.contains("details[documentTemplate]")) {
                                assertEquals(List.of("Application", "Application Letters"), selected);
                                int attempt = templateChecks.incrementAndGet();
                                if (attempt == 1) {
                                    throw new StaleElementReferenceException("Modal was updated");
                                }
                                if (attempt == 2) {
                                    throw new NoSuchElementException("Template options still loading");
                                }
                            }
                            yield option("", selected);
                        }
                        throw new NoSuchElementException(selector);
                    }
                    default -> throw new UnsupportedOperationException(method.getName());
                }));

        new LetterGenerationJourney(new World()).selectFirstAndFinalRequestTemplate();

        assertTrue(templateChecks.get() >= 3, "The template option should be retried after a modal update");
        assertEquals(List.of("Application", "Application Letters", "[New] First and Finals GB"), selected);
    }

    private static WebElement select(String option, List<String> selected) {
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(), new Class<?>[]{WebElement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getTagName" -> "select";
                    case "isEnabled" -> true;
                    case "getCssValue" -> "visible";
                    case "getDomAttribute", "getAttribute" -> null;
                    case "findElements" -> {
                        assertTrue(args[0].toString().contains(option));
                        yield List.of(option(option, selected));
                    }
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    private static WebElement option(String text, List<String> selected) {
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(), new Class<?>[]{WebElement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isEnabled", "isDisplayed" -> true;
                    case "isSelected" -> false;
                    case "getCssValue" -> "visible";
                    case "click" -> {
                        selected.add(text);
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
