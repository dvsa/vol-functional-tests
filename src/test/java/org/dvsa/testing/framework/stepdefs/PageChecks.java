package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PageChecks extends BasePage implements En {

    public PageChecks(World world) {
        And("^on self serve the withdraw application link is present$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            clickByLinkText("GOV.UK");
            clickByLinkText(world.createLicence.getApplicationNumber());
            checkTextisPresent("Withdraw application");
        });
        Then("^on self serve the withdraw application link is not present$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            clickByLinkText("GOV.UK");
            clickByLinkText(world.createLicence.getApplicationNumber());
            assertFalse(isTextPresent("Withdraw application", 5));
        });
    }
}