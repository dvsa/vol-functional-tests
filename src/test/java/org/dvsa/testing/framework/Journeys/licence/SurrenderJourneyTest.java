package org.dvsa.testing.framework.Journeys.licence;

import activesupport.driver.Browser;
import org.dvsa.testing.framework.Injectors.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SurrenderJourneyTest {
    @AfterEach
    public void clearDriver() {
        Browser.removeLocalDriverThread();
    }

    @Test
    public void fillsDiscDetailsUsingLabelsForGeneratedCheckboxIds() throws Exception {
        var document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream("""
                <form>
                  <input type="checkbox" name="stolenSection[stolen]" id="stolenSection-stolen-"/>
                  <label for="stolenSection-stolen-">Stolen</label>
                  <input type="checkbox" name="lostSection[lost]" id="lostSection-lost-"/>
                  <label for="lostSection-lost-">Lost</label>
                  <input type="checkbox" name="possessionSection[inPossession]" id="possessionSection-inPossession-"/>
                  <label for="possessionSection-inPossession-">In your possession</label>
                  <p>Number of discs stolen</p>
                  <input id="possessionSection[info][number]"/>
                  <input id="lostSection[info][number]"/>
                  <input id="lostSection[info][details]"/>
                  <input id="stolenSection[info][number]"/>
                  <input id="stolenSection[info][details]"/>
                  <button id="submit"/>
                </form>
                """.getBytes(StandardCharsets.UTF_8)));
        var xpath = XPathFactory.newInstance().newXPath();
        List<String> clicked = new ArrayList<>();
        List<String> entered = new ArrayList<>();
        TestBrowser.use((WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(), new Class<?>[]{WebDriver.class, JavascriptExecutor.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "executeScript" -> "complete";
                    case "findElement" -> {
                        String selector = args[0].toString().replaceFirst("^By.xpath: ", "");
                        Element node = (Element) xpath.evaluate(selector, document, XPathConstants.NODE);
                        if (node == null) {
                            throw new NoSuchElementException(selector);
                        }
                        yield (WebElement) Proxy.newProxyInstance(
                                WebElement.class.getClassLoader(), new Class<?>[]{WebElement.class},
                                (elementProxy, elementMethod, elementArgs) -> switch (elementMethod.getName()) {
                                    case "isDisplayed", "isEnabled" -> true;
                                    case "click" -> {
                                        clicked.add(node.hasAttribute("for") ? node.getAttribute("for") : node.getAttribute("id"));
                                        yield null;
                                    }
                                    case "sendKeys" -> {
                                        entered.add(node.getAttribute("id") + "=" + ((CharSequence[]) elementArgs[0])[0]);
                                        yield null;
                                    }
                                    default -> throw new UnsupportedOperationException(elementMethod.getName());
                                });
                    }
                    default -> throw new UnsupportedOperationException(method.getName());
                }));

        new SurrenderJourney(new World()).addDiscInformation();

        assertEquals(List.of("stolenSection-stolen-", "lostSection-lost-", "possessionSection-inPossession-", "submit"), clicked);
        assertEquals(List.of("possessionSection[info][number]=2", "lostSection[info][number]=2",
                "lostSection[info][details]=lost", "stolenSection[info][number]=1",
                "stolenSection[info][details]=stolen"), entered);
    }

    private static class TestBrowser extends Browser {
        static void use(WebDriver driver) {
            threadLocalDriver.set(driver);
        }
    }
}
