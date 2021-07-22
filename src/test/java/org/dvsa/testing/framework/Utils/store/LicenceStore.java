package org.dvsa.testing.framework.Utils.store;

import activesupport.string.Str;
import org.dvsa.testing.framework.Utils.store.permit.AnnualBilateralStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.PermitUsage;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyProportion;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.dvsa.testing.framework.pageObjects.external.enums.Sector;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesPage;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LicenceStore extends OperatorStore {

    private String licenceId;
    private String referenceNumber;
    private String licenceNumber;
    private int numberOfAuthorisedVehicles;
    private int numberOfAuthorisedTrailers;
    private List<FeesPage.Fee> fees = new ArrayList<>();

    private List<AnnualMultilateralStore> annualMultilateralStores = new ArrayList<>();
    private List<AnnualBilateralStore> annualBilateral = new ArrayList<>();
    private Ecmt ecmt = new Ecmt(); // Can only have a single ecmt application per licence

    public String getLicenceId() {
        return licenceId;
    }

    public LicenceStore setLicenceId(String licenceId) {
        this.licenceId = licenceId;
        return this;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public LicenceStore setReferenceNumber(String referenceNumber){
        this.referenceNumber = referenceNumber;
        setLicenceNumber(Str.find("\\w{2}\\d{7}", referenceNumber).get());
        ecmt.setReferenceNumber(referenceNumber);
        return this;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public LicenceStore setLicenceNumber(@NotNull String licenceNumber) {
        this.licenceNumber = licenceNumber;
        return this;
    }

    public int getNumberOfAuthorisedVehicles() {
        return numberOfAuthorisedVehicles;
    }

    public void setNumberOfAuthorisedVehicles(int numberOfAuthorisedVehicles) {
        this.numberOfAuthorisedVehicles = numberOfAuthorisedVehicles;
    }

    public int getNumberOfAuthorisedTrailers() {
        return numberOfAuthorisedTrailers;
    }

    public void setNumberOfAuthorisedTrailers(int numberOfAuthorisedTrailers) {
        this.numberOfAuthorisedTrailers = numberOfAuthorisedTrailers;
    }

    public List<FeesPage.Fee> getFees() {
        return fees;
    }

    public void setFees(List<FeesPage.Fee> fees) {
        this.fees = fees;
    }

    public Ecmt getEcmt() {
        return ecmt;
    }


    public Optional<AnnualMultilateralStore> getLatestAnnualMultilateral() {
        AnnualMultilateralStore permit;
        int numPermits = annualMultilateralStores.size();

        return numPermits > 0 ?
                Optional.of(annualMultilateralStores.get(numPermits - 1)) :
                Optional.empty();
    }

    public boolean hasPermit(AnnualMultilateralStore permit) {
        return annualMultilateralStores.stream().anyMatch((p) -> p.equals(permit));
    }

    public LicenceStore addAnnualMultilateral(AnnualMultilateralStore permit) {
        if (!hasPermit(permit))
            annualMultilateralStores.add(permit);
        return this;
    }

    public Optional<AnnualBilateralStore> getLatestAnnualBilateral() {
        AnnualBilateralStore permit = null;

        if (annualBilateral.size() > 0) {
            permit = annualBilateral.get(annualBilateral.size() - 1);
        }

        return permit == null ? Optional.empty() : Optional.of(permit);
    }

    public void setAnnualBilateral(@NotNull AnnualBilateralStore annualBilateral) {
        if (!hasAnnualBilateralPermit(annualBilateral))
            this.annualBilateral.add(annualBilateral);
    }

    private boolean hasAnnualBilateralPermit(AnnualBilateralStore permit) {
        return annualBilateral.stream()
                .anyMatch(
                        (currentPermit) -> currentPermit.equals(permit)
                );
    }

    public class Ecmt {
        private PermitType type;
        private PeriodType shortTermType;
        private boolean certificatesRequired;
        private String referenceNumber;
        private boolean euro6;
        private boolean cabotage;
        private boolean restrictedCountries;
        // ALL booleans are defaulting as false. Only Booleans default as null. SO THESE ARE PASSING DUE TO LUCK.
        // STORE IN PAGE CLASS INSTEAD.
        private int numberOfPermits;
        private LocalDateTime submitDate;


        public String getReferenceNumber() {
            return Str.find("(?<=\\w{2}\\d{7} / )\\d+", referenceNumber).get();
        }

        public Optional<PermitType> getType() {
            return type == null ? Optional.empty() : Optional.of(type);
        }

        public Ecmt setType(PermitType type) {
            this.type = type;
            return this;
        }

        public Ecmt setShortTermType(PeriodType shortTermType){
            this.shortTermType =shortTermType ;
            return this;
        }


        public String getFullReferenceNumber(){
            return referenceNumber;
        }

        public Ecmt setReferenceNumber(String referenceNumber) {
            this.referenceNumber = referenceNumber;
            return this;
        }

        public Ecmt setEuro6(boolean euro6){
            this.euro6 = euro6;
            return this;
        }

        public boolean isEuro6() {
            return euro6;
        }


        public boolean isCertificatesRequired(){return certificatesRequired;}


        public Ecmt setCertificatesRequired(boolean certificatesRequired){
            this.certificatesRequired = certificatesRequired;
            return this;
        }

        public boolean isCabotage() {
            return cabotage;
        }

        public Ecmt setCabotage(boolean cabotage) {
            this.cabotage = cabotage;
            return this;
        }

        public boolean hasRestrictedCountries() {
            return restrictedCountries;
        }

        public Ecmt setRestrictedCountries(boolean restrictedCounties) {
            this.restrictedCountries = restrictedCounties;
            return this;
        }

        public  int getNumberOfPermits() {
            return numberOfPermits;
        }

        public Ecmt setNumberOfPermits(int numberOfPermits) {
            this.numberOfPermits = numberOfPermits;
            return this;
        }

        public void setSubmitDate(LocalDateTime submitDate) {
            this.submitDate = submitDate;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LicenceStore that = (LicenceStore) o;
        return Objects.equals(getLicenceNumber(), that.getLicenceNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLicenceId(), getReferenceNumber());
    }
}
