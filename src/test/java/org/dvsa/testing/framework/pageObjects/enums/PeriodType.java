package org.dvsa.testing.framework.pageObjects.enums;

import java.util.Arrays;

public enum PeriodType {
    BilateralCabotagePermitsOnly("Bilaterals cabotage permits only"),
    BilateralsStandardPermitsNoCabotage("Bilaterals standard permits no cabotage"),
    BilateralsStandardAndCabotagePermits("Bilaterals standard and cabotage permits"),
    BilateralsTurkey("Bilaterals Turkey"),
    BilateralsUkraine("Bilaterals Ukraine"),
    MoroccoStandardMultipleJourney("Standard multiple journey permit for 2021"),
    MoroccoStandardSingleJourney("Standard single journey permit"),
    MoroccoHorsContingency("Hors Contingent single journey permit"),
    MoroccoEmptyEntry("Empty Entry single journey permit"),
    ShortTermECMTAPSGWithoutSectors("APSG without sectors"),
    ShortTermECMTAPSGWithSectors("APSG with sectors");

    private String label;

    private PeriodType(String label) {
        this.label = label;
    }

    public static org.dvsa.testing.framework.pageObjects.enums.PeriodType toEnum(String shortterm ) {
        return Arrays.stream(values()).filter((s) -> {
            return s.toString().equalsIgnoreCase(shortterm.trim());
        }).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public String toString() {
        return this.label;
    }
}