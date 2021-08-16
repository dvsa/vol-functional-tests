package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckCorrespondence extends BasePage implements En {
    public CheckCorrespondence(World world) {
        And("^i have logged in to self serve$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        });
        When("^i open the documents tab$", () -> {
           click("//a[@href='/correspondence/']", SelectorType.XPATH);
        });
        Then("^all correspondence for the application should be displayed$", () -> {
            waitForElementToBePresent("//table");
            assertTrue(findElement("//table",SelectorType.XPATH,300).getText().contains(world.applicationDetails.getLicenceNumber()));
        });
    }
}