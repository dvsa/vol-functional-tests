package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
 import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckCorrespondence extends BasePage implements En {
    public CheckCorrespondence(World world) {
        And("^i have logged in to self serve$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
        });
        When("^i open the documents tab$", () -> {
           click("//a[@href='/correspondence/']", SelectorType.XPATH);
        });
        Then("^all correspondence for the application should be displayed$", () -> {
            waitForElementToBePresent("//table");
            assertTrue(findElement("//table",SelectorType.XPATH,30).getText().contains(world.createLicence.getLicenceNumber()));
        });
    }
}