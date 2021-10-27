package org.dvsa.testing.framework.enums;

        import org.jetbrains.annotations.NotNull;
        import java.util.Arrays;

public enum LicenceType {

    RESTRICTED("//input[@value='ltyp_r']"),
    STANDARD_NATIONAL("//input[@value='ltyp_sn']"),
    STANDARD_INTERNATIONAL("//input[@value='ltyp_si']"),
    SPECIAL_RESTRICTED("//input[@value='ltyp_sr']");

    private String type;

    LicenceType(String type) {
        this.type = type;
    }

    public static org.dvsa.testing.framework.enums.LicenceType getEnum(@NotNull String name) {
        return Arrays.stream(org.dvsa.testing.framework.enums.LicenceType.values()).filter(licenceType -> licenceType.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {return type;}
}

