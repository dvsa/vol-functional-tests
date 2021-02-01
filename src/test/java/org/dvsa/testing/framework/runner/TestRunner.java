//package org.dvsa.testing.framework.runner;
//
//import Injectors.World;
//import activesupport.IllegalBrowserException;
//import activesupport.driver.Browser;
//import cucumber.api.testng.AbstractTestNGCucumberTests;
//import org.testng.annotations.*;
//
//import cucumber.api.CucumberOptions;
//
//import java.net.MalformedURLException;
//
//@CucumberOptions(features = {"src/test/resources/features"},
//        glue = {"org.dvsa.testing.framework.stepdefs"})
//
//public class TestRunner extends AbstractTestNGCucumberTests {
//
//    private World world;
//
//    String browserStackUser = world.configuration.config.getString("browserStackUser");
//    String browserStackKey = world.configuration.config.getString("browserStackKey");
//
//    public TestRunner( World world) {
//        this.world = world;
//    }
//
//    @BeforeClass
//    public void setBrowserStackCredentials(){
//        Browser.setGridURL(String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub",browserStackUser,browserStackKey));
//    }
//
//    @BeforeTest(alwaysRun = true)
//    @Parameters({ "browser", "version", "platform" })
//    public void setUpClass(String browser, String version, String platform) {
//        System.out.println("===BROWSER==" + browser);
//        System.out.println("===VERSION==" + version);
//        Browser.setBrowserVersion(version);
//        System.setProperty("browser",browser);
//    }
//
//    @DataProvider(parallel = true)
//    public Object[][] scenarios() {
//        return super.scenarios();
//    }
//
//    @AfterClass
//    public void tearDown() throws MalformedURLException, IllegalBrowserException {
//        System.out.println("finished");
////        Browser.navigate().quit();
//    }
//}