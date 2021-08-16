package org.dvsa.testing.framework.pageObjects.type;

import org.dvsa.testing.framework.pageObjects.enums.Country;

import java.util.Objects;
import java.util.Optional;

public class Permit {
    private Country country;
    private String type;
    private String year;
    private Integer feePerPermit;
    private Integer totalFee;
    private int numberOfPermits;
    private int quantity;

    public Permit(Country country, String year, int quantity) {
        this.country = country;
        this.year = year;
        this.quantity = quantity;
    }

    public Permit(String year, int numberOfPermits, int feePerPermit) {
        this.year = year;
        this.numberOfPermits = numberOfPermits;
        this.feePerPermit = feePerPermit;
    }

    public String getYear() {
        return year;
    }

    public int getNumberOfPermits() {
        return numberOfPermits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        org.dvsa.testing.framework.pageObjects.type.Permit permit = (org.dvsa.testing.framework.pageObjects.type.Permit) o;
        return getNumberOfPermits() == permit.getNumberOfPermits() &&
                getYear().equals(permit.getYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYear(), getNumberOfPermits());
    }
}
