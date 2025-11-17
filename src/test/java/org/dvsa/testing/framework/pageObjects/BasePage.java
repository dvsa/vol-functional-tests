package org.dvsa.testing.framework.pageObjects;

import activesupport.driver.Browser;
import com.google.common.base.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.conditions.ElementCondition;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public abstract class BasePage extends DriverUtils {
    public static final int WAIT_TIME_SECONDS = 9;
    private static final int TIME_OUT_SECONDS = 11;
    private static final int POLLING_SECONDS = 1;
    private static final Logger LOGGER = LogManager.getLogger(BasePage.class);

    private static String selectedValue;

    protected static String getAttribute(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String attribute) {
        return findElement(selector, selectorType).getAttribute(attribute);
    }

    /**
     * This returns any text content that an element possesses.
     *
     * @param selector     This should be a CSS or XPATH selector which is used to identify which elements text is to be retrieved.
     * @param selectorType This is the type of selector that the argument selector is.
     * @return The specified elements text contents.
     */
    protected static String getText(@NotNull String selector, @NotNull SelectorType selectorType) {
        return findElement(selector, selectorType).getText();
    }

    protected static String waitAndGetText(@NotNull String selector, @NotNull SelectorType selectorType) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                var wait = new FluentWait<>(getDriver())
                        .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                        .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                        .ignoring(NoSuchElementException.class)
                        .ignoring(StaleElementReferenceException.class);

                WebElement element = wait.until(ExpectedConditions.visibilityOf(findElement(selector, selectorType)));
                return element.getText();
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("StaleElementReferenceException encountered. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            } catch (WebDriverException e) {
                LOGGER.warn("WebDriverException encountered. Attempting retry " + (attempt + 1));
            }
        }
        throw new RuntimeException("Failed to retrieve text after " + maxRetries + " attempts due to StaleElementReferenceException or WebDriverException.");
    }

    protected static String getText(@NotNull String selector) {
        return getText(selector, SelectorType.CSS);
    }

    protected static String getLink(@NotNull String selector, @NotNull SelectorType selectorType) {
        return findElement(selector, selectorType).getAttribute("href");
    }

    protected static String getLink(@NotNull String selector) {
        return getLink(selector, SelectorType.CSS);
    }

    public static String getTextFromNestedElement(WebElement webElement, String selector) {
        return webElement.findElement(By.xpath(selector)).getText();
    }

    protected static void untilElementWithText() {
        new FluentWait<>(getDriver())
                .ignoreAll(Arrays.asList(NoSuchElementException.class, StaleElementReferenceException.class))
                .withTimeout(Duration.of(org.dvsa.testing.framework.enums.Duration.CENTURY, ChronoUnit.SECONDS))
                .pollingEvery(Duration.of(500, ChronoUnit.MILLIS))
                .until(driver -> driver.findElement(by("//h1[@class='govuk-heading-xl']", SelectorType.XPATH)).getText().equalsIgnoreCase("Permit fee"));
    }

    protected static boolean isTextPresent(String locator) {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(1))
                    .until(driver -> visibilityOf(findElement(String.format("//*[contains(text(),'%s')]", locator), SelectorType.XPATH)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isErrorMessagePresent() {
        String ERROR_MESSAGE_HEADING = "Please correct the following errors";
        String ERROR_CLASS = ".govuk-error-message";
        return isTextPresent(ERROR_MESSAGE_HEADING) || isElementPresent(ERROR_CLASS, SelectorType.CSS);
    }

    protected static void scrollAndEnterField(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String text, boolean append) {
        WebElement field = findElement(selector, selectorType);
        if (!append) {
            field.clear();
        }
        new Actions(getDriver()).moveToElement(field).sendKeys(field, text).perform();
    }

    public String getNoButtonId() {
        WebElement noButton = Browser.navigate().findElement(By.xpath("//*[@id=\"responsibilities\"]//input[@value='N']"));
        return noButton.getAttribute("id");
    }
    protected static void scrollAndEnterField(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String text) {
        scrollAndEnterField(selector, selectorType, text, false);
    }

    protected static void scrollAndEnterField(@NotNull String selector, @NotNull String text) {
        scrollAndEnterField(selector, SelectorType.CSS, text);
    }

    protected static void waitAndClickByLinkText(@NotNull String selector) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                var wait = new FluentWait<>(getDriver())
                        .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                        .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                        .ignoring(NoSuchElementException.class)
                        .ignoring(StaleElementReferenceException.class)
                        .ignoring(ElementClickInterceptedException.class);

                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                        by(selector, SelectorType.PARTIALLINKTEXT)
                ));
                element.click();
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("StaleElementReferenceException encountered. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            } catch (WebDriverException e) {
                LOGGER.warn("WebDriverException encountered. Attempting retry " + (attempt + 1));
            }
        }
        throw new RuntimeException("Failed to click element after " + maxRetries + " attempts due to StaleElementReferenceException or WebDriverException.");
    }

    protected static void clickByFullLinkText(@NotNull String selector) {
        findElement(selector, SelectorType.LINKTEXT).click();
    }

    public void enterTextIntoSearchBox(String text) {
        String selector = "//div[@class='chosen-search']/input[@type='text' and @autocomplete='off']";
        waitForElementToBePresent(selector);
        WebElement searchBox = findElement(selector, SelectorType.XPATH);
        searchBox.clear();
        searchBox.sendKeys(text);
    }

    protected static void clickByXPath(@NotNull String selector) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                findElement(selector, SelectorType.XPATH).click();
                return;
            } catch (StaleElementReferenceException | ElementNotInteractableException e) {
                LOGGER.warn(e.getClass().getSimpleName() + " encountered. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            }
        }
        throw new RuntimeException("Failed to click element after " + maxRetries + " attempts due to StaleElementReferenceException or ElementNotInteractableException.");
    }

    protected static void clickById(@NotNull String selector) {
        findElement(selector, SelectorType.ID).click();
    }

    protected static void clickByName(@NotNull String selector) {
        findElement(selector, SelectorType.NAME).click();
    }

    protected static void clickByCSS(@NotNull String selector) {
        findElement(selector, SelectorType.CSS).click();
    }

    protected static void selectValueFromDropDown(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String listValue) {
        var selectItem = new Select(findElement(selector, selectorType));
        selectItem.selectByVisibleText(listValue);
    }

    protected static void selectValueFromDropDownByIndex(@NotNull String selector, @NotNull SelectorType selectorType, int listValue) {
        var selectItem = new Select(findElement(selector, selectorType));
        selectItem.selectByIndex(listValue);
    }

    public void cycleThroughPaginationUntilElementIsDisplayed(String selector) {
        var pagination = getDriver().findElements(By.xpath("//ul[@class='govuk-pagination__list']"));
        var paginationCount = pagination.size();

        while (!isElementPresent(selector, SelectorType.LINKTEXT)) {
            for (int i = 0; i < paginationCount; i++) {
                if (isElementPresent(selector, SelectorType.LINKTEXT)) {
                    break;
                }
                scrollAndClick("Next", SelectorType.LINKTEXT);
            }
        }
    }

    private static By createBySelector(String selector, SelectorType selectorType) {
        return switch (selectorType) {
            case ID -> By.id(selector);
            case NAME -> By.name(selector);
            case XPATH -> By.xpath(selector);
            case CSS -> By.cssSelector(selector);
            default -> throw new IllegalArgumentException("Unsupported SelectorType: " + selectorType);
        };
    }

    public static String selectRandomValueFromDropDown(@NotNull String selector, @NotNull SelectorType selectorType) {
        var bySelector = createBySelector(selector, selectorType);
        var select = new Select(getDriver().findElement(bySelector));
        var options = select.getOptions();
        var randomOption = options.get(new Random().nextInt(options.size()));
        selectedValue = randomOption.getText();
        select.selectByVisibleText(selectedValue);
        return selectedValue;
    }
    public static String getSelectedValue() {
        return selectedValue;
    }

    public static void setSelectedValue(String selectedValue) {
        BasePage.selectedValue = selectedValue;
    }

    public static boolean isLinkPresent(String locator, int duration) {
        var wait = new FluentWait<>(getDriver())
                .withTimeout(ofSeconds(duration))
                .pollingEvery(ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        try {
            wait.until(driver -> wait.until(ExpectedConditions.visibilityOf(
                    findElement(locator, SelectorType.PARTIALLINKTEXT))));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void selectRandomCheckBoxOrRadioBtn(String typeArgument) {
        var checkboxes = findElements(String.format("//input[@type='%s']", typeArgument), SelectorType.XPATH);
        var index = (int) (Math.random() * checkboxes.size());
        checkboxes.get(index).click();
    }

    public void tickCheckbox(String checkboxXPath) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                WebElement checkbox = getDriver().findElement(By.xpath(checkboxXPath));
                if (!checkbox.isSelected()) {
                    checkbox.click();
                }
                return;
            } catch (NoSuchElementException e) {
                LOGGER.warn("NoSuchElementException encountered. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            }
        }
        throw new RuntimeException("Failed to locate and click checkbox after " + maxRetries + " attempts.");
    }

    public void selectRandomRadioBtn() {
        var radioButtons = findElements("//tr//*[@type='radio']", SelectorType.XPATH);
        var result = radioButtons.stream().findAny();
        result.ifPresent(WebElement::click);
    }

    protected static boolean isTitlePresent(String locator, int duration) {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(duration))
                    .until(driver -> visibilityOf(findElement(String.format("//h1[contains(text(),\"%s\")]", locator), SelectorType.XPATH)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected static void click(@NotNull String selector, @NotNull SelectorType selectorType) {
        findElement(selector, selectorType).click();
    }

    protected static void scrollAndClick(@NotNull String selector, @NotNull SelectorType selectorType) {
        try {
            new Actions(getDriver())
                    .moveToElement(findElement(selector, selectorType))
                    .click()
                    .perform();
        } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
            if (isElementPresent(selector, selectorType)) {
                new Actions(getDriver())
                        .moveToElement(findElement(selector, selectorType))
                        .click()
                        .perform();
            } else {
                throw new NoSuchElementException("Element was removed from the DOM and not replaced");
            }
        }
    }

    protected static void scrollAndClick(@NotNull String selector) {
        scrollAndClick(selector, SelectorType.CSS);
    }

    protected static String nameAttribute(@NotNull String element, @NotNull String value) {
        return String.format("%s[%s=\"%s\"]", element, "name", value);
    }

    protected static List<WebElement> findAll(@NotNull String selector, @NotNull SelectorType selectorType) {
        return findElements(selector, selectorType);
    }

    protected static int size(@NotNull String selector, @NotNull SelectorType selectorType) {
        return findElements(selector, selectorType).size();
    }

    protected static boolean isElementNotPresent(@NotNull String selector, SelectorType selectorType) {
        return !isElementPresent(selector, selectorType);
    }

    protected static boolean isElementPresent(@NotNull String selector, SelectorType selectorType) {
        try {
            findElement(selector, selectorType);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected static boolean isElementSelected(@NotNull String selector, SelectorType selectorType) {
        return findElement(selector, selectorType).isSelected();
    }

    protected static boolean isElementVisible(@NotNull String selector, long duration) {
        try {
            untilVisible(selector, SelectorType.XPATH, duration, TimeUnit.SECONDS);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public static void untilVisible(@NotNull String selector, @NotNull SelectorType selectorType, long duration, TimeUnit timeUnit) {
        var by = by(selector, selectorType);
        until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void until(ExpectedCondition<?> expectedCondition) {
        new FluentWait<>(getDriver())
                .withTimeout(ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class)
                .until(expectedCondition);
    }

    public static void untilElementIsPresent(@NotNull String selector, SelectorType selectorType, long duration, TimeUnit timeUnit) {
        var wait = new FluentWait<>(getDriver())
                .withTimeout(ofSeconds(duration))
                .pollingEvery(ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        try {
            wait.until(driver -> ElementCondition.isPresent(selector, selectorType));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void untilElementIsPresent(@NotNull String selector, long duration, TimeUnit timeUnit) {
        untilElementIsPresent(selector, SelectorType.CSS, duration, timeUnit);
    }

    public static void waitForElementNotToBePresent(@NotNull String selector) {
        new WebDriverWait(Browser.navigate(), Duration.ofSeconds(15), Duration.ofSeconds(20))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(selector)));
    }

    public static void tryUntilElementIsPresent(@NotNull String selector, SelectorType selectorType, long duration, TimeUnit timeUnit) {
        try {
            untilElementIsPresent(selector, selectorType, duration, timeUnit);
        } catch (WebDriverException ex) {
            ex.printStackTrace();
        }
    }

    public static void untilElementIsNotPresent(@NotNull String selector, SelectorType selectorType) {
        until(ElementCondition.absenceOfElement(selector, selectorType));
    }

    public static String waitAndGetElementValueByText(@NotNull String selector, @NotNull SelectorType selectorType) {
        return getText(selector, selectorType);
    }

    public static String waitAndwaitAndGetElementValueByText(@NotNull String selector, @NotNull SelectorType selectorType) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                var wait = new FluentWait<>(getDriver())
                        .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                        .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                        .ignoring(NoSuchElementException.class)
                        .ignoring(StaleElementReferenceException.class);

                WebElement element = wait.until(ExpectedConditions.visibilityOf(findElement(selector, selectorType)));
                return element.getText();
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("StaleElementReferenceException encountered. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            } catch (WebDriverException e) {
                LOGGER.warn("WebDriverException encountered. Attempting retry " + (attempt + 1));
            }
        }
        throw new RuntimeException("Failed to retrieve text after " + maxRetries + " attempts due to StaleElementReferenceException or WebDriverException.");
    }

    public static void untilUrlMatches(String needle, long duration, ChronoUnit unit) {
        new FluentWait<>(getDriver())
                .pollingEvery(Duration.of(500, unit))
                .withTimeout(Duration.of(duration, unit))
                .until(driver -> ExpectedConditions.urlMatches(needle));
    }

    public static boolean isPath(@NotNull String path) {
        var p = Pattern.compile(path);
        var url = getURL().getPath();
        var m = p.matcher(url);
        LOGGER.info("path: " + path);
        LOGGER.info("URL: " + url);
        return m.find();
    }

    public static URL getURL() {
        try {
            return new URL(Objects.requireNonNull(getDriver().getCurrentUrl()));
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Malformed URL");
        }
    }

    public static boolean isElementEnabled(@NotNull String selector, @NotNull SelectorType selectorType) {
        return findElement(selector, selectorType).isEnabled();
    }

    public static boolean isElementClickable(@NotNull String selector, @NotNull SelectorType selectorType) {
        try {
            var wait = new FluentWait<>(getDriver())
                    .withTimeout(ofSeconds(5))
                    .pollingEvery(ofSeconds(1))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class);

            wait.until(driver -> wait.until(ExpectedConditions.elementToBeClickable(by(selector, selectorType)))).click();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void waitForElementToBeClickable(@NotNull String selector, @NotNull SelectorType selectorType) {
        var wait = new FluentWait<>(getDriver())
                .withTimeout(ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(TimeoutException.class)
                .ignoring(ElementNotInteractableException.class);

        wait.until(driver -> ExpectedConditions.elementToBeClickable(by(selector, selectorType)));
    }

    public static void waitAndSelectValueFromDropDown(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String listValue) {
        var wait = new FluentWait<>(getDriver())
                .withTimeout(ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        wait.until(driver -> wait.until(ExpectedConditions.elementToBeClickable(getDriver().findElement(By.xpath(selector)))));
        var selectItem = new Select(findElement(selector, selectorType));
        selectItem.selectByVisibleText(listValue);
    }

    public static void waitAndSelectByIndex(@NotNull String selector, @NotNull SelectorType selectorType, int listValue) {
        var wait = new FluentWait<>(getDriver())
                .withTimeout(ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        wait.until(driver -> wait.until(ExpectedConditions.elementToBeClickable(by(selector, selectorType))));
        var selectItem = new Select(findElement(selector, selectorType));
        selectItem.selectByIndex(listValue);
    }

    public static void waitAndClick(@NotNull String selector, @NotNull SelectorType selectorType) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                var wait = new FluentWait<>(getDriver())
                        .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                        .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                        .ignoring(NoSuchElementException.class)
                        .ignoring(ElementClickInterceptedException.class)
                        .ignoring(TimeoutException.class)
                        .ignoring(ElementNotInteractableException.class);

                wait.until(driver -> wait.until(ExpectedConditions.elementToBeClickable(by(selector, selectorType)))).click();
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("StaleElementReferenceException encountered. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            } catch (WebDriverException e) {
                if (e.getMessage().contains("Node with given id does not belong to the document")) {
                    LOGGER.warn("WebDriverException encountered: Node with given id does not belong to the document. Attempting retry " + (attempt + 1));
                    getDriver().navigate().refresh();
                } else {
                    throw e;
                }
            }
        }
        throw new RuntimeException("Failed to click element after " + maxRetries + " attempts due to StaleElementReferenceException or WebDriverException.");
    }

    public static void waitForTextToBePresent(@NotNull String selector) {
        waitForElementToBePresent(String.format("//*[contains(text(),'%s')]", selector));
    }

    public static void waitForTitleToBePresent(@NotNull String selector) {
        waitForElementToBePresent(String.format("//h1[contains(text(),'%s')]", selector));
    }

    public static void waitForTitleToBePresent(@NotNull String htmlTag, @NotNull String selector) {
        waitForElementToBePresent(String.format("//%s[contains(text(),'%s')]", htmlTag, selector));
    }

    public static void waitForElementToBePresent(@NotNull String selector) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                var wait = new FluentWait<>(getDriver())
                        .withTimeout(ofSeconds(TIME_OUT_SECONDS))
                        .pollingEvery(ofSeconds(POLLING_SECONDS))
                        .ignoring(NoSuchElementException.class)
                        .ignoring(StaleElementReferenceException.class)
                        .ignoring(ElementClickInterceptedException.class)
                        .ignoring(TimeoutException.class)
                        .ignoring(ElementNotInteractableException.class);

                wait.until(driver -> visibilityOf(getDriver().findElement(By.xpath(selector))));
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("StaleElementReferenceException encountered. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            } catch (WebDriverException e) {
                LOGGER.warn("WebDriverException encountered. Attempting retry " + (attempt + 1));
            }
        }
        throw new RuntimeException("Failed to wait for element to be present after " + maxRetries + " attempts.");
    }

    public static void waitAndEnterText(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String textValue) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                var wait = new FluentWait<>(getDriver())
                        .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                        .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                        .ignoring(NoSuchElementException.class)
                        .ignoring(InvalidElementStateException.class)
                        .ignoring(StaleElementReferenceException.class)
                        .ignoring(ElementClickInterceptedException.class)
                        .ignoring(TimeoutException.class)
                        .ignoring(ElementNotInteractableException.class);

                wait.until(driver -> wait.until(ExpectedConditions.elementToBeClickable(by(selector, selectorType)))).sendKeys(textValue);
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("StaleElementReferenceException encountered. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            }
        }
        throw new RuntimeException("Failed to enter text after " + maxRetries + " attempts due to StaleElementReferenceException.");
    }

    public void writeCommentInContentEditableField(String comment) {
        String contentEditableXPath = "//div[@contenteditable='true']";
        String jsScript = "arguments[0].innerText = arguments[1];";
        WebElement contentEditableField = findElement(contentEditableXPath, SelectorType.XPATH);
        javaScriptExecutor(jsScript, contentEditableField, comment);
    }

    public static Object javaScriptExecutor(String jsScript, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(jsScript, args);
    }
    public static boolean isFieldEnabled(String field, SelectorType selectorType) {
        return Boolean.parseBoolean(findElement(field, selectorType).getAttribute("disabled"));
    }

    public static Object javaScriptExecutor(String jsScript) {
        return ((JavascriptExecutor) getDriver()).executeScript(jsScript);
    }

    public static void enterText(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String textValue) {
        findElement(selector, selectorType).sendKeys(textValue);
    }

    public static void enterText(@NotNull String selector, @NotNull int intValue, @NotNull SelectorType selectorType) {
        waitAndEnterText(selector, selectorType, String.valueOf(intValue));
    }

    public void replaceText(String selector, SelectorType selectorType, String text) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                WebElement element = findElement(selector, selectorType);
                element.clear();
                waitAndEnterText(selector, selectorType, text);
                return;
            } catch (NoSuchElementException e) {
                LOGGER.warn("NoSuchElementException encountered during replaceText. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            } catch (WebDriverException e) {
                LOGGER.warn("WebDriverException encountered during replaceText. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            }
        }
        throw new RuntimeException("Failed to replace text after " + maxRetries + " attempts.");
    }

    public static int returnTableRows(@NotNull String selector, @NotNull SelectorType selectorType) {
        return findElements(selector, selectorType).size();
    }

    public static void findSelectAllRadioButtonsByValue(String value) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                var radioButtons = findElements("//*[@type='radio']", SelectorType.XPATH);
                radioButtons.stream()
                        .filter(x -> x.getAttribute("value").equals(value))
                        .filter(isChecked -> !isChecked.isSelected())
                        .forEach(WebElement::click);
                return;

            } catch (StaleElementReferenceException e) {
                LOGGER.warn("StaleElementReferenceException encountered. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            }
        }
        throw new RuntimeException("Failed to select radio buttons after " + maxRetries + " attempts due to StaleElementReferenceException.");
    }


    public void refreshUntilSuccessfulOrTimeout() {
        long startTime = System.currentTimeMillis();
        long timeout = 2 * 60 * 1000;
        while (System.currentTimeMillis() - startTime < timeout) {
            if (isElementPresent("//strong[@class='govuk-tag govuk-tag--green' and contains(text(),'Successful')]", SelectorType.XPATH)) {
                return;
            }
            refreshPage();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted", e);
            }
        }
        throw new RuntimeException("Timeout: 'Successful' tag not found within 2 minutes");
    }

    public void selectFirstValueInList(String selector) {
        findElements(selector, SelectorType.XPATH).stream().findFirst().get().click();
    }

    public void selectFirstValueFromDropdown(String dropdownSelector, String optionSelector) {
        var wait = new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class);

        WebElement dropdown = wait.until(driver -> driver.findElement(By.xpath(dropdownSelector)));
        dropdown.click();

        List<WebElement> options = wait.until(driver -> driver.findElements(By.xpath(optionSelector)));
        if (!options.isEmpty()) {
            options.get(0).click();
        } else {
            throw new NoSuchElementException("No options found in the dropdown.");
        }
    }

    public boolean checkForFullMatch(String searchTerm) {
        return findElements("//table/tbody/tr[*]", SelectorType.XPATH).stream().allMatch(w -> w.getText().contains(searchTerm));
    }

    public boolean checkForPartialMatch(String searchTerm) {
        return findElements("//table/tbody/tr[*]", SelectorType.XPATH).stream().anyMatch(w -> w.getText().contains(searchTerm));
    }

    public void enterTextIntoMultipleFields(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String text) {
        var wait = new FluentWait<>(getDriver())
                .withTimeout(ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(InvalidElementStateException.class)
                .ignoring(StaleElementReferenceException.class);

        wait.until(driver -> wait.until(elementToBeClickable(by(selector, selectorType))));
        findElements(selector, selectorType).forEach(x -> x.sendKeys(text));
    }

    public String getValue(String selector, SelectorType selectorType) {
        return findElement(selector, selectorType).getAttribute("value");
    }

    public static void waitForPageLoad() {
        var wait = new FluentWait<>(getDriver())
                .withTimeout(ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class);

        Function<WebDriver, Boolean> expect = driver -> {
            assert driver != null;
            return Objects.requireNonNull(((JavascriptExecutor) driver).executeScript("return document.readyState")).toString().equals("complete");
        };
        wait.until(expect);
        try {
            assertEquals("complete", javaScriptExecutor("return document.readyState").toString());
        } catch (Exception e) {
            LOGGER.info("Page timed out trying to load.");
        }
    }

    public static void waitForUrlToContain(String substring, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.urlContains(substring));
    }

    protected static boolean ifUrlContains(String substring) {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(1))
                    .until(driver -> driver.getCurrentUrl().contains(substring));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void enterDateFieldsByPartialId(String regex, HashMap<String, String> hashMapDate) {
        replaceText(regex.concat("_day"), SelectorType.ID, hashMapDate.get("day"));
        replaceText(regex.concat("_month"), SelectorType.ID, hashMapDate.get("month"));
        replaceText(regex.concat("_year"), SelectorType.ID, hashMapDate.get("year"));
    }

    public void clickAllCheckboxes() {
        getDriver().findElements(By.xpath("//*[@type='checkbox']")).forEach(WebElement::click);
    }

    public static void refreshPageUntilElementAppears(String selector, SelectorType selectorType) {
        boolean elementFound = false;
        long kickoutTime = System.currentTimeMillis() + 120 * 2000;
        while (!elementFound && System.currentTimeMillis() < kickoutTime) {
            javaScriptExecutor("location.reload(true)");
            elementFound = isElementPresent(selector, selectorType);
        }
    }

    public void waitForTabsToLoad(int expectedNumberOfTabs, int seconds) {
        ArrayList<String> tabs;
        long kickOut = System.currentTimeMillis() + (seconds * 1000L);
        do {
            tabs = new ArrayList<>(getWindowHandles());
            if (kickOut < System.currentTimeMillis()) {
                throw new TimeoutException("Incorrect number of tabs or tabs failed to load.");
            }
        } while (tabs.size() != expectedNumberOfTabs);
    }

    public void closeTabAndFocusTab(int tab) {
        closeTab();
        var tabs = new ArrayList<>(getWindowHandles());
        switchToWindow(tabs.get(tab));
    }

    public static void uploadFile(@NotNull String inputBoxSelector, @NotNull String file, String jScript, @NotNull SelectorType selectorType) {
        if (jScript != null) {
            javaScriptExecutor(jScript);
        }
        waitAndEnterText(inputBoxSelector, selectorType, file);
    }

    public static void untilNotInDOM(@NotNull String selector, int seconds) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(seconds))
                .until(driver -> ExpectedConditions.presenceOfAllElementsLocatedBy(by(selector, SelectorType.CSS)));
    }

    public static String getSelectedTextFromDropDown(@NotNull String selector, @NotNull SelectorType selectorType) {
        var option = new Select(findElement(selector, selectorType));
        return option.getFirstSelectedOption().getText();
    }

    public static void scrollToBottom() {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                var footer = getDriver().findElement(By.className("govuk-footer"));
                int deltaY = footer.getRect().y;
                new Actions(getDriver())
                        .scrollByAmount(0, deltaY)
                        .perform();
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("StaleElementReferenceException encountered. Attempting retry " + (attempt + 1));
                getDriver().navigate().refresh();
            }
        }
        throw new RuntimeException("Failed to scroll to bottom after " + maxRetries + " attempts due to StaleElementReferenceException.");
    }



    public boolean pageContains(String text) {
        return Objects.requireNonNull(getDriver().getPageSource()).contains(text);
    }}
