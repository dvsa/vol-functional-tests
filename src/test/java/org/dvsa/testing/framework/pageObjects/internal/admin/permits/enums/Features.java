package org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums;

public enum Features {
    PermitsAdmin("Permits admin"),
    InternalEcmt("Internal ECMT"),
    InternalPermits("Internal permits"),
    SelfserveEcmt("Selfserve ECMT"),
    SelfservePermits("Selfserve permits"),
    BackendEcmt("Backend ECMT"),
    BackendPermits("Backend permits"),
    BackendSurrender("Backend Surrender"),
    InternalSurrender("Internal Surrender"),
    SelfserveSurrender("Selfserve Surrender");

    private String name;

    Features(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}