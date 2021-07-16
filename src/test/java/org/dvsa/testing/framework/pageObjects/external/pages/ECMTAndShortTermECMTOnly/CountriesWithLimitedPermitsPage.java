package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.temporal.ChronoUnit;

public class CountriesWithLimitedPermitsPage extends BasePermitPage {

    public static void untilOnPage() {
        untilUrlMatches("/permits/application/\\d+/st-restricted-countries/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static boolean isAdvisoryTextPresent() {
        return isTextPresent("There is a very small number of permits available for these countries.")
        && isTextPresent("We cannot guarantee if you receive a permit that it will allow you to travel to these countries.")
        && isTextPresent("Short-term permits do not allow journeys to Austria.");
    }

    public static void noCountriesWithLimitedPermits() {
        waitAndClick("//label[contains(text(),'No')]", SelectorType.XPATH);
        saveAndContinue();
    }

}