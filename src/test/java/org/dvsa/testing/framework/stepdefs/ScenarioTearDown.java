package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.runner.Hooks;
import org.dvsa.testing.lib.pages.BasePage;

import java.net.MalformedURLException;


public class ScenarioTearDown extends BasePage implements En {

    public ScenarioTearDown(World world) {
        After((Scenario scenario) -> {
            Hooks hooks = new Hooks();
            hooks.attach(scenario);
        });
    }
}