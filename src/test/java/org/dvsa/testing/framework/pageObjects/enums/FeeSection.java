package org.dvsa.testing.framework.pageObjects.enums;

import org.jetbrains.annotations.NotNull;

public enum FeeSection {

    ApplicationDate("Application date"),
    ApplicationReference("Application reference"),
    ApplicationStatus("Application status"),
    PermitType("Permit type"),
    PermitYear("Permit year"),
    NumberOfPermits("Number of permits"),
    ApplicationFeePerPermit("Application fee per permit"),
    TotalApplicationFeeToBePaid("Total application fee to be paid"),
    TotalFeeToBePaid("Total fee to be paid"),
    TotalFee("Total fee");

    private String section;

    FeeSection(@NotNull String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return section;
    }

}
