package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class YearSelectionPage extends BasePermitPage {

    public static void selectShortTermValidityPeriod() {
        if (isElementPresent("//input[@type='radio']", SelectorType.XPATH)) {
            String selector = String.format("//input[@id='year']|//input[@value='2021']");
            scrollAndClick(selector, SelectorType.XPATH);
        }
        saveAndContinue();
    }

    public static boolean isShortTermWarningMessagePresent() {
        return isTextPresent("You can only use Short-term permits for 30 consecutive days from the start date you choose, during the calendar year they've been issued for.")
        && isTextPresent("Only one application per licence will be accepted. You will need to decide if you want permits for Euro 5 or Euro 6 vehicles. You cannot apply for both emission standards on one licence.");
    }

    public static void selectECMTValidityPeriod() {
        if (isYearChoicePresent()) {
            String selector = String.format("//label[contains(text(),'2021')]");
            scrollAndClick(selector, SelectorType.XPATH);
        }
        saveAndContinue();
    }

    public static boolean isYearChoicePresent() {
        return isElementPresent("//input[@type='radio']", SelectorType.XPATH);
    }
}
