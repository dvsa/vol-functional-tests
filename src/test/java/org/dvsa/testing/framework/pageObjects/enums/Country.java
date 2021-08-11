package org.dvsa.testing.framework.pageObjects.enums;

import java.util.stream.Stream;

public enum Country {
    Austria,
    Belgium,
    Belarus,
    Bulgaria,
    Croatia,
    Cyprus,
    CzechRepublic("Czech Republic"),
    Denmark,
    Estonia,
    Finland,
    France,
    Germany,
    Georgia,
    Greece,
    Hungary,
    Iceland,
    Ireland,
    Italy,
    Latvia,
    Liechtenstein,
    Lithuania,
    Luxembourg,
    Kazakhstan,
    Malta,
    Morocco,
    Netherlands,
    Norway,
    Russia,
    Poland,
    Portugal,
    Romania,
    Slovakia,
    Slovenia,
    Spain,
    Sweden,
    Tunisia,
    Turkey,
    Ukraine,
    UnitedKingdom("United Kingdom");

    private String name;

    Country() {
    }

    Country(String name) {
        this.name = name;
    }

    public static org.dvsa.testing.framework.pageObjects.enums.Country toEnum(String country) {
        return Stream.of(org.dvsa.testing.framework.pageObjects.enums.Country.values())
                .filter(countryEnum -> countryEnum.toString().equalsIgnoreCase(country))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported enum value: " + country));
    }

    @Override
    public String toString() {
        return (name == null) ? this.name() : name;
    }
}
