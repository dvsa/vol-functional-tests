package org.dvsa.testing.framework.pageObjects.conditions;

import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public final class ElementCondition extends BasePage {

    public static ExpectedCondition<Boolean> absenceOfElement(@NotNull String selector, SelectorType selectorType) {
        return new ExpectedCondition<Boolean>(){
            @Override
            public Boolean apply(final WebDriver driver) {
                return isElementNotPresent(selector, selectorType);
            }

            @Override
            public String toString() {
                return String.format("Absence of element with locator of: %s", selector);
            }
        };
    }



    public static ExpectedCondition<Boolean> isPresent(@NotNull String selector, SelectorType selectorType) {
        return new ExpectedCondition<Boolean>(){
            @Override
            public Boolean apply(final WebDriver driver) {
                return isElementPresent(selector, selectorType);
            }

            @Override
            public String toString() {
                return String.format("Presence of element with locator of: %s", selector);
            }
        };
    }

}
