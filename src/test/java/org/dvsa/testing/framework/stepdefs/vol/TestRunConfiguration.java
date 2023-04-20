package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.hooks.ScreenShotAttachment;

public class TestRunConfiguration {
    @Before
    public void setUp(Scenario scenario) throws Exception {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
        System.out.println("Testing Scenario:" + scenario.getName());
    }

    @After
    public void generateScreenShotForFailedScenario(Scenario scenario) throws Exception {
        ScreenShotAttachment.attach(scenario);
    }

    @AfterAll
    public static void tearDown() throws Exception {
//        Browser.closeBrowser();
    }
}