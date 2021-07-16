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
        private List<Country> restrictedCountriesName;
        private int numberOfPermits;
        private int numberOfTrips;
        private JourneyProportion internationalBusiness;
        private Sector sector;
        private LocalDateTime submitDate;
        private PermitUsage usage;
        private String journeyType;
        private List<JourneyType> journeyTypeName;
        private String noCabotage;


        public String getReferenceNumber() {
            return Str.find("(?<=\\w{2}\\d{7} / )\\d+", referenceNumber).get();
        }

        public Optional<PermitType> getType() {
            return type == null ? Optional.empty() : Optional.of(type);
        }

        public Optional<PeriodType> getShortTermType(){
            return shortTermType == null ? Optional.empty() : Optional.of(shortTermType);
        }

        public boolean hasType(PermitType type) {
            return getType().isPresent() && getType().get().equals(type);
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

        public String getNoCabotage()
        {return noCabotage;}

        public Ecmt setNoCabotage(String noCabotage){
            this.noCabotage = noCabotage;
            return this;
        }

        public String getJourneyType(){return journeyType;}

        public Ecmt setJourneyType(String journeyType){
            this.journeyType= journeyType;
            return this;
        }

        public boolean hasRestrictedCountries() {
            return restrictedCountries;
        }

        public Ecmt setRestrictedCountries(boolean restrictedCounties, List<Country> countries) {
            this.restrictedCountries = restrictedCounties;
            this.restrictedCountriesName = countries;
            return this;
        }

        public Ecmt setRestrictedCountries(List<Country> countries) {
            return setRestrictedCountries(true, countries);
        }

        public List<Country> getRestrictedCountriesName() {
            return restrictedCountriesName;
        }

        public  int getNumberOfPermits() {
            return numberOfPermits;
        }

        public Ecmt setNumberOfPermits(int numberOfPermits) {
            this.numberOfPermits = numberOfPermits;
            return this;
        }

        public int getNumberOfTrips() {
            return numberOfTrips;
        }

        public Ecmt setNumberOfTrips(int numberOfTrips) {
            this.numberOfTrips = numberOfTrips;
            return this;
        }

        public JourneyProportion getInternationalBusiness() {
            return internationalBusiness;
        }

        public Ecmt setInternationalBusiness(JourneyProportion internationalBusiness) {
            this.internationalBusiness = internationalBusiness;
            return this;
        }

        public Sector getSector() {
            return sector;
        }

        public Ecmt setSector(Sector sector) {
            this.sector = sector;
            return this;
        }

        public LocalDateTime getSubmitDate() {
            return submitDate;
        }

        public void setSubmitDate(LocalDateTime submitDate) {
            this.submitDate = submitDate;
        }

        public PermitUsage getPermitusage(){
            return usage;
        }
        public Ecmt setPermitUsage(PermitUsage permitusage){
            this.usage = permitusage;
            return this;
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
