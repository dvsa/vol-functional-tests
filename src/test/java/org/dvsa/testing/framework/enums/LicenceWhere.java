package org.dvsa.testing.framework.enums;

import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

public enum LicenceWhere {

    GREAT_BRITAIN("//input[@id='type-of-licence[operator-location]']"),
    NORTHERN_IRELAND("61790589c0c15");

    private String type;

    LicenceWhere(String type) {
        this.type = type;
    }

    public static org.dvsa.testing.framework.enums.LicenceWhere getEnum(@NotNull String name) {
        return Arrays.stream(org.dvsa.testing.framework.enums.LicenceWhere.values()).filter(licenceWhere -> licenceWhere.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {return type;}
}
