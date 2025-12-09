package org.dvsa.testing.framework.parallel;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "@target/rerun.txt",
        glue = {"org.dvsa.testing.framework.stepdefs"},
        plugin = {
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm:target/allure-results",
                "pretty",
                "html:target/cucumber-reports/retry-html",
                "json:target/cucumber-reports/retry-json/Cucumber.json"
        }
)
public class ReRunFailedCucumberTests {
}