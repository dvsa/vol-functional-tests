package org.dvsa.testing.framework.pageObjects.external.enums.sections;

import org.dvsa.testing.framework.pageObjects.external.enums.sections.Section;

public enum ApplicationSection implements Section {

    Licence("Licence"),
    CheckIfYouNeedECMTPermits("Check if you need ECMT permits"),
    Cabotage("Cabotage"),
    CertificatesRequired("Certificates required"),
    RestrictedCountries("Transportation of goods to Austria, Greece, Hungary, Italy or Russia"),
    NumberOfPermits("Number of permits"),
    Euro6("Euro emission standard");

    private String sectionTitle;

    ApplicationSection(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    @Override
    public String toString() {
        return sectionTitle;
    }

}
