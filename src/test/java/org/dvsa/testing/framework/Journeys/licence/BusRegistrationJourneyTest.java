package org.dvsa.testing.framework.Journeys.licence;

import activesupport.driver.Browser;
import activesupport.dates.Dates;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Injectors.World;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BusRegistrationJourneyTest {
    @After
    public void clearDriver() {
        Browser.removeLocalDriverThread();
    }

    @Test
    public void replacesInheritedServiceDetailsInsteadOfAppending() throws Exception {
        Map<String, String> fields = new HashMap<>(Map.of(
                "serviceNo", "459",
                "startPoint", "North Terminal",
                "finishPoint", "Lakeside Centre",
                "via", "Cedar Avenue"));
        BusRegistrationJourney journey = variationJourney(fields);
        Map<String, String> updatedDetails = Map.of(
                "serviceNo", "460",
                "startPoint", "West Terminal",
                "finishPoint", "Hilltop Centre",
                "via", "King Street");

        journey.updateManualBusRegistrationVariation(updatedDetails);

        updatedDetails.forEach((field, value) -> assertEquals(value, fields.get(field)));
        assertEquals(updatedDetails, state(journey, "savedVariationServiceDetails"));
    }

    @Test
    public void savesGeneratedVariationNumberWithoutChangingTheOriginalOrFeatureData() throws Exception {
        Map<String, String> fields = new HashMap<>(Map.of("serviceNo", "459"));
        BusRegistrationJourney journey = variationJourney(fields);
        Map<String, String> original = Map.of("serviceNo", "459");
        var originalField = BusRegistrationJourney.class.getDeclaredField("originalManualBusServiceDetails");
        originalField.setAccessible(true);
        originalField.set(journey, original);
        Map<String, String> updatedDetails = Map.of(
                "serviceNo", "Generated",
                "startPoint", "West Terminal",
                "finishPoint", "Hilltop Centre",
                "via", "King Street");

        journey.updateManualShortNoticeBusRegistrationVariation(updatedDetails);

        assertTrue(fields.get("serviceNo").matches("[1-9][0-9]{2}"));
        assertNotEquals("459", fields.get("serviceNo"));
        assertEquals(fields.get("serviceNo"),
                ((Map<?, ?>) state(journey, "savedVariationServiceDetails")).get("serviceNo"));
        assertEquals("Generated", updatedDetails.get("serviceNo"));
        assertEquals(original, state(journey, "originalManualBusServiceDetails"));
        updatedDetails.forEach((field, value) -> {
            if (!field.equals("serviceNo")) {
                assertEquals(value, fields.get(field));
            }
        });
    }

    @Test
    public void verifiesReadOnlyOriginalWithoutRequestingItsHiddenFields() throws Exception {
        verificationJourney("North Terminal").verifyManualBusRegistrationVariationServiceDetails(
                Map.of("serviceNo", "Generated", "startPoint", "West Terminal",
                        "finishPoint", "Hilltop Centre", "via", "King Street"));
    }

    @Test
    public void rejectsChangedServiceDetailsOnTheReadOnlyOriginal() throws Exception {
        BusRegistrationJourney journey = verificationJourney("Changed terminal");

        AssertionError failure = assertThrows(AssertionError.class,
                () -> journey.verifyManualBusRegistrationVariationServiceDetails(
                        Map.of("serviceNo", "Generated", "startPoint", "West Terminal",
                                "finishPoint", "Hilltop Centre", "via", "King Street")));

        assertTrue(failure.getMessage().contains("Start point"));
    }

    @Test
    public void acceptsPaddedAndUnpaddedCancellationDateParts() throws Exception {
        cancellationJourney("01", "01").verifyManualBusCancellationDatesAreSaved();
        cancellationJourney("1", "1").verifyManualBusCancellationDatesAreSaved();
    }

    @Test
    public void rejectsDifferentCancellationDayOrMonth() throws Exception {
        BusRegistrationJourney wrongDay = cancellationJourney("02", "01");
        assertThrows(AssertionError.class, wrongDay::verifyManualBusCancellationDatesAreSaved);
        BusRegistrationJourney wrongMonth = cancellationJourney("01", "02");
        assertThrows(AssertionError.class, wrongMonth::verifyManualBusCancellationDatesAreSaved);
    }

    private BusRegistrationJourney cancellationJourney(String day, String month) throws Exception {
        Map<String, String> fields = new HashMap<>();
        for (String date : new String[]{"receivedDate", "effectiveDate", "applicationCompleteDate"}) {
            fields.put(date + "_day", day);
            fields.put(date + "_month", month);
            fields.put(date + "_year", "2026");
        }
        for (String part : new String[]{"day", "month", "year"}) {
            fields.put("endDate_" + part, "");
        }
        BusRegistrationJourney journey = variationJourney(fields);
        journey.world.globalMethods.date = new Dates(() -> new org.joda.time.LocalDate(2026, 1, 1));
        var datesField = BusRegistrationJourney.class.getDeclaredField("manualCancellationDates");
        datesField.setAccessible(true);
        datesField.set(journey, Map.of(
                "dateReceived", "Today", "dateEffective", "Today", "applicationCompleteDate", "Today"));
        return journey;
    }

    private BusRegistrationJourney verificationJourney(String actualOriginalStartPoint) throws Exception {
        String originalUrl = "https://example.test/licence/1/bus/1/details/service/";
        String variationUrl = "https://example.test/licence/1/bus/2/details/service/";
        AtomicReference<String> currentUrl = new AtomicReference<>(variationUrl);
        Map<String, String> variation = Map.of(
                "serviceNo", "674", "startPoint", "West Terminal",
                "finishPoint", "Hilltop Centre", "via", "King Street",
                "fields[id]", "2", "fields[version]", "3");
        var originalPage = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                new ByteArrayInputStream("""
                        <form id="bus-service-number-and-type">
                          <ul class="definition-list readonly">
                            <li><dt>Service number</dt><dd>673</dd></li>
                            <li><dt>Start point</dt><dd>%s</dd></li>
                            <li><dt>Finish point</dt><dd>Lakeside Centre</dd></li>
                            <li><dt>Via</dt><dd>Cedar Avenue</dd></li>
                          </ul>
                        </form>
                        """.formatted(actualOriginalStartPoint).getBytes(StandardCharsets.UTF_8)));
        var xpath = XPathFactory.newInstance().newXPath();
        TestBrowser.use((WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(), new Class<?>[]{WebDriver.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "get" -> {
                        currentUrl.set((String) args[0]);
                        yield null;
                    }
                    case "getCurrentUrl" -> currentUrl.get();
                    case "findElement" -> {
                        String selector = args[0].toString();
                        String field = selector.substring(selector.indexOf(": ") + 2);
                        if (currentUrl.get().equals(originalUrl) && selector.startsWith("By.id:")) {
                            assertNotEquals("The read-only original has no hidden version field",
                                    "fields[version]", field);
                            throw new NoSuchElementException(field);
                        }
                        if (currentUrl.get().equals(originalUrl) && field.startsWith("//form")) {
                            Node node = (Node) xpath.evaluate(field, originalPage, XPathConstants.NODE);
                            if (node == null) {
                                throw new NoSuchElementException(field);
                            }
                            yield (WebElement) Proxy.newProxyInstance(
                                    WebElement.class.getClassLoader(), new Class<?>[]{WebElement.class},
                                    (element, operation, parameters) -> switch (operation.getName()) {
                                        case "isDisplayed" -> true;
                                        case "getText" -> node.getTextContent();
                                        default -> throw new UnsupportedOperationException(operation.getName());
                                    });
                        }
                        yield element(variation, field);
                    }
                    default -> throw new UnsupportedOperationException(method.getName());
                }));
        BusRegistrationJourney journey = new BusRegistrationJourney(new World());
        Map<String, Object> initialState = Map.of(
                "originalBusServiceDetailsUrl", originalUrl,
                "originalBusRegistrationId", "1",
                "originalManualBusServiceDetails", Map.of(
                        "serviceNo", "673", "startPoint", "North Terminal",
                        "finishPoint", "Lakeside Centre", "via", "Cedar Avenue"),
                "variationServiceDetailsUrl", variationUrl,
                "variationRegistrationId", "2",
                "savedVariationRegistrationVersion", "3",
                "savedVariationServiceDetails", variation);
        for (var entry : initialState.entrySet()) {
            var field = BusRegistrationJourney.class.getDeclaredField(entry.getKey());
            field.setAccessible(true);
            field.set(journey, entry.getValue());
        }
        return journey;
    }

    private BusRegistrationJourney variationJourney(Map<String, String> fields) throws Exception {
        TestBrowser.use((WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(), new Class<?>[]{WebDriver.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findElement")) {
                        String selector = args[0].toString();
                        String field = selector.substring(selector.indexOf(": ") + 2);
                        return element(fields, field);
                    }
                    throw new UnsupportedOperationException(method.getName());
                }));
        World world = new World();
        world.globalMethods = new GlobalMethods(world);
        BusRegistrationJourney journey = new BusRegistrationJourney(world);
        var urlField = BusRegistrationJourney.class.getDeclaredField("variationServiceDetailsUrl");
        urlField.setAccessible(true);
        urlField.set(journey, "https://example.test/licence/1/bus/2/details/service/");
        return journey;
    }

    private WebElement element(Map<String, String> fields, String field) {
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(), new Class<?>[]{WebElement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isDisplayed", "isEnabled" -> true;
                    case "clear" -> {
                        fields.put(field, "");
                        yield null;
                    }
                    case "sendKeys" -> {
                        fields.merge(field, String.join("", (CharSequence[]) args[0]), String::concat);
                        yield null;
                    }
                    case "click" -> null;
                    case "getAttribute" -> fields.getOrDefault(field, "1");
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    private Object state(BusRegistrationJourney journey, String name) throws Exception {
        var field = BusRegistrationJourney.class.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(journey);
    }

    private static class TestBrowser extends Browser {
        static void use(WebDriver driver) {
            threadLocalDriver.set(driver);
        }
    }
}
