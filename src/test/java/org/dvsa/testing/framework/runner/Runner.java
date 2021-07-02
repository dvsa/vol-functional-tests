package org.dvsa.testing.framework.runner;

import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;


@CucumberOptions( features = {"src/test/resources/features"},
        glue = {"org.dvsa.testing.framework.stepdefs"}
)
public class Runner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}