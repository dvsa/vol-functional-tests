package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class CountriesWithLimitedPermitsPage extends BasePermitPage {

    public static void chooseNoCountriesWithLimitedPermits() {
        BasePermitJourney.setCountriesWithLimitedPermitsChoice(false);
        waitAndClick("//label[contains(text(),'No')]", SelectorType.XPATH);
        saveAndContinue();
    }
}