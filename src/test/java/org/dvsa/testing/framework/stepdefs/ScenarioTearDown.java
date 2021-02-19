package org.dvsa.testing.framework.stepdefs;

import activesupport.driver.Browser;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.runner.Hooks;

import java.io.IOException;


public class ScenarioTearDown {

    @After
    public static void afterClass(Scenario scenario) throws Exception {
        Hooks hooks = new Hooks();
        hooks.attach(scenario);
        if (scenario.getStatus().toString().equals("PASSED")) {
            if (Browser.isBrowserOpen()) {
                Browser.closeBrowser();
            }
        }
    }

    @Before
    public void setUpReportConfig() throws IOException {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
    }
}