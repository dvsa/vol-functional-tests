package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
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

    public static void selectECMTValidityPeriod() {
        if (isYearChoicePresent()) {
            String year = "2021";
            String selector = String.format("//label[contains(text(),'%s')]", year);
            scrollAndClick(selector, SelectorType.XPATH);
            BasePermitJourney.setYearChoice(year);
        }
        saveAndContinue();
    }

    public static boolean isYearChoicePresent() {
        return isElementPresent("//input[@type='radio']", SelectorType.XPATH);
    }
}
