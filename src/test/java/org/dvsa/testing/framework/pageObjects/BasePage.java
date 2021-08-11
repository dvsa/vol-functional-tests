package org.dvsa.testing.framework.pageObjects;

import com.google.common.base.Function;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.conditions.ElementCondition;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;

public abstract class BasePage extends DriverUtils {
    public static final int WAIT_TIME_SECONDS = 10;
    private static final int TIME_OUT_SECONDS = 60;
    private static final int POLLING_SECONDS = 1;

    private static String ERROR_MESSAGE_HEADING = "Please correct the following errors";
    private static String ERROR_CLASS = ".error__text";
    protected static String MAIN_TITLE_SELECTOR = "h1";


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

    protected static String getText(@NotNull String selector) {
        return getText(selector, SelectorType.CSS);
    }

    protected static void untilElementWithText(String selector, SelectorType selectorType, String text, ChronoUnit unit, long duration) {
        new FluentWait<>(getDriver())
                .ignoreAll(Arrays.asList(NoSuchElementException.class, StaleElementReferenceException.class))
                .withTimeout(Duration.of(duration, unit))
                .pollingEvery(Duration.of(500, ChronoUnit.MILLIS))
                .until(driver -> driver.findElement(by(selector, selectorType)).getText().equalsIgnoreCase(text));
    }

    protected static boolean isTextPresent(String locator) {
        boolean itsFound = true;
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), 30);
            wait.until(ExpectedConditions.visibilityOf(findElement(String.format("//*[contains(text(),\"%s\")]", locator),SelectorType.XPATH)));
        } catch (Exception e) {
            return false;

        }
        return itsFound;
    }

    public static boolean isErrorMessagePresent(){
        boolean hasError = false;

        if (isTextPresent(ERROR_MESSAGE_HEADING) || isElementPresent(ERROR_CLASS, SelectorType.CSS)) hasError = true;

        return hasError;
    }

    protected static void scrollAndEnterField(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String text, boolean append) {
        WebElement field = findElement(selector, selectorType);

        if (!append)
            field.clear();

        new Actions(getDriver()).moveToElement(field).sendKeys(field, text).perform();
    }

    protected static void scrollAndEnterField(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String text) {
        scrollAndEnterField(selector, selectorType, text, false);
    }

    protected static void scrollAndEnterField(@NotNull String selector, @NotNull String text) {
        scrollAndEnterField(selector, SelectorType.CSS, text);
    }

    protected static void clickByLinkText(@NotNull String selector) {
        findElement(selector,SelectorType.PARTIALLINKTEXT).click();
    }

    protected static void clickByXPath(@NotNull String selector) {
        findElement(selector,SelectorType.XPATH).click();
    }

    protected static void clickById(@NotNull String selector) {
        findElement(selector,SelectorType.ID).click();
    }

    protected static void clickByName(@NotNull String selector) {
        findElement(selector,SelectorType.NAME).click();
    }

    protected static void selectValueFromDropDown(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String listValue) {
        Select selectItem = new Select(findElement(selector, selectorType));
        selectItem.selectByVisibleText(listValue);
    }

    protected static void selectValueFromDropDownByIndex(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull int listValue) {
        Select selectItem = new Select(findElement(selector, selectorType));
        selectItem.selectByIndex(listValue);
    }

    protected static boolean isLinkPresent(String locator, int duration) {
        boolean itsFound = true;
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), duration);
            wait.until(ExpectedConditions.visibilityOf(findElement(locator,SelectorType.PARTIALLINKTEXT)));
        } catch (Exception e) {
            return false;

        }
        return itsFound;
    }

    protected static boolean isTitlePresent(String locator, int duration) {
        boolean itsFound = true;
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), duration);
            wait.until(ExpectedConditions.visibilityOf(findElement(String.format("//h1[contains(text(),\"%s\")]", locator),SelectorType.XPATH)));
        } catch (Exception e) {
            return false;
        }
        return itsFound;
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
        } catch (StaleElementReferenceException e){
            if(isElementPresent(selector, selectorType)){
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
        boolean isElementPresent = true;

        try {
            findElement(selector, selectorType);
        } catch (NoSuchElementException e) {
            isElementPresent = false;
        }

        return isElementPresent;
    }

    protected static boolean isElementVisible(@NotNull String selector, long duration) {
        boolean visible = true;

        try {
            untilVisible(selector, SelectorType.XPATH, duration, TimeUnit.SECONDS);
        } catch (Exception ex) {
            visible = false;
        }

        return visible;
    }

    protected static boolean isElementPresentWithin(@NotNull String selector) {
        boolean isInDOM = true;

        try {
            waitForElementToBePresent(selector);
        } catch (TimeoutException e) {
            isInDOM = false;
        }

        return isInDOM;
    }

    public static void untilVisible(@NotNull String selector, @NotNull SelectorType selectorType, long duration, TimeUnit timeUnit) {
        By by = by(selector, selectorType);

        until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void until(ExpectedCondition<?> expectedCondition) {

        new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class)
                .until(expectedCondition);
    }

    public static void untilElementIsPresent(@NotNull String selector, SelectorType selectorType, long duration, TimeUnit timeUnit){
        until(ElementCondition.isPresent(selector, selectorType));
    }

    public static void untilElementIsPresent(@NotNull String selector, long duration, TimeUnit timeUnit){
        untilElementIsPresent(selector, SelectorType.CSS, duration, timeUnit);
    }

    public static boolean tryUntilElementIsPresent(@NotNull String selector, SelectorType selectorType, long duration, TimeUnit timeUnit) {
        try {
            untilElementIsPresent(selector, selectorType, duration, timeUnit);
        } catch (WebDriverException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static void untilElementIsNotPresent(@NotNull String selector, SelectorType selectorType){
        until(ElementCondition.absenceOfElement(selector, selectorType));
    }

    public static String getElementValueByText(@NotNull String selector, @NotNull SelectorType selectorType) {
        return getText(selector, selectorType);
    }

    public static void untilUrlMatches(String needle, long duration, ChronoUnit unit){
        new FluentWait<>(getDriver())
                .pollingEvery(Duration.of(500, ChronoUnit.MILLIS))
                .withTimeout(Duration.of(duration, unit))
                .until(ExpectedConditions.urlMatches(needle));
    }

    public static boolean isPath(@NotNull String path) {
        Pattern p  = Pattern.compile(path);
        Matcher m  = p.matcher(getURL().getPath());
        boolean matches = m.find();

        return matches;
    }

    public static URL getURL() {
        URL url = null;
        try {
            url = new URL(getDriver().getCurrentUrl());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Malformed URL");
        }

        return url;
    }

    public static boolean isElementEnabled(@NotNull String selector, @NotNull SelectorType selectorType) {
        return findElement(selector, selectorType).isEnabled();
    }

    public static void waitForElementToBeClickable(@NotNull String selector, @NotNull SelectorType selectorType) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
                .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(by(selector, selectorType)));
                return submit;
            }
        });
    }

    public static void waitAndSelectByIndex(@NotNull final String textWait, @NotNull final String selector, @NotNull SelectorType selectorType, @NotNull final int listValue) {
        final Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
                .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement foundIt = null;
                foundIt = (WebElement)wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(String.format("//*[contains(text(),'%s')]", textWait)))));
                Select selectItem = new Select(driver.findElement(By.xpath(selector)));
                selectItem.selectByIndex(listValue);
                return foundIt;
            }
        });
    }

    public static void waitAndClick(@NotNull String selector, @NotNull SelectorType selectorType) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
                .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class);

        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(by(selector, selectorType))));
                submit.click();
                return submit;
            }
        });
    }

    public static void waitForTextToBePresent(@NotNull String selector) {
        waitForElementToBePresent(String.format("//*[contains(text(),'%s')]", selector));
    }

    public static void waitForTitleToBePresent(@NotNull String selector) {
        waitForElementToBePresent(String.format("//h1[contains(text(),'%s')]", selector));
    }

    public static void waitForElementToBePresent(@NotNull String selector) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
                .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        WebElement element = wait.until(new Function<WebDriver, WebElement>() {

            public WebElement apply(WebDriver driver) {
                WebElement submit = null;
                submit = wait.until(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(
                        selector))));

                return submit;
            }
        });
    }

    public static void waitAndEnterText(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String textValue) {
        final Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
                .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        WebElement element = (WebElement) wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement sendText = wait.until(ExpectedConditions.elementToBeClickable(by(selector, selectorType)));
                sendText.sendKeys(textValue);
                return sendText;
            }
        });
    }

    public static Object javaScriptExecutor(String jsScript) {
        return ((JavascriptExecutor) getDriver()).executeScript(jsScript);
    }

    public static void enterText(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String textValue) {
        findElement(selector, selectorType).sendKeys(textValue);
    }

    public static void enterText(@NotNull String selector, @NotNull int intValue, @NotNull SelectorType selectorType) {
        enterText(selector, selectorType, String.valueOf(intValue));
    }

    public void replaceText(String selector, SelectorType selectorType, String text) {
        findElement(selector,selectorType).clear();
        enterText(selector, selectorType, text);
    }

    public static int returnTableRows(@NotNull String selector, @NotNull SelectorType selectorType) {
        return findElements(selector, selectorType).size();
    }

    public void findSelectAllRadioButtonsByValue(String value) {
        List<WebElement> radioButtons = findElements("//*[@type='radio']",SelectorType.XPATH);
        radioButtons.stream().
                filter(x -> x.getAttribute("value").equals(value)).
                filter(isChecked -> !isChecked.isSelected()).
                forEach(x -> x.click());
    }

    public void selectFirstValueInList(String selector) {
        findElements(selector,SelectorType.XPATH).stream().findFirst().get().click();
    }

    public boolean checkForFullMatch(String searchTerm) {
        return findElements("//table/tbody/tr[*]",SelectorType.XPATH).stream().allMatch(w -> w.getText().contains(searchTerm));
    }

    public boolean checkForPartialMatch(String searchTerm) {
        return findElements("//table/tbody/tr[*]",SelectorType.XPATH).stream().anyMatch(w -> w.getText().contains(searchTerm));
    }

    public void enterTextIntoMultipleFields(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String text) {
        findElements(selector, selectorType).stream().forEach(x -> x.sendKeys(text));
    }

    public String getValue(String selector, SelectorType selectorType) {
        return findElement(selector, selectorType).getAttribute("value");
    }

    public void waitForPageLoad()  {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
                .withTimeout(Duration.ofSeconds(TIME_OUT_SECONDS))
                .pollingEvery(Duration.ofSeconds(POLLING_SECONDS))
                .ignoring(java.util.NoSuchElementException.class);

        ExpectedCondition<Boolean> expect = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver){
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };

        wait.until(expect);

        try {
            Assert.assertEquals("complete",javaScriptExecutor("return document.readyState").toString());
        } catch (Exception e) {
            System.out.println("Page timed out trying to load.");
        }
    }

    public void replaceDateFieldsByPartialId(String regex, HashMap<String, String> hashMapDate) {
        replaceText(regex.concat("_day"), SelectorType.ID, hashMapDate.get("day"));
        replaceText(regex.concat("_month"), SelectorType.ID, hashMapDate.get("month"));
        replaceText(regex.concat("_year"), SelectorType.ID, hashMapDate.get("year"));
    }

    public void clickAllCheckboxes() {
        getDriver().findElements(By.xpath("//*[@type='checkbox']")).forEach(WebElement::click);
    }

    public static void refreshPageUntilElementAppears(String selector, SelectorType selectorType) {
        boolean elementFound = false;
        long kickoutTime = System.currentTimeMillis() + 120*1000;
        while (!elementFound && System.currentTimeMillis() < kickoutTime) {
            javaScriptExecutor("location.reload(true)");
            elementFound = isElementPresent(selector, selectorType);
        }
    }

    public void waitForTabsToLoad(int expectedNumberOfTabs, int seconds) {
        ArrayList<String> tabs;
        long kickOut = System.currentTimeMillis() + (seconds * 1000);
        do {
            tabs = new ArrayList<String> (getWindowHandles());
            if (kickOut < System.currentTimeMillis()){
                throw new TimeoutException("Incorrect number of tabs or tabs failed to load.");
            }
        } while(tabs.size() != expectedNumberOfTabs);
    }

    public void closeTabAndFocusTab(int tab) {
        closeTab();
        ArrayList<String> tabs = new ArrayList<String>(getWindowHandles());
        switchToWindow(tabs.get(tab));
    }

    public static void uploadFile(@NotNull String inputBoxSelector, @NotNull String file, String jScript, @NotNull SelectorType selectorType) {
        if (jScript != null) {
            javaScriptExecutor(jScript);
        }

        enterText(inputBoxSelector, selectorType, file);
    }

    public static void untilNotInDOM(@NotNull String selector, int seconds) {
        new WebDriverWait(getDriver(), seconds).until(not(ExpectedConditions.presenceOfAllElementsLocatedBy(by(selector, SelectorType.CSS))));
    }
}