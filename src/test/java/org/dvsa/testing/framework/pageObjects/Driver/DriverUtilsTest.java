package org.dvsa.testing.framework.pageObjects.Driver;

import activesupport.driver.Browser;
import activesupport.selfhealing.SelfHealingElementLocator;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DriverUtilsTest extends BasePage {
    private WebDriver driver;
    private WebElement element;
    private MockedStatic<Browser> browser;
    private MockedStatic<SelfHealingElementLocator> healing;

    @BeforeEach
    public void setUp() {
        driver = mock(WebDriver.class);
        element = mock(WebElement.class);
        browser = mockStatic(Browser.class);
        browser.when(Browser::navigate).thenReturn(driver);
        healing = mockStatic(SelfHealingElementLocator.class);
    }

    @AfterEach
    public void tearDown() {
        healing.close();
        browser.close();
    }

    @Test
    public void workingSelectorDoesNotInvokeHealing() {
        when(driver.findElement(By.id("save"))).thenReturn(element);

        assertSame(element, findElement("save", SelectorType.ID));
        healing.verifyNoInteractions();
    }

    @Test
    public void missingSelectorUsesHealedElement() {
        when(driver.findElement(By.id("save"))).thenThrow(new NoSuchElementException("Missing save"));
        healing.when(() -> SelfHealingElementLocator.heal(driver, "save", "ID"))
                .thenReturn(Optional.of(element));

        assertSame(element, findElement("save", SelectorType.ID));
        healing.verify(() -> SelfHealingElementLocator.heal(driver, "save", "ID"), times(1));
    }

    @Test
    public void unsuccessfulHealingPreservesOriginalFailure() {
        NoSuchElementException failure = new NoSuchElementException("Missing save");
        when(driver.findElement(By.id("save"))).thenThrow(failure);
        healing.when(() -> SelfHealingElementLocator.heal(driver, "save", "ID"))
                .thenReturn(Optional.empty());

        assertSame(failure, assertThrows(NoSuchElementException.class,
                () -> findElement("save", SelectorType.ID)));
    }

    @Test
    public void disabledHealingPreservesOriginalFailureWithoutReadingThePage() {
        String previous = System.getProperty("selfHealing.enabled");
        System.setProperty("selfHealing.enabled", "false");
        try {
            NoSuchElementException failure = new NoSuchElementException("Missing save");
            when(driver.findElement(By.id("save"))).thenThrow(failure);
            healing.when(() -> SelfHealingElementLocator.heal(driver, "save", "ID")).thenCallRealMethod();

            assertSame(failure, assertThrows(NoSuchElementException.class,
                    () -> findElement("save", SelectorType.ID)));
            verify(driver, never()).getPageSource();
        } finally {
            if (previous == null) {
                System.clearProperty("selfHealing.enabled");
            } else {
                System.setProperty("selfHealing.enabled", previous);
            }
        }
    }

    @Test
    public void normalWaitCompletesBeforeConsideringHealing() {
        when(driver.findElement(By.id("save")))
                .thenThrow(new NoSuchElementException("Still loading")).thenReturn(element);

        assertSame(element, findElement("save", SelectorType.ID, 2));
        healing.verifyNoInteractions();
    }

    @Test
    public void timedOutClickableLookupUsesHealedElement() {
        when(driver.findElement(By.id("save"))).thenThrow(new NoSuchElementException("Missing save"));
        when(driver.findElements(By.id("save"))).thenReturn(List.of());
        when(element.isDisplayed()).thenReturn(true);
        when(element.isEnabled()).thenReturn(true);
        healing.when(() -> SelfHealingElementLocator.heal(driver, "save", "ID"))
                .thenReturn(Optional.of(element));

        waitForClickableElement("save", SelectorType.ID, 0).click();

        verify(element).click();
        healing.verify(() -> SelfHealingElementLocator.heal(driver, "save", "ID"), times(1));
    }

    @Test
    public void unsuccessfulHealingPreservesTimeoutFailure() {
        when(driver.findElement(By.id("save"))).thenThrow(new NoSuchElementException("Missing save"));
        when(driver.findElements(By.id("save"))).thenReturn(List.of());
        healing.when(() -> SelfHealingElementLocator.heal(driver, "save", "ID")).thenReturn(Optional.empty());

        assertThrows(TimeoutException.class, () -> findElement("save", SelectorType.ID, 0));
        healing.verify(() -> SelfHealingElementLocator.heal(driver, "save", "ID"), times(1));
    }

    @Test
    public void hiddenOriginalElementDoesNotInvokeHealing() {
        when(driver.findElement(By.id("save"))).thenReturn(element);
        when(driver.findElements(By.id("save"))).thenReturn(List.of(element));
        when(element.isDisplayed()).thenReturn(false);

        assertThrows(TimeoutException.class, () -> waitForVisibleElement("save", SelectorType.ID, 0));
        healing.verifyNoInteractions();
    }

    @Test
    public void healedElementMustStillBeClickable() {
        when(driver.findElement(By.id("save"))).thenThrow(new NoSuchElementException("Missing save"));
        when(driver.findElements(By.id("save"))).thenReturn(List.of());
        when(element.isDisplayed()).thenReturn(true);
        when(element.isEnabled()).thenReturn(false);
        healing.when(() -> SelfHealingElementLocator.heal(driver, "save", "ID"))
                .thenReturn(Optional.of(element));

        assertThrows(TimeoutException.class, () -> waitForClickableElement("save", SelectorType.ID, 0));
        verify(element, never()).click();
    }

    @Test
    public void absenceCheckDoesNotInvokeHealing() {
        when(driver.findElement(By.id("save"))).thenThrow(new NoSuchElementException("Missing save"));

        assertTrue(isElementNotPresent("save", SelectorType.ID));
        healing.verifyNoInteractions();
    }
}
