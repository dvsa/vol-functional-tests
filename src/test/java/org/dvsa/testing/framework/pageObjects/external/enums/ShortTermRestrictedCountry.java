package org.dvsa.testing.framework.pageObjects.external.enums;

import activesupport.number.Int;
import org.dvsa.testing.framework.pageObjects.external.enums.RestrictedCountry;

public enum ShortTermRestrictedCountry {

    Greece("Greece"),
    Hungary("Hungary"),
    Italy("Italy"),
    RussianFederation("Russian Federation");

    private String country;

    ShortTermRestrictedCountry(String country){
        this.country = country;
    }

    public static org.dvsa.testing.framework.pageObjects.external.enums.ShortTermRestrictedCountry random() {
        int index = Int.random(0, RestrictedCountry.values().length - 1);
        return org.dvsa.testing.framework.pageObjects.external.enums.ShortTermRestrictedCountry.values()[index];
    }

    @Override
    public String toString() {
        return country;
    }

}
