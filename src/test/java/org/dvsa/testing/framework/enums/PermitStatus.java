package org.dvsa.testing.framework.enums;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum PermitStatus {

    PRINTED("Printed"),
    PENDING("Pending"),
    VALID("Valid"),
    PROCESSING("Processing"),
    FEE_PAID("Fee Paid"),
    WITHDRAWN("Withdrawn"),
    EXPIRED("Expired"),
    NOT_YET_SUBMITTED("Not Yet Submitted"),
    CANCELLED("Cancelled"),
    UNDER_CONSIDERATION("Under Consideration"),
    AWAITING_FEE("Awaiting Fee"),
    NOT_STARTED_YET("Not started yet"),
    COMPLETED("Completed"),
    CANT_START_YET("Can't start yet");

    private String status;

    PermitStatus(String status) {
        this.status = status;
    }

    public static org.dvsa.testing.framework.enums.PermitStatus getEnum(@NotNull String name) {
        return Arrays.stream(org.dvsa.testing.framework.enums.PermitStatus.values()).filter(status -> status.toString().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unable to convert to enum, name: ".concat(name)));
    }

    @Override
    public String toString() {
        return status;
    }

}
