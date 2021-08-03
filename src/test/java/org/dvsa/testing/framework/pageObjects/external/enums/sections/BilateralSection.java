package org.dvsa.testing.framework.pageObjects.external.enums.sections;

public enum BilateralSection implements Section {
    Licence("Licence"),
    CountriesSelected("Countries selected"),
    QuestionsAnsweredFor("Questions answered for"),
    PermitType("Permit type"),
    NumberOfPermits("Number of permits"),
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
