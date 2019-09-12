package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

public class WebDav extends BasePage implements En {

    private EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    public WebDav(World world) {
        Then("^I check the document has been generated$", () -> {

//            world.genericUtils.writeLineToFile();
//            world.genericUtils.readLineFromFile();
        });
        And("^i write to a file the necessary information$", () -> {

        });
        And("^I check the change has been made to the document$", () -> {

        });
    }
}
