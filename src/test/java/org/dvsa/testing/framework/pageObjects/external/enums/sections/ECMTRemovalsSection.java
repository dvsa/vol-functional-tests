package org.dvsa.testing.framework.pageObjects.external.enums.sections;

import org.dvsa.testing.framework.pageObjects.external.enums.sections.Section;

public enum ECMTRemovalsSection implements Section {
    PermitType("Permit type"),
    Licence("Licence"),
    RemovalsEligibility("Removal permit eligibility"),
    Cabotage("Cabotage"),
    CertificatesRequired("Certificates required"),
    PermitStartDate("Permit start date"),
    NumberOfPermits("Number of permits");

    private String sectionTitle;

    ECMTRemovalsSection(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    @Override
    public String toString() {
        return sectionTitle;
    }
}
