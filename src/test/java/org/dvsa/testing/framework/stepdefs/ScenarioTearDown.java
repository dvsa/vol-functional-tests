package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.runner.Hooks;
import org.dvsa.testing.lib.pages.BasePage;

import java.io.IOException;


public class ScenarioTearDown extends BasePage implements En {

    @Before
    public void setUpReportConfig() throws IOException {

        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
    }

    public ScenarioTearDown(World world) {
        After((Scenario scenario) -> {
            Hooks hooks = new Hooks();
            hooks.attach(scenario);
        });
    }
}