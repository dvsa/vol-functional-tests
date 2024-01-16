package org.dvsa.testing.framework.enums;

public enum SelfServeNavBar {

    HOME("Home"),
    BUS_REGISTRATIONS("Bus Registrations"),
    MANAGE_USERS("Manage users"),
    YOUR_ACCOUNT("Your account"),
    SIGN_OUT("Sign out");

    private final String navbarHeading;

    SelfServeNavBar(String navbarHeading) {
        this.navbarHeading = navbarHeading;
    }

    @Override
    public String toString() {
        return navbarHeading;
    }
}
