package org.dvsa.testing.framework.runner;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.document.XMLParser;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import activesupport.driver.Browser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

public class Hooks {

    XMLParser parser = new XMLParser();
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
            scenarioStatus.attach(attachment, "PNG", String.valueOf(screenshotStream));
            screenshotStream.write(attachment);
            screenshotStream.close();
        }
    }

    public void setUpReportConfig() throws IOException {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
    }

    @Before
    public void chooseBrowser() throws IOException {
        setUpReportConfig();
        parser.getElementsByTagName();
        for (String browserName : parser.browser) {
            System.out.println("======" + browserName);
            System.setProperty("browser",browserName);
        }
    }

    @After
    public void tearDown() throws Exception {
       if(Browser.isBrowserOpen()){
           Browser.closeBrowser();
       }
    }
}