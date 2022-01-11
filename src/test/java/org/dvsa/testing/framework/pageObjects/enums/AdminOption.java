package org.dvsa.testing.framework.pageObjects.enums;

public enum AdminOption {

    BUS_REGISTRATIONS("Bus registrations"),
    CONTINUATIONS("Continuations"),
    CONTENT_MANAGEMENT("Content Management"),
    DATA_RETENTION("Data retention"),
    FEATURE_TOGGLE("Feature toggle"),
    FEE_RATES("Fee rates"),
    FINANCIAL_STANDING_RATES("Financial standing rates"),
    PAYMENT_PROCESSING("Payment processing"),
    PERMITS("Permits"),
    PRINTING("Printing"),
    PUBLIC_HOLIDAYS("Public holidays"),
    PUBLICATIONS("Publications"),
    REPORTS("Reports"),
    SCANNING("Scanning"),
    SYSTEM_MESSAGES("System messages"),
    SYSTEM_PARAMETERS("System parameters"),
    TASK_ALLOCATION_RULES("Task allocation rules"),
    USER_MANAGEMENT("User management"),
    YOUR_ACCOUNT("Your account");

    private String name;

    AdminOption(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


}
