package org.dvsa.testing.framework.runner;

import activesupport.IllegalBrowserException;
import cucumber.api.Scenario;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import activesupport.driver.Browser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

public class Hooks {

    private static File directory = new File("img");

    private void createDirectory() throws IOException {
        FileUtils.forceMkdir(directory);
    }


    @Attachment(value = "Screenshot on failure", type = "image/png")
    public void attach(Scenario scenarioStatus) throws IOException, IllegalBrowserException {
        createDirectory();
        File screenshot = new File(String.format(directory + "/error%s.png", Instant.now().getEpochSecond()));
        if (scenarioStatus.isFailed()) {
            FileOutputStream screenshotStream = new FileOutputStream(screenshot);
            byte[] attachment = ((TakesScreenshot) Browser.navigate())
                    .getScreenshotAs(OutputType.BYTES);
            scenarioStatus.embed(attachment, String.valueOf(screenshotStream));
            screenshotStream.write(attachment);
            screenshotStream.close();
        }
    }

    private void deleteDirectory() throws IOException {
        FileUtils.deleteDirectory(directory);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        if (Browser.isBrowserOpen()) {
            Browser.closeBrowser();
        }
    }
}