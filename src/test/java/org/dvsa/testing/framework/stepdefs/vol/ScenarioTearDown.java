package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.BrowserStack;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.runner.Hooks;


import java.io.IOException;


public class ScenarioTearDown {
    @After
    public void afterClass(Scenario scenario) throws Exception {
        Hooks.attach(scenario);
        BrowserStack.stopLocal();
    }

    @Before
    public void setUpReportConfig() throws Exception {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
//        if(System.getProperty("gridURL") != null && (System.getProperty("gridURL")
//                .contains("hub-cloud.browserstack.com"))){
//            BrowserStack.startLocal();
//        }
    }
}