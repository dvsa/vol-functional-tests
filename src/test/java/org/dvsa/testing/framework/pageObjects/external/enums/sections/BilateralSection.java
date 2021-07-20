package org.dvsa.testing.framework.pageObjects.external.enums.sections;

public enum BilateralSection implements Section {
    PermitType("Permit type"),
    Licence("Licence selected"),
    Country("Select the country you are transporting goods to"),
    Permits("How many permits do you require?");

    private String sectionTitle;

    BilateralSection(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    @Override
    public String toString() {
            return sectionTitle;
        }
}
