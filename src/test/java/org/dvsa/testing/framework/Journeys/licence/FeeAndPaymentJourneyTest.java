package org.dvsa.testing.framework.Journeys.licence;

import activesupport.driver.Browser;
import org.dvsa.testing.framework.Injectors.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeeAndPaymentJourneyTest {
    @AfterEach
    public void clearDriver() {
        Browser.removeLocalDriverThread();
    }

    @Test
    public void paysAllGrantFeesUsingTheSelectAllLabel() throws Exception {
        var document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream("""
                <table>
                  <thead><tr><th><div class="govuk-checkboxes__item">
                    <input id="generated-select-all" type="checkbox" name="checkall"/>
                    <label for="generated-select-all"><span>Select all</span></label>
                  </div></th></tr></thead>
                  <tbody>
                    <tr><td data-heading="Fee amount">£257.00</td></tr>
                    <tr><td data-heading="Fee amount">£68.00</td></tr>
                  </tbody>
                </table>
                """.getBytes(StandardCharsets.UTF_8)));
        var xpath = XPathFactory.newInstance().newXPath();
        AtomicBoolean selected = new AtomicBoolean();
        List<String> actions = new ArrayList<>();
        WebElement feesLink = element(true, true, "Fees", () -> actions.add("open fees"));
        WebElement payButton = (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(), new Class<?>[]{WebElement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isDisplayed" -> true;
                    case "isEnabled" -> selected.get();
                    case "click" -> {
                        actions.add("pay");
                        yield null;
                    }
                    default -> throw new UnsupportedOperationException(method.getName());
                });
        TestBrowser.use((WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(), new Class<?>[]{WebDriver.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findElement" -> {
                        String selector = args[0].toString();
                        if (selector.equals("By.partialLinkText: Fees")) {
                            yield feesLink;
                        }
                        if (selector.equals("By.id: pay")) {
                            yield payButton;
                        }
                        if (selector.startsWith("By.xpath: ")) {
                            Element node = (Element) xpath.evaluate(selector.substring("By.xpath: ".length()),
                                    document, XPathConstants.NODE);
                            if (node != null && node.getTagName().equals("label")) {
                                yield element(true, true, "", () -> {
                                    selected.set(true);
                                    actions.add("select all");
                                });
                            }
                        }
                        throw new NoSuchElementException(selector);
                    }
                    case "findElements" -> {
                        assertEquals("By.cssSelector: td[data-heading='Fee amount']", args[0].toString());
                        yield List.of(element(true, true, "£257.00", () -> {}),
                                element(true, true, "£68.00", () -> {}));
                    }
                    default -> throw new UnsupportedOperationException(method.getName());
                }));

        FeeAndPaymentJourney journey = new FeeAndPaymentJourney(new World()) {
            @Override
            public void payFee(String amount, String paymentMethod) {
                actions.add("payment " + amount + " " + paymentMethod);
            }
        };
        journey.payGrantFees();

        assertEquals(List.of("open fees", "select all", "pay", "payment 325.00 cash"), actions);
    }

    private WebElement element(boolean displayed, boolean enabled, String text, Runnable click) {
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(), new Class<?>[]{WebElement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isDisplayed" -> displayed;
                    case "isEnabled" -> enabled;
                    case "getText" -> text;
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
