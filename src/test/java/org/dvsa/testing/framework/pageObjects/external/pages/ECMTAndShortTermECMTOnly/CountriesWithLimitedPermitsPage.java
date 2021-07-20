package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class CountriesWithLimitedPermitsPage extends BasePermitPage {

    public static void noCountriesWithLimitedPermits() {
        waitAndClick("//label[contains(text(),'No')]", SelectorType.XPATH);
        saveAndContinue();
    }
}