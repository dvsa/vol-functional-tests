package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.runner.Hooks;
import org.dvsa.testing.framework.document.XMLParser;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.IOException;

public class ScenarioTearDown {
    XMLParser parser = new XMLParser();

    @After
    public void afterClass(Scenario scenario) throws Exception {
        Hooks.attach(scenario);
    }

    @BeforeStep
    public void chooseBrowser() throws IOException {
        setUpReportConfig();
        parser.getElementsByTagName();
        for (String browserName : parser.browser) {
            System.out.println("======" + browserName);
            System.setProperty("browser", browserName);
        }
    }


    public void setUpReportConfig() throws IOException {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
    }
}