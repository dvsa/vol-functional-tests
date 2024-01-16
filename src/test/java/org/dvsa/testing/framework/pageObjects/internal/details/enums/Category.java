package org.dvsa.testing.framework.pageObjects.internal.details.enums;

import java.util.Arrays;

public enum Category {
    Permits,
    TransportManager("Transport Manager"),
    Licensing,
    Application;

    private String name;

    Category() {
    }

    Category(String name) {
        this.name = name;
    }

    public static org.dvsa.testing.framework.pageObjects.internal.details.enums.Category toEnum(String category) {
        return Arrays.stream(org.dvsa.testing.framework.pageObjects.internal.details.enums.Category.values())
                .filter(c -> c.toString().equalsIgnoreCase(category.trim())).findFirst().get();
    }

    @Override
    public String toString() {
        return name == null ? this.name() : this.name;
    }
}
