package org.dvsa.testing.framework.stepdefs;

import activesupport.driver.Browser;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import org.dvsa.testing.framework.document.XMLParser;

import java.util.Collection;

public class ParallelBrowser {

    public void chooseBrowser(Scenario scenario) {
        XMLParser parser = new XMLParser();
        parser.getElementsByTagName();
        Collection<String> tags = scenario.getSourceTagNames();

        parser.getElementsByTagName();
        Browser.setGridURL("https://wondersonchideya2:q5bs9xMvduGuJbsbq61k@hub-cloud.browserstack.com/wd/hub");

        for (String tag : tags) {
            if (tag.equals("@cross-browser-test")) {
                for (String browser : parser.browser) {
                    System.setProperty("browser", browser);
                    Browser.setBrowserVersion("87.0");
                }
            }
        }

    }
}
