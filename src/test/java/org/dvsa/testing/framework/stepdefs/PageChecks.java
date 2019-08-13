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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PageChecks extends BasePage implements En {

    public PageChecks(World world) {
        And("^on self serve the withdraw application link is present on \"([^\"]*)\"$", (String page) -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.UIJourneySteps.navigateToSelfServePage(page,"view");
            checkTextisPresent("Withdraw application");
        });
        Then("^on self serve the withdraw application link is not present on \"([^\"]*)\"$", (String page) -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.UIJourneySteps.navigateToSelfServePage(page,"view");
            assertFalse(isTextPresent("Withdraw application", 5));
        });
        Then("^the \"([^\"]*)\" document is produced automatically$", (String documentName) -> {
            clickByLinkText("Docs & attachments");
            assertTrue(checkForPartialMatch(documentName));
        });
    }
}