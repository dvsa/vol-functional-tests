package org.dvsa.testing.framework.enums;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum PermitType {

    ANNUAL_BILATERAL("Bilateral permits"),
    ANNUAL_MULTILATERAL("Annual Multilateral (EU and EEA)"),
    CERTIFICATE_OF_ROADWORTHINESS_FOR_VEHICLES("Certificate of Roadworthiness for vehicles"),
    CERTIFICATE_OF_ROADWORTHINESS_FOR_TRAILERS("Certificate of Roadworthiness for trailers"),
    ECMT_ANNUAL("Annual ECMT"),
    ECMT_INTERNATIONAL_REMOVAL("ECMT International Removal"),
    SHORT_TERM_ECMT("Short-term ECMT");

    private String type;

    PermitType(String type) {
        this.type = type;
    }

    public static org.dvsa.testing.framework.enums.PermitType getEnum(@NotNull String name) {
        return Arrays.stream(org.dvsa.testing.framework.enums.PermitType.values()).filter(permitType -> permitType.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {
        return type;
    }

}
