package org.dvsa.testing.framework.Utils.store.permit;

import org.dvsa.testing.lib.newPages.common.type.Permit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnnualMultilateralStore {
    String reference;
    List<Permit> numberOfPermits = new ArrayList<>();
    private LocalDateTime applicationDate;

    public String getReference() {
        return reference;
    }

    public AnnualMultilateralStore setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public List<Permit> getNumberOfPermits() {
        return numberOfPermits;
    }

    public int totalNumberOfPermits() {
        return getNumberOfPermits().stream().mapToInt(Permit::getNumberOfPermits).sum();
    }

    public AnnualMultilateralStore setNumberOfPermits(List<Permit> numberOfPermits) {
        this.numberOfPermits = numberOfPermits;
        return this;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public AnnualMultilateralStore setApplicationDate() {
        this.applicationDate = LocalDateTime.now();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnualMultilateralStore that = (AnnualMultilateralStore) o;
        return Objects.equals(getReference(), that.getReference());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReference());
    }
}
