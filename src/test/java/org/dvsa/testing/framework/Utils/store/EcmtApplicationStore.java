package org.dvsa.testing.framework.Utils.store;

import org.dvsa.testing.framework.pageObjects.type.Permit;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyProportion;
import org.dvsa.testing.framework.pageObjects.external.enums.Sector;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EcmtApplicationStore {

    String reference;
    List<Permit> numberOfPermits = new ArrayList<>();
    private LocalDateTime applicationDate;

    public LicenceStore licence;
    public boolean euro6 = false;
    public boolean cabotage = false;
    //public int numberOfPermits = 0;
    public int numberOfTrips = 0;
    public JourneyProportion internationalBusiness = null;
    public Sector sector = null;
    public boolean declaration = false;

    public String getReference() {
        return reference;
    }

    public EcmtApplicationStore setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public List<Permit> getNumberOfPermits() {
        return numberOfPermits;
    }

    public int totalNumberOfPermits() {
        return getNumberOfPermits().stream().mapToInt(Permit::getNumberOfPermits).sum();
    }

    public EcmtApplicationStore setNumberOfPermits(List<Permit> numberOfPermits) {
        this.numberOfPermits = numberOfPermits;
        return this;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public EcmtApplicationStore setApplicationDate() {
        this.applicationDate = LocalDateTime.now();
        return this;
    }

    public LocalDateTime date = null;

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
    }

}
