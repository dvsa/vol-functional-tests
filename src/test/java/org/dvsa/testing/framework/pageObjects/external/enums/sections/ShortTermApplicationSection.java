package org.dvsa.testing.framework.pageObjects.external.enums.sections;

public enum ShortTermApplicationSection implements Section {
    PermitType("Permit type"),
    Licence("Licence"),
    PermitsUsage("Permit usage"),
    Cabotage("Cabotage"),
    CertificatesRequired("Certificates required"),
    NumberOfPermits("Number of permits");

    private String sectionTitle;

    ShortTermApplicationSection(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    @Override
    public String toString() {
        return sectionTitle;
    }
} // Merge with overview