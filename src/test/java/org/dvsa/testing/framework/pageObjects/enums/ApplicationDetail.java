package org.dvsa.testing.framework.pageObjects.enums;

public enum ApplicationDetail {

    Status("Application status"),
    PermitType("Permit type"),
    ReferenceNumber("Application reference"),
    ApplicationDate("Application date"),
    PermitsRequired("Number of permits required"),
    ApplicationFeePaid("Total application fee paid");

    private String key;

    ApplicationDetail(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

}
