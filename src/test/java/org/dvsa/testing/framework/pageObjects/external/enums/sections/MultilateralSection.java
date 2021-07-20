package org.dvsa.testing.framework.pageObjects.external.enums.sections;

public enum MultilateralSection implements Section {
    PermitType("Permit type"),
    Licence("Licence"),
    NumberOfPermits("Number of permits");

    private String sectionTitle;

    MultilateralSection(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    @Override
    public String toString() {
            return sectionTitle;
        }
}
