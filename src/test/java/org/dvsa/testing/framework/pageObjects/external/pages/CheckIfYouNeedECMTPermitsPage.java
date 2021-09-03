package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class CheckIfYouNeedECMTPermitsPage extends BasePermitPage{

    private static String NEED_ECMT_PERMITS = "#qaElement";
    private static String SELECTED_NEED_ECMT_PERMITS = "label.selected " + NEED_ECMT_PERMITS;

    final public static String RESOURCE = "permits/application/\\d+/check-ecmt-needed/";

    public static void checkNeedECMTPermits() {
        if (!isElementPresent(SELECTED_NEED_ECMT_PERMITS, SelectorType.CSS)) {
            scrollAndClick(NEED_ECMT_PERMITS);
        }
    }

    public static boolean hasErrorMessagePresent() {
        return isTextPresent ("Confirm that you need to apply for ECMT permits");
    }
}
