package org.dvsa.testing.framework.enums;

public enum SelfServeSection {

    ADDRESSES("Addresses"),
    BUSINESS_DETAILS("Business details"),
    BUSINESS_TYPE("Business type"),
    CONDITIONS_AND_UNDERTAKINGS("Conditions and undertakings"),
    CONVICTIONS_AND_PENALTIES("Convictions and penalties"),
    DIRECTORS("Directors"),
    FINANCIAL_EVIDENCE("Financial evidence"),
    FINANCIAL_HISTORY("Financial history"),
    LICENCE_AUTHORISATION("Licence authorisation"),
    LICENCE_DISCS("Licence discs"),
    LICENCE_HISTORY("Licence history"),
    OPERATING_CENTERS_AND_AUTHORISATION("Operating centres and authorisation"),
    REVIEW_AND_DECLARATIONS("Review and declarations"),
    SAFETY_AND_COMPLIANCE("Safety and compliance"),
    TAXI_AND_PHV("Taxi and PHV"),
    TRAILERS("Trailers"),
    TRANSPORT_MANAGERS("Transport Managers"),
    TYPE_OF_LICENCE("Type of licence"),
    VEHICLE_DECLARATIONS("Vehicles Declarations"),
    VEHICLES("Vehicles"),
    VIEW("View");

    private final String section;

    SelfServeSection(String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return section;
    }

}
