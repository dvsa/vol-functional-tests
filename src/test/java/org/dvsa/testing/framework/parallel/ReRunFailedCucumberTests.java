package org.dvsa.testing.framework.parallel;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "@target/rerun.txt",
        glue = {"org.dvsa.testing.framework.stepdefs"},
        plugin = {
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "pretty",
                "html:target/cucumber-reports/retry-html",
                "json:target/cucumber-reports/retry-json/Cucumber.json",
                "junit:target/cucumber-reports/retry-junit/Cucumber.xml"
        }
)
public class ReRunFailedCucumberTests {
}