package org.dvsa.testing.framework.enums;

        import org.jetbrains.annotations.NotNull;
        import java.util.Arrays;

public enum LicenceVehicles {

    GOODS_VEHICLES("//input[@value='lcat_gv']"),
    PUBLIC_SERVICE_VEHICLES("//input[@value='lcat_psv']");

    private String type;

    LicenceVehicles(String type) {
        this.type = type;
    }

    public static org.dvsa.testing.framework.enums.LicenceVehicles getEnum(@NotNull String name) {
        return Arrays.stream(org.dvsa.testing.framework.enums.LicenceVehicles.values()).filter(licenceVehicles -> licenceVehicles.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {return type;}
}

