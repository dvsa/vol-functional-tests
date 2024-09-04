package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.hooks.ScreenShotAttachment;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.Collection;
import java.util.List;

import static org.dvsa.testing.framework.pageObjects.BasePage.*;

public class TestRunConfiguration {
    @Before
    public void setUp(Scenario scenario) throws Exception {
        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();
        System.out.println("Testing Scenario:" + scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) throws Exception {
        ScreenShotAttachment.attach(scenario);
        Collection<String> tags = scenario.getSourceTagNames();
        for (String tag : tags) {
            if (Browser.isBrowserOpen() && tag.contains("readOnly")){
                if (isLinkPresent("Sign out", 10)) {
                    waitAndClick("Sign out", SelectorType.LINKTEXT);
                }
            }
        }
        if (Browser.isBrowserOpen()) {
            Browser.closeBrowser();
        }
    }
}