package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;

public class WebDav extends BasePage implements En {
    public WebDav(World world) {
        Then("^I check the document has been generated$", () -> {
//            GenericUtils.writeLineToFile();
        });
        And("^i write to a file the necessary information$", () -> {
        });
        And("^I check the change has been made to the document$", () -> {

        });
    }
}
