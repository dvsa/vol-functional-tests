package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PageChecks extends BasePage implements En {

    public PageChecks(World world) {
        And("^on self serve the withdraw application link is present on \"([^\"]*)\"$", (String page) -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.UIJourneySteps.navigateToSelfServePage(page,"view");
            assertTrue(isElementPresent("//div//a[text()='Withdraw application']", SelectorType.XPATH));
        });
        Then("^on self serve the withdraw application link is not present on \"([^\"]*)\"$", (String page) -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.UIJourneySteps.navigateToSelfServePage(page,"view");
            assertFalse(isElementPresent("//div//a[text()='Withdraw application']", SelectorType.XPATH));
        });
        Then("^the \"([^\"]*)\" document is produced automatically$", (String documentType) -> {
            clickByLinkText("Docs & attachments");
            assertTrue(checkForPartialMatch(documentType));
        });
    }
}