package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.UpdateLicenceAPI;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.By;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PageChecks extends BasePage implements En {

    public PageChecks(World world) {
        And("^on self serve the withdraw application link is present$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.UIJourneySteps.navigateToApplicationOrVariationalPage();
            checkTextisPresent("Withdraw application");
        });
        Then("^on self serve the withdraw application link is not present$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.UIJourneySteps.navigateToApplicationOrVariationalPage();
            assertFalse(isTextPresent("Withdraw application", 5));
        });
    }
}