package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PageChecks extends BasePage implements En {
    private final World world;

    public PageChecks (World world) {this.world=world;}

        @And("on self serve the withdraw application link is present on {string}")
        public void onSelfServeTheWithdrawApplicationLinkIsPresentOn(String page) {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage(page,"View");
            assertTrue(isElementPresent("//div//a[text()='Withdraw application']", SelectorType.XPATH));
        }
        @Then("on self serve the withdraw application link is not present on {string}")
            public void onSelfServeTheWithdrawApplicationLinkIsNotPresentOn(String page) {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage(page,"View");
            assertFalse(isElementPresent("//div//a[text()='Withdraw application']", SelectorType.XPATH));
        }
        @Then("the {string} document should be generated")
            public void documentShouldBeGenerated(String documentType) {
            if (isElementPresent("//a[contains(text(),'Docs & attachments')]",SelectorType.XPATH)) {
                clickByLinkText("Docs & attachments");
            }
            assertTrue(checkForPartialMatch(documentType));
        };
    }
