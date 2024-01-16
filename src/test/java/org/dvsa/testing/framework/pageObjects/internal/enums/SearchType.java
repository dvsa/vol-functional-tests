package org.dvsa.testing.framework.pageObjects.internal.enums;

public enum SearchType {
    Licence("Licence"),
    Application("Applications"),
    Case("Cases"),
    PsvDisc("PSV Disc"),
    Vehicle("Vehicle"),
    Address("Address"),
    BusRegistrations("Bus Registration"),
    People("People"),
    Users("Users"),
    Publication("Publication"),
    Irfo("IRFO");

    private String itemName;

    SearchType(String itemName) {
        this.itemName = itemName;
    }

    public String toString(){
        return itemName;
    }
}
