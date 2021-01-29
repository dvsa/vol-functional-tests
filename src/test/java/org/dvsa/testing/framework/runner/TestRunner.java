package org.dvsa.testing.framework.runner;

import activesupport.driver.Browser;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.*;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = {"src/test/resources/features"},
        glue = {"org.dvsa.testing.framework.stepdefs"})

public class TestRunner extends AbstractTestNGCucumberTests {

    public TestRunner() {
        Browser.setGridURL("https://username:password@hub-cloud.browserstack.com/wd/hub");
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({ "browser", "version", "platform" })
    public void setUpClass(@Optional("edge") String browser, @Optional("87.0") String version, @Optional String platform) {
        System.out.println("===BROWSER==" + browser);
        System.out.println("===VERSION==" + version);
        Browser.setBrowserVersion(version);
        System.setProperty("browser",browser);
    }

    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}