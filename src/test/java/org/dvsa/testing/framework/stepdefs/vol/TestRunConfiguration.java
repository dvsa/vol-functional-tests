package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import static org.dvsa.testing.framework.pageObjects.BasePage.isLinkPresent;
import static org.dvsa.testing.framework.pageObjects.BasePage.waitAndClick;

public class TestRunConfiguration {
    @Before
    public void setUp(Scenario scenario) throws Exception {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
        System.out.println("Testing Scenario:" + scenario.getName());
    }

    @After
    public void takeScreenShotAfterFailure(Scenario scenario) {
        try {
            attach(scenario);
            Collection<String> tags = scenario.getSourceTagNames();
            for (String tag : tags) {
                if (Browser.isBrowserOpen() && tag.contains("readOnly")) {
                    if (isLinkPresent("Sign out", 10)) {
                        waitAndClick("Sign out", SelectorType.LINKTEXT);
                    }
                }
            }
        } finally {
            try {
                if (Browser.isBrowserOpen()) {
                    Browser.closeBrowser();
                }
            } catch (Exception e) {
                System.out.println("An error occurred while trying to quit the browser: " + e.getMessage());
            }
        }
    }

    public static void attach(Scenario scenarioStatus) {
        if (scenarioStatus.isFailed()) {
            Allure.addAttachment("Oops something has gone wrong",
                    new ByteArrayInputStream(((TakesScreenshot) Browser.navigate()).getScreenshotAs(OutputType.BYTES)));
                    var currentUrl = Browser.getDriver().getCurrentUrl();
             Allure.addAttachment("Current URL", currentUrl);
        }
        }
    }
