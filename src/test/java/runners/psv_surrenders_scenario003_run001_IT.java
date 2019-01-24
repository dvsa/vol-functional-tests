package runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.qameta.allure.cucumber2jvm.AllureCucumber2Jvm"},
        features = {"src/test/resources/parallel/features/psv_surrenders_scenario003_run001_IT.feature"},
        glue = {"org.dvsa.testing.framework.stepdefs"}
)
public class psv_surrenders_scenario003_run001_IT {
}

// Generated by Cucable from src/test/resources/parallel/cucable.template

