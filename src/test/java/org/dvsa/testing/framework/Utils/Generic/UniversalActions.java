package org.dvsa.testing.framework.Utils.Generic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UniversalActions extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(UniversalActions.class);
    private static World world;

    public UniversalActions(World world) {
        this.world = world;
    }

    public void refreshPageWithJavascript() {javaScriptExecutor("location.reload(true)");
    }

    public List<WebElement> getTableBodyRowList() {
        return findElements("//tbody", SelectorType.XPATH);
    }

    public void checkValue(String selector, SelectorType selectorType, String text) {
        assertEquals(getValue(selector, selectorType), text);
    }

    public static void clickSaveAndContinue() {
        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void clickSubmit() {
        waitAndClick("form-actions[submit]", SelectorType.NAME);
    }

    public void clickSend() {
        waitAndClick("form-actions[send]", SelectorType.NAME);
    }

    public void clickContinue() {
        waitAndClick("form-actions[continue]", SelectorType.ID);
    }

    public void clickSaveAndReturn() {
        waitAndClick("//*[@id='form-actions[save]']", SelectorType.XPATH);
    }

    public void clickCancel() {
        waitAndClick("form-actions[cancel]", SelectorType.NAME);
    }

    public void clickOk() {
        waitAndClick("//*[@id='form-actions[ok]']", SelectorType.XPATH);
    }

    public void clickConfirm() {
        waitAndClick("//button[@id='form-actions[confirm]']", SelectorType.XPATH);
    }

    public void clickPay() {
        waitAndClick("//*[@name='form-actions[pay]']", SelectorType.XPATH);
    }

    public void closeAlert() {
        waitForElementToBePresent("//p[@role='alert']");
        waitAndClick("//*[contains(text(),'Close')]", SelectorType.XPATH);
    }
}