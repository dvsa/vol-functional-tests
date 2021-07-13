package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.runner.Hooks;

import java.io.IOException;


public class ScenarioTearDown {
    @After
    public void afterClass(Scenario scenario) throws Exception {
        Hooks hooks = new Hooks();
        hooks.attach(scenario);
    }

//    @After
//    public static void tearDown() {
//        Browser.navigate().close();
//    }

    @Before
    public void setUpReportConfig() throws IOException {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
    }
}