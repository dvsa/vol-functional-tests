package org.dvsa.testing.framework.pageObjects.external.ValidPermit;

import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.Country;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValidAnnualBilateralPermit {
    private String permit;
    private Country country;
    private String application;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private PermitStatus status;

    public ValidAnnualBilateralPermit(String application, String permit, String country, String startDate, String expiryDate,
                                      String status) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        this.application = application;
        this.permit = permit;
        this.country = Country.toEnum(country);
        this.startDate = LocalDate.parse(startDate, formatter);
        this.expiryDate = LocalDate.parse(expiryDate, formatter);
        this.status = PermitStatus.getEnum(status);
    }

    public String getPermit() {
        return permit;
    }

    public Country getCountry() {
        return country;
    }

    public String getApplication() {
        return application;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public PermitStatus getStatus() {
        return status;
    }
}
