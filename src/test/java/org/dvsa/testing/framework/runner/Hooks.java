package org.dvsa.testing.framework.runner;

import activesupport.IllegalBrowserException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.TakesScreenshot;
import activesupport.driver.Browser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

public class Hooks {


    private static final File directory = new File(System.getProperty("user.dir") + "/target/img");

    private void createDirectory() throws IOException {
        FileUtils.forceMkdir(directory);
    }

    @Before
    public void deleteRunnersFolder(){
        File runners = new File("src/test/java/runners");
        if(runners.exists()){
            runners.delete();
        }
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    public void attach(Scenario scenarioStatus) throws IOException, IllegalBrowserException {
      if(scenarioStatus.isFailed())
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

    @After
    public static void tearDown(){
        try {
            if(Browser.isBrowserOpen());
            Browser.closeBrowser();
        } catch (SessionNotCreatedException ignored) { } catch (Exception e) {
            e.printStackTrace();
        }
    }
}