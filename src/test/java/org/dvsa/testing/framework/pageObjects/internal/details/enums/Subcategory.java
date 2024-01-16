package org.dvsa.testing.framework.pageObjects.internal.details.enums;

import java.util.Arrays;

public enum Subcategory {
    Application,
    TM1AssistedDigital("TM1 Assisted Digital"),
    OtherDocuments("Other Documents"),
    FormsAssistedDigital("Forms Assisted Digital"),
    FeeRequest("Fee Request"),
    FormsDigital("Forms Digital");

    private String name;

    Subcategory() {
    }

    Subcategory(String name) {
        this.name = name;
    }

    public static org.dvsa.testing.framework.pageObjects.internal.details.enums.Subcategory toEnum(String subcategory) {
        return Arrays.stream(org.dvsa.testing.framework.pageObjects.internal.details.enums.Subcategory.values())
                .filter(sc -> subcategory.toLowerCase().trim().contains(sc.toString().toLowerCase().trim())).findFirst().get();
    }

    @Override
    public String toString() {
        return name == null ? this.name() : name;
    }
}
