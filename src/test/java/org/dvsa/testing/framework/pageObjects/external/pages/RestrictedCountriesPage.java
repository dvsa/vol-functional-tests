package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.enums.RestrictedCountry;

public class RestrictedCountriesPage extends BasePermitPage {

    private static String COUNTRY_TEMPLATE = "//label[contains(text(), '%s')]/input";

    public static void deliverToRestrictedCountry(boolean answer) {
        int position = answer ? 1 : 2;
        scrollAndClick(String.format("(//*[@class='govuk-radios__item'])[%d]/input", position), SelectorType.XPATH);
    }

    public static void countries(RestrictedCountry... countries) {
        deliverToRestrictedCountry(true);

        for(RestrictedCountry country : countries) {
            scrollAndClick(String.format(COUNTRY_TEMPLATE, country.toString()), SelectorType.XPATH);
        }
    }

    public static String getAdvisoryText() {
        return getText("//div[@class='govuk-inset-text']", SelectorType.XPATH);
    }
}
