package org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums;

import java.util.Arrays;

public enum ECMTType {
    Euro6("Euro 6"),
    Euro5("Euro 5");

    String name;

    ECMTType(String name) {
        this.name = name;
    }

    public static org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.ECMTType toEnum(String value) {
        return Arrays.stream(org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.ECMTType.values())
                .filter(type -> type.toString().equalsIgnoreCase(value)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {
        return name;
    }
}