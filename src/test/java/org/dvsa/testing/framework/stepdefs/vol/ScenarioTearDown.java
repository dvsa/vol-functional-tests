package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.runner.Hooks;


import java.io.IOException;


public class ScenarioTearDown {
    @After
    public void afterClass(Scenario scenario) throws Exception {
        Hooks hooks = new Hooks();
        hooks.attach(scenario);
    }

    @Before
    public void setUpReportConfig() throws IOException {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
    }
}