package org.dvsa.testing.framework.Utils.store;

import apiCalls.Utils.eupaBuilders.external.PersonModel;
import apiCalls.Utils.eupaBuilders.organisation.ResultModel;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OperatorStore {

    private String organisationName;
    private String username;
    private String email;
    private String country;
    private List<LicenceStore> licences = new ArrayList<>();
    private String currentLicence;
    private PermitType currentPermitType;
    private PeriodType currentShortTermType;

    public PeriodType getCurrentBilateralPeriodType() {
        return currentBilateralPeriodType;
    }

    public void setCurrentBilateralPeriodType(PeriodType currentBilateralPeriodType) {
        this.currentBilateralPeriodType = currentBilateralPeriodType;
    }

    private PeriodType currentBilateralPeriodType;

    public String getCountry()
    {
        return country;
    }

    public OperatorStore setCountry(String country)
    {
        this.country= country;
        return this;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public String getUsername() {
        return username;
    }

    public OperatorStore setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public OperatorStore setEmail(String email) {
        this.email = email;
        return this;
    }

    public List<LicenceStore> getLicences() {
        return licences;
    }

    public List<LicenceStore> getLicences(String licenceNumber) {
        return licences((LicenceStore licence) -> licence.getLicenceNumber().equalsIgnoreCase(licenceNumber));
    }

    public List<LicenceStore> licences(Predicate<LicenceStore> test) {
        return licences.stream().filter(test).collect(Collectors.toList());
    }

    public Optional<LicenceStore> getLatestLicence() {
        Optional<LicenceStore> licence = Optional.empty();
        int lastLicenceIdx = getLicences().size() - 1;
                if (lastLicenceIdx > -1)
            licence = Optional.of(getLicences().get(lastLicenceIdx));
             return licence;
    }

    public OperatorStore withLicences(LicenceStore licence) {
        if (!hasLicence(licence))
            this.licences.add(licence);
        return this;
    }

    private boolean hasLicence(LicenceStore licence) {
        return licences.stream()
                .anyMatch(
                        (currentLicence) -> currentLicence.equals(licence)
                );
    }


    public Optional<String> getCurrentLicenceNumber() {
        return currentLicence == null ? Optional.empty() : Optional.of(currentLicence);
    }

    public Optional<LicenceStore> getCurrentLicence() {
        if (getCurrentLicenceNumber().isPresent()) {
            return Optional.of(getLicences(getCurrentLicenceNumber().get()).get(0));
        } else {
            return Optional.empty();
        }
    }

    public void setCurrentPermitType(PermitType currentPermitType) {
        this.currentPermitType = currentPermitType;
    }

    public void setCurrentShortTermType(PeriodType currentShortTermType){
        this.currentShortTermType = currentShortTermType;
    }

    public void setCurrentLicenceNumber(String currentLicence) {
        this.currentLicence = currentLicence;
    }
}
