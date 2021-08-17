package org.dvsa.testing.framework.pageObjects.external.enums;
import activesupport.number.Int;

public enum AnnualEcmtPermitUsage {
    CROSS_TRADE_ONLY("Journeys including cross trade movements"),
    TRANSIT_ONLY("Journeys that transit through EU Member States to other countries that are part of the ECMT system"),
    POINT_TO_POINT("Point to point journeys between the UK and EU Member States"),
    BOTH_TRANSIT_AND_CROSSTRADE("International journeys including both transit and cross trade");


    private String name;

    AnnualEcmtPermitUsage(String name) {
        this.name = name;
    }

    public static org.dvsa.testing.framework.pageObjects.external.enums.AnnualEcmtPermitUsage random(){
        return org.dvsa.testing.framework.pageObjects.external.enums.AnnualEcmtPermitUsage.values()[Int.random(0, org.dvsa.testing.framework.pageObjects.external.enums.AnnualEcmtPermitUsage.values().length - 1)];
    }

    @Override
    public String toString() {
        return name;
    }

}
