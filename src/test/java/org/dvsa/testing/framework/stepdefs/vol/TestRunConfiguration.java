package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import activesupport.driver.BrowserStack;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.hooks.ScreenShotAttachment;


public class TestRunConfiguration {
    @After
    public void generateScreenShotForFailedScenario(Scenario scenario) throws Exception {
        ScreenShotAttachment.attach(scenario);
    }

    @Before
    public void setUp() throws Exception {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
        if(!Browser.isBrowserOpen()){
            Browser.navigate();
        }
        if(System.getProperty("gridURL") != null && (System.getProperty("gridURL")
                .contains("hub-cloud.browserstack.com"))){
            BrowserStack.startLocal();
        }
    }
}