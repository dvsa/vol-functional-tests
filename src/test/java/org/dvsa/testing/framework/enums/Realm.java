package org.dvsa.testing.framework.enums;

public enum Realm {
    EXTERNAL("selfserve"),
    INTERNAL("internal");

    private final String serviceType;
    Realm(String serviceType) {
        this.serviceType = serviceType;
    }
    public String getServiceType() {
        return serviceType;
    }
}
