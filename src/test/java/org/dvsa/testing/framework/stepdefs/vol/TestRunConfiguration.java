package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import activesupport.driver.BrowserStack;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.hooks.ScreenShotAttachment;


public class TestRunConfiguration {
    @After
    public void generateScreenShotForFailedScenario(Scenario scenario) throws Exception {
        ScreenShotAttachment.attach(scenario);
    }

    @AfterAll
    public static void tearDown(){
        Browser.navigate().quit();
    }

    @Before
    public void setUp() throws Exception {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
        if(System.getProperty("gridURL") != null && (System.getProperty("gridURL")
                .contains("hub-cloud.browserstack.com"))){
            BrowserStack.startLocal();
        }
    }
}