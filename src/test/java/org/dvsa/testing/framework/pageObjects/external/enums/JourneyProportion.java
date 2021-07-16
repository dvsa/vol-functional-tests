package org.dvsa.testing.framework.pageObjects.external.enums;

import activesupport.number.Int;

public enum JourneyProportion {

    LessThan60Percent("Less than 60%"),
    From60To90Percent("60% to 90%"),
    MoreThan90Percent("More than 90%");

    private String label;

    JourneyProportion(String label) {
        this.label = label;
    }

    public static org.dvsa.testing.framework.pageObjects.external.enums.JourneyProportion random() {
        int index = Int.random(0, org.dvsa.testing.framework.pageObjects.external.enums.JourneyProportion.values().length - 1);
        return org.dvsa.testing.framework.pageObjects.external.enums.JourneyProportion.values()[index];
    }

    @Override
    public String toString() {
        return label;
    }

}
