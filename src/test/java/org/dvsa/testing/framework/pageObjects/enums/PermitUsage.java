package org.dvsa.testing.framework.pageObjects.enums;

import activesupport.number.Int;

public enum PermitUsage {
    CROSS_TRADE_ONLY("Cross-trade only"),
    TRANSIT_ONLY("Transit only"),
    BOTH_TRANSIT_AND_CROSSTRADE("For both transit and cross-trade");

    private String name;

    PermitUsage(String name) {
        this.name = name;
    }

    public static org.dvsa.testing.framework.pageObjects.enums.PermitUsage random(){
        return org.dvsa.testing.framework.pageObjects.enums.PermitUsage.values()[Int.random(0, org.dvsa.testing.framework.pageObjects.enums.PermitUsage.values().length - 1)];
    }

    @Override
    public String toString() {
        return name;
    }

}
