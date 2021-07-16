package org.dvsa.testing.framework.pageObjects.internal.details;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;


public class BaseDetailsPage extends BasePage {

    private static final String BUTTON_TEMPLATE = "//a[contains(text(), '%s')]";

    public static void untilHeadingStatusIs(PermitStatus status) {
        untilElementWithText(".page-header span.status", SelectorType.CSS, status.toString(), ChronoUnit.SECONDS, Duration.LONG);
    }

    public static void curtail() {
        scrollAndClick(String.format(BUTTON_TEMPLATE, "Curtail"), SelectorType.XPATH);
    }

    public static void suspend() {
        scrollAndClick(String.format(BUTTON_TEMPLATE, "Suspend"), SelectorType.XPATH);
    }

    public static class Tab {

        private static final String TAB_SELECT_TEMPLATE = "//ul[@class='horizontal-navigation']/descendant::*[text()='%s']";

        public static boolean hasTab(DetailsTab detailsTab){
            return isElementPresent(String.format(TAB_SELECT_TEMPLATE, detailsTab.toString()), SelectorType.XPATH);
        }

        public static void select(DetailsTab detailsTab) {
            String selector = String.format(TAB_SELECT_TEMPLATE, detailsTab.toString());
            untilElementIsPresent(selector, SelectorType.XPATH, Duration.LONG, TimeUnit.SECONDS);
            waitAndClick(selector,SelectorType.XPATH);
        }

    }

}
