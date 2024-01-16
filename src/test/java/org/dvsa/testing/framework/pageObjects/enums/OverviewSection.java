package org.dvsa.testing.framework.pageObjects.enums;

import java.util.Arrays;

public enum OverviewSection {
    AnnualTripsAbroad("Annual trips abroad"),
    BilateralDeclaration("Read declaration"),
    Cabotage("Cabotage"),
    Countries("Select countries you need permits for"),
    CertificateOfComplianceNumber("Certificate of Compliance number"),
    CertificatesRequired("Certificates required"),
    CheckIfYouNeedPermits("Check if you need ECMT permits"),
    CheckYourAnswers("Check your answers"),
    CountriesWithLimitedPermits("Countries with limited permits"),
    Declaration("Declaration"),
    EditCountrySelection("Edit country selection"),
    EngineNumber("Engine number"),
    EngineType("Engine type"),
    EuroEmissionStandards("Euro emission standards"),
    HowWillYouUseThePermits("How will you use the permits"),
    LicenceNumber("Licence number"),
    MakeAndModel("Make and model"),
    MOTExpiryDate("MOT expiry date"),
    NumberOfPaymentsRequired("Number of permits required"),
    NumberOfPermits("Number of permits required"),
    RegistrationNumber("Registration number"),
    RemovalsEligibility("Removals eligibility"),
    SubmitAndPay("Submit and pay"),
    VehicleIdentificationNumber("Vehicle identification number");

    private String section;

    OverviewSection (String section) {
        this.section = section;
    }

    public static org.dvsa.testing.framework.pageObjects.enums.OverviewSection toEnum(String section) {
        return Arrays.stream(org.dvsa.testing.framework.pageObjects.enums.OverviewSection.values()).filter(s -> s.toString().equalsIgnoreCase(section.trim()))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {
        return section;
    }
}
