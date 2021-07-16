package org.dvsa.testing.framework.pageObjects.external.ValidPermit;

import org.dvsa.testing.framework.enums.PermitStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValidAnnualMultilateralPermit {
    private String permit;
    private String application;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private PermitStatus status;


    public ValidAnnualMultilateralPermit(String permit, String application, String startDate, String expiryDate,
                                         String status) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        this.permit = permit;
        this.application = application;
        this.startDate = LocalDate.parse(startDate, formatter);
        this.expiryDate = LocalDate.parse(expiryDate, formatter);
        this.status = PermitStatus.getEnum(status);
    }

    public String getPermit() {
        return permit;
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
