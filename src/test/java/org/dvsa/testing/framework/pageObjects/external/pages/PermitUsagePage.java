package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.temporal.ChronoUnit;

public class PermitUsagePage extends BasePermitPage {

    public static final String RESOURCE_URL = "permits/application/\\d+/ipa/\\d+/bi-permit-usage";
    public static String journey;

    public static String getJourney() {
        return journey;
    }

    public static void setJourney(String journey) {
        org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage.journey = journey;
    }

    public static void untilOnPage() {
        untilUrlMatches(RESOURCE_URL, Duration.LONG, ChronoUnit.SECONDS);
    }

    public static void journeyType(JourneyType JourneyType) {
        if (isElementPresent("//input[@id='qaElement']", SelectorType.XPATH)) {
            scrollAndClick(String.format("//label[contains(text(), '%s')]/../input", JourneyType), SelectorType.XPATH);
            setJourney(JourneyType.toString());
            saveAndContinue();
        } else {
            String defaultJourney = getText("//form[@id='Qa']//fieldset//fieldset//div[contains(@class,'field')]",SelectorType.XPATH);
            if (defaultJourney.contains("single")) {
                String a = defaultJourney.substring(5,19)+"s";
                String singleuppercase = a.substring(0,1).toUpperCase() +a.substring(1);
                setJourney(singleuppercase);
            }
            else
            {String multiple = defaultJourney.substring(5,21)+"s";
                String multipleeuppercase = multiple.substring(0,1).toUpperCase() + multiple.substring(1);
                setJourney(multipleeuppercase);
            }
            saveAndContinue();
        }
    }

    public static String getDefaultPeriodOption() {
        return getText("//p[contains(text(),'Only single journey permits are available for this')]", SelectorType.XPATH);
    }
}
