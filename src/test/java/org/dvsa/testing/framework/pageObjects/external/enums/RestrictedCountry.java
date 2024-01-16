package org.dvsa.testing.framework.pageObjects.external.enums;

import activesupport.number.Int;

public enum RestrictedCountry {

    Austria("Austria"),
    Greece("Greece"),
    Hungary("Hungary"),
    Italy("Italy"),
    Russia("Russia");

    private String country;

    RestrictedCountry(String country){
        this.country = country;
    }

    public static org.dvsa.testing.framework.pageObjects.external.enums.RestrictedCountry random() {
        int index = Int.random(0, org.dvsa.testing.framework.pageObjects.external.enums.RestrictedCountry.values().length - 1);
        return org.dvsa.testing.framework.pageObjects.external.enums.RestrictedCountry.values()[index];
    }

    @Override
    public String toString() {
        return country;
    }

}
