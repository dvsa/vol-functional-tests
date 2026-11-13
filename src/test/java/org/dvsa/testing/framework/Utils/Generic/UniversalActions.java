package org.dvsa.testing.framework.Utils.Generic;

import activesupport.driver.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.Awaitility;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UniversalActions extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(UniversalActions.class);

    public UniversalActions(World world) {
    }

    public static void refreshPageWithJavascript() {javaScriptExecutor("location.reload(true)");
    }

    public List<WebElement> getTableBodyRowList() {
        return findElements("//tbody", SelectorType.XPATH);
    }

    public void checkValue(String selector, SelectorType selectorType, String text) {
        assertEquals(getValue(selector, selectorType), text);
    }

    public static void clickSaveAndContinue() {
        try {
            waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        } catch (Exception e) {
            LOGGER.warn("Failed to click using form-actions[saveAndContinue], attempting submitbutton ID");
            waitAndClick("//button[@id='submitbutton']", SelectorType.XPATH);
        }
    }

    public static void clickSubmit() {
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }

    public static void clickIAgreeSubmit(){
        waitAndClick("form-actions[sign]", SelectorType.NAME);
    }

    public static void clickSend() {
        waitAndClick("form-actions[send]", SelectorType.NAME);
    }

    public static void clickContinue() {
        waitAndClick("form-actions[continue]", SelectorType.ID);
    }

    public static void clickSaveAndReturn() {
        try {
            waitAndClick("//*[@id='form-actions[save]']", SelectorType.XPATH);
        } catch (Exception e) {
            LOGGER.warn("Failed to click using form-actions[save], attempting save-return-button XPath");
            waitAndClick("//button[@id='save-return-button']", SelectorType.XPATH);
        }
    }


    public static void clickSave() {waitAndClick("//*[@id='form-actions[save]']", SelectorType.XPATH);}

    public static void clickCancel() {
        waitAndClick("form-actions[cancel]", SelectorType.NAME);
    }

    public static void clickOk() {
        waitAndClick("//*[@id='form-actions[ok]']", SelectorType.XPATH);
    }

    public static void clickConfirm() {
        waitAndClick("//button[@id='form-actions[confirm]']", SelectorType.XPATH);
    }

    public static void clickPay() {waitAndClick("//*[@name='form-actions[pay]']", SelectorType.XPATH);}

    public static void clickPayAndSubmit() {waitAndClick( "//*[@id='submitAndPay']", SelectorType.XPATH);}

    public static void clickHome() {
        refreshPage();
        click("//a[@href='/dashboard/']", SelectorType.XPATH);
    }

    public static void clickManageUsers() {
        refreshPage();
        waitAndClick("//a[@href='/manage-user/']", SelectorType.XPATH);
    }

    public static void clickYourAccount() {
        refreshPage();
        waitAndClick("//a[@href='/your-account/']", SelectorType.XPATH);
    }

    public static void clickSignOut() {
        refreshPage();
        waitAndClick("//a[@href='/auth/logout/']", SelectorType.XPATH);
    }

    public static void closeAlert() {
        waitForElementToBePresent("//p[@role='alert']");
        waitAndClick("//*[contains(text(),'Close')]", SelectorType.XPATH);
    }

    public static Path downloadCsv(Path targetDirectory) throws IOException {
        var driver = Browser.getDriver();
        RemoteWebDriver remoteDriver = (RemoteWebDriver)
                (driver instanceof RemoteWebDriver ? driver : new Augmenter().augment(driver));
        String csvName = Awaitility.await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> remoteDriver.getDownloadableFiles().stream()
                                .filter(name -> name.toLowerCase(Locale.ROOT).endsWith(".csv"))
                                .findFirst()
                                .orElse(null),
                        Objects::nonNull);

        Files.createDirectories(targetDirectory);
        remoteDriver.downloadFile(csvName, targetDirectory);
        return targetDirectory.resolve(csvName);
    }
}