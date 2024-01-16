package org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums;

public enum DateField {
    StartDate("Window Start Date"),
    EndDate("Window End Date");

    private String heading;

    DateField(String heading) {
        this.heading = heading;
    }

    @Override
    public String toString(){
        return heading;
    }
}