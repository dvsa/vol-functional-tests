package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import io.cucumber.java8.En;
import io.cucumber.java8.Scenario;
import org.dvsa.testing.framework.runner.Hooks;
import org.dvsa.testing.lib.pages.BasePage;

public class ScenarioTearDown extends BasePage implements En {

    public ScenarioTearDown(World world) {
        After((Scenario scenario) -> {
            Hooks hooks = new Hooks();
            hooks.attach(scenario);
        });
    }
}