package org.dvsa.testing.framework.pageObjects.internal.admin.permits;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.PermitsSidebarItem;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.concurrent.TimeUnit;


public class SideBar extends BasePage{

    public static void select(PermitsSidebarItem section) {
        String selector = String.format("//ul[@class='vertical-navigation']/li/descendant-or-self::*[contains(text(),'%s')]", section.toString());
        untilElementIsPresent(selector, SelectorType.XPATH, Duration.SHORT, TimeUnit.SECONDS);
        scrollAndClick(selector, SelectorType.XPATH);
    }
}
