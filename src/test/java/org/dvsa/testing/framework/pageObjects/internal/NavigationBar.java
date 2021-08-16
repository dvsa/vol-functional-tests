package org.dvsa.testing.framework.pageObjects.internal;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class NavigationBar extends BasePage {

    private static String ADMIN_BUTTON = "ul > li.admin__title";
    private static String OPEN_ADMIN_MENU = ADMIN_BUTTON + ".active";

    private static String ADMIN_MENU_ITEM_TEMPLATE = "//div[@class='admin__menu']//a[contains(text(),'%s')]/..";

    public static void openAdminPanel() {
        if (isAdminPanelClosed()) {
            clickAdministratorButton();
            waitForElementToBePresent("//ul/li[@class='admin__title active']");
        }
    }

    private static boolean isAdminPanelClosed() {
        return !isElementPresent(OPEN_ADMIN_MENU, SelectorType.CSS);
    }

    public static void clickAdministratorButton() {
        click(ADMIN_BUTTON, SelectorType.CSS);
    }

    public static void clickAdministratorList(@NotNull AdminOption option) {
        openAdminPanel();
        scrollAndClick(String.format(ADMIN_MENU_ITEM_TEMPLATE, option.toString()), SelectorType.XPATH);
    }

    public static void verifyOptionInList(@NotNull AdminOption option) {
        openAdminPanel();
        untilElementIsPresent(
                String.format(ADMIN_MENU_ITEM_TEMPLATE, option.toString()),
                SelectorType.XPATH, Duration.MEDIUM, TimeUnit.SECONDS
        );
    }
}
