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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PageChecks extends BasePage implements En {

    public PageChecks(World world) {
        And("^on self serve the withdraw application link is present on \"([^\"]*)\"$", (String page) -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage(page,"View");
            assertTrue(isElementPresent("//div//a[text()='Withdraw application']", SelectorType.XPATH));
        });
        Then("^on self serve the withdraw application link is not present on \"([^\"]*)\"$", (String page) -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage(page,"View");
            assertFalse(isElementPresent("//div//a[text()='Withdraw application']", SelectorType.XPATH));
        });
        Then("^the \"([^\"]*)\" document should be generated$", (String documentType) -> {
            if (isElementPresent("//a[contains(text(),'Docs & attachments')]",SelectorType.XPATH)) {
                clickByLinkText("Docs & attachments");
            }
            assertTrue(checkForPartialMatch(documentType));
        });
    }
}