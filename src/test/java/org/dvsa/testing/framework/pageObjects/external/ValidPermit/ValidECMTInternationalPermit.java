package org.dvsa.testing.framework.pageObjects.external.ValidPermit;

import org.dvsa.testing.framework.enums.PermitStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValidECMTInternationalPermit {
    private String permitNumber;
    private String applicationNumber;
    private String notValidForTravel;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private PermitStatus status;


    public ValidECMTInternationalPermit(String permitNumber, String applicationNumber, String notValidForTravel, String startDate, String expiryDate,
                                        String status) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        this.permitNumber = permitNumber;
        this.applicationNumber = applicationNumber;
        this.notValidForTravel= notValidForTravel;
        this.startDate = LocalDate.parse(startDate, formatter);
        this.expiryDate = LocalDate.parse(expiryDate, formatter);
        this.status = PermitStatus.getEnum(status);
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