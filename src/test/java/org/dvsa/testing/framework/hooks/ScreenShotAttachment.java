package org.dvsa.testing.framework.hooks;

import cucumber.api.Scenario;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import activesupport.driver.Browser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

public class ScreenShotAttachment {


    private static final File directory = new File(System.getProperty("user.dir") + "/target/img");

    private static void createDirectory() throws IOException {
        FileUtils.forceMkdir(directory);
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    public static void attach(Scenario scenarioStatus) throws IOException {
        if (scenarioStatus.isFailed())
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
}