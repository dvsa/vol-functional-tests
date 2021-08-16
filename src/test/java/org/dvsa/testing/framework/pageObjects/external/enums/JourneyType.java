package org.dvsa.testing.framework.pageObjects.external.enums;

import activesupport.number.Int;

import java.util.stream.Stream;

public enum JourneyType {

    MultipleJourneys("Multiple journeys"),
    SingleJourneys("Single journeys");

    private String type;

    private JourneyType(String type) {
        this.type = type;
    }


    public static org.dvsa.testing.framework.pageObjects.external.enums.JourneyType toEnum(String journeyType) {
        return Stream.of(org.dvsa.testing.framework.pageObjects.external.enums.JourneyType.values())
                .filter(JourneyType -> JourneyType.toString().equalsIgnoreCase(journeyType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported enum value: " + journeyType));
    }

    public static org.dvsa.testing.framework.pageObjects.external.enums.JourneyType random(){
        return values()[Int.random(0, org.dvsa.testing.framework.pageObjects.external.enums.JourneyType.values().length - 1)];
    }

    public String toString() {
        return this.type;
    }

}
