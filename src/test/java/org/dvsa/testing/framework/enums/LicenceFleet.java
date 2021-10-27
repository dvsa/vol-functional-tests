package org.dvsa.testing.framework.enums;

        import org.jetbrains.annotations.NotNull;
        import java.util.Arrays;

public enum LicenceFleet {

    LGV_ONLY("//input[@value='app_veh_type_lgv']"),
    MIXED("//input[@value='app_veh_type_mixed']");

    private String type;

    LicenceFleet(String type) {
        this.type = type;
    }

    public static org.dvsa.testing.framework.enums.LicenceFleet getEnum(@NotNull String name) {
        return Arrays.stream(org.dvsa.testing.framework.enums.LicenceFleet.values()).filter(licenceFleet -> licenceFleet.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {return type;}
}


