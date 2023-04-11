package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PageChecks extends BasePage {
    private final World world;

    public PageChecks (World world) {this.world=world;}

        @And("on self serve the withdraw application link is present on {string}")
        public void onSelfServeTheWithdrawApplicationLinkIsPresentOn(String page) {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage(page, SelfServeSection.VIEW);
            assertTrue(isElementPresent("//div//a[text()='Withdraw application']", SelectorType.XPATH));
        }
        @Then("on self serve the withdraw application link is not present on {string}")
            public void onSelfServeTheWithdrawApplicationLinkIsNotPresentOn(String page) {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage(page,SelfServeSection.VIEW);
            assertFalse(isElementPresent("//div//a[text()='Withdraw application']", SelectorType.XPATH));
        }
        @Then("the {string} document should be generated")
            public void documentShouldBeGenerated(String documentType) {
            if (isElementPresent("//a[contains(text(),'Docs & attachments')]",SelectorType.XPATH)) {
                clickByLinkText("Docs & attachments");
            }
            assertTrue(checkForPartialMatch(documentType));
        }
    }
