package org.dvsa.testing.framework.runner;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import activesupport.driver.Browser;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

public class Hooks {

    private static final ThreadLocal<WebDriver> browser = new ThreadLocal<>();

    public static ThreadLocal<WebDriver> getBrowser() {
        return browser;
    }

    private static final File directory = new File(System.getProperty("user.dir") + "/target/img");

    private static void createDirectory() throws IOException {
        FileUtils.forceMkdir(directory);
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    public void attach(Scenario scenarioStatus) throws IOException {
        if (scenarioStatus.isFailed())
            createDirectory();
        File screenshot = new File(String.format(directory + "/error%s.png", Instant.now().getEpochSecond()));
        if (scenarioStatus.isFailed()) {
            FileOutputStream screenshotStream = new FileOutputStream(screenshot);
            byte[] attachment = ((TakesScreenshot) getBrowser())
                    .getScreenshotAs(OutputType.BYTES);
            scenarioStatus.attach(attachment, "PNG", String.valueOf(screenshotStream));
            screenshotStream.write(attachment);
            screenshotStream.close();
        }
    }

    @Before
    public void setUp() {
        getBrowser().set(Browser.navigate());
    }

    @AfterAll
    public void tearDown() {
        getBrowser().get().quit();
    }
}