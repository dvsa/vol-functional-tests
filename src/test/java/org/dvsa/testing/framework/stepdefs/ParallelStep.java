package org.dvsa.testing.framework.stepdefs;

import activesupport.driver.Browser;
import cucumber.api.Scenario;
import cucumber.api.java.en.Given;
import org.dvsa.testing.framework.document.XMLParser;

import java.util.Collection;

public class ParallelStep {
    @Given("i want to test on multiple browsers")
    public void iWantToTestOnMultipleBrowsers() {
        XMLParser parser = new XMLParser();
        parser.getElementsByTagName();

        parser.getElementsByTagName();
        Browser.setGridURL("https://wondersonchideya2:q5bs9xMvduGuJbsbq61k@hub-cloud.browserstack.com/wd/hub");

        for (String browser : parser.browser) {
            System.out.println("browser===== " + browser);
            System.setProperty("browser", browser);
            Browser.setOsVersion("10");
            Browser.setBrowserVersion("84");
        }
    }
}
