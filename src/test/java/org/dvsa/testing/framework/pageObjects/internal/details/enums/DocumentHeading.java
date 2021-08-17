package org.dvsa.testing.framework.pageObjects.internal.details.enums;

public enum DocumentHeading {
    Licence("Licence selected"),
    Euro6("Mandatory ECMT certificates for vehicles and trailers you intend to use"),
    Cabotage("ECMT permits do not allow you to carry out cabotage"),
    RestrictedCountries("Will you be transporting goods to Austria, Greece, Hungary, Italy or Russia?"),
    NumberOfPermits("How many permits do you require for this licence?"),
    NumberOfTrips("How many international trips did you make in the last"),
    PercentageOfInternationalTrips("In the last 12 months, what percentage"),
    Sector("Select one sector you mainly transport goods in using this licence");

    private String heading;

    DocumentHeading(String heading) {
        this.heading = heading;
    }

    @Override
    public String toString(){
        return heading;
    }
}
