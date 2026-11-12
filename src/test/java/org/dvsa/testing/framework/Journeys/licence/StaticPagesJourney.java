package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StaticPagesJourney extends BasePage {
    private final World world;

    public StaticPagesJourney(World world) {
        this.world = world;
    }

    public void openSelfServeSignIn() {
        world.selfServeNavigation.navigateToLoginPage();
    }

    public void followFooterLink(String path) {
        clickByXPath("//footer//a[contains(@href, '" + path + "')]");
        waitForUrlToContain(path, 10);
    }

    public void verifyPageContains(String expectedText) {
        waitForPageLoad();
        assertTrue(getText("//body", SelectorType.XPATH).contains(expectedText),
                "The destination page should contain: " + expectedText);
    }
}
